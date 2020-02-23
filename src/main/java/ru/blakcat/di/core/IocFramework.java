package ru.blakcat.di.core;

import com.google.common.base.Strings;
import org.checkerframework.checker.units.qual.A;
import org.reflections.Reflections;
import ru.blakcat.di.annotations.Component;
import ru.blakcat.di.annotations.GoFrameworkApp;
import ru.blakcat.di.annotations.Inject;

import java.lang.reflect.*;
import java.util.*;

public final class IocFramework {
    private static Map<Class<?>, Object> container = new HashMap<>();

    private IocFramework() {
    }


    private static void findComponents(String pack) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections(pack);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Component.class);
        Map<Class<?>, Constructor<?>[]> constructorsForClass = new HashMap<>();
        for (Class<?> type : types) {
            constructorsForClass.put(type, type.getConstructors());
            for (Constructor<?> constructor : type.getConstructors()) {
                if (constructor.getAnnotation(Inject.class) == null && constructor.getParameterCount() == 0) {
                    Object bean = constructor.newInstance(null);
                    fillContainer(type, bean);
                }
            }
        }

        List<Class<?>> sortedClasses = sortClasses(new ArrayList<>(types));
        injectToNonEmptyConstructor(sortedClasses);
        injectToAnnotatedConstructor(sortedClasses);
        injectToMethod();


    }



    private static void injectToNonEmptyConstructor(List<Class<?>> sortedClasses) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        for ( Class<?> classEntry : sortedClasses) {
            for (Constructor<?> constructor : classEntry.getConstructors()) {
                if (constructor.getParameterCount() != 0) {
                    Object bean = createObjectRecursion(classEntry, constructor);
                    fillContainer(bean.getClass(), bean);
                }
            }
        }

    }

    private static <T> T createObjectRecursion (Class<T> clazz, Constructor constructor) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        List<Object> objects = new ArrayList<>();
        Parameter[] parameters = constructor.getParameters();
        for (Parameter parameter : parameters) {
            Object object = getByInterface(parameter.getType());
            if (object==null) {
                Constructor [] constructors = parameter.getType().getConstructors();
                for (Constructor constructor1 : constructors) {
                    object= createObjectRecursion(parameter.getClass(), constructor1);
                }
            }
            objects.add(object);
        }
        if (objects.contains(null)) return null;
        else {
            T bean = (T) constructor.newInstance(objects.toArray());
            fillContainer(bean.getClass(),bean);
            return bean;
        }
    }

    private static void injectToAnnotatedConstructor (List<Class<?>> sortedClasses) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for ( Class<?> classEntry : sortedClasses) {
            for (Constructor<?> constructor : classEntry.getConstructors()) {
                if (constructor.getAnnotation(Inject.class) != null) {
                    Object bean = createObjectRecursion(classEntry, constructor);
                    fillContainer(bean.getClass(), bean);
                }
            }
        }
    }

    private static void injectToMethod() throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<Class<?>, Object> classObjectEntry : container.entrySet()) {
            if (classObjectEntry.getValue() != null) {
                Method[] methods = classObjectEntry.getValue().getClass().getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getAnnotation(Inject.class) != null) {
                        List<Object> objects = new ArrayList<>();
                        Parameter[] parameters = method.getParameters();
                        for (Parameter parameter : parameters) {
                            objects.add(getByInterface(parameter.getType()));
                        }
                        if (objects.contains(null)) continue;
                        method.invoke(classObjectEntry.getValue(), objects.toArray());
                    }
                }
            }
        }
    }

    private static void fillContainer(Class<?> type, Object bean) {
        Class<?>[] interfaces = type.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            container.put(anInterface, bean);
        }
    }


    public static IocFramework init(String pack) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        findComponents(pack);


        annotationBeanProcessor();

        return null;
    }

    public static <T> T getByInterface(Class<T> myinterface) {

        T bean = (T) container.get(myinterface);
//        if (bean == null) throw new RuntimeException();

        return bean;
    }

    private static void annotationBeanProcessor() throws IllegalAccessException {
        Collection<Object> beans = container.values();
        for (Object bean : beans) {
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (Objects.nonNull(declaredField.getAnnotation(Inject.class))) {
                    declaredField.setAccessible(true);
                    Class<?> interfaceOfThis = declaredField.getType();
                    Object injection = getByInterface(interfaceOfThis);
                    declaredField.set(bean, injection);
                }
            }
        }


    }

    public static void run(Class<?> mainClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        GoFrameworkApp goFrameworkApp = mainClass.getAnnotation(GoFrameworkApp.class);
        if (Objects.isNull(goFrameworkApp)) throw new RuntimeException("Annotation not found");
        String path = goFrameworkApp.value();
        if (Strings.isNullOrEmpty(path)) path = mainClass.getPackage().getName();
        init(path);
    }


    private static List<Class<?>> sortClasses (List<Class<?>> classes) {
        classes.sort(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> aClass, Class<?> t1) {

                return parametrsLength(aClass.getConstructors()).compareTo(parametrsLength(t1.getConstructors()));
            }

            private Integer parametrsLength (Constructor<?> [] constructors) {
                int length = 0;
                for (Constructor<?> constructor : constructors) {
                    length+=constructor.getParameterCount();
                }
                return length;
            }

        });
        return classes;
    }
}
