package ru.blakcat.di.core;

import com.google.common.base.Strings;
import ee.blakcat.Controllers.UserController;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import ru.blakcat.di.annotations.Component;
import ru.blakcat.di.annotations.GoFrameworkApp;
import ru.blakcat.di.annotations.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public final class IocFramework {
    private static Map <Class<?>, Object> container=new HashMap<>();
    private IocFramework() {
    }


    private  static void findComponents (String pack) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections(pack);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Component.class);
        Map <Class<?>, Constructor<?> []> constructorsForClass = new HashMap<>();
        for (Class<?> type : types) {
            constructorsForClass.put(type, type.getConstructors());
            for (Constructor<?> constructor : type.getConstructors()) {
               if(constructor.getAnnotation(Inject.class)==null&&constructor.getParameterCount()==0) {
                   Object bean = constructor.newInstance(null);
                  fillContainer(type,bean);
               }
            }
        }

        for (Map.Entry<Class<?>, Constructor<?>[]> classEntry : constructorsForClass.entrySet()) {
            for (Constructor<?> constructor : classEntry.getValue()) {
                if (constructor.getParameterCount()!=0) {
                    List<Object> objects = new ArrayList<>();
                    Parameter [] parameters =constructor.getParameters();
                    for (Parameter parameter : parameters) {
                        objects.add(getByInterface(parameter.getType()));
                    }
                    if (objects.contains(null)) continue;
                    Object bean = constructor.newInstance(objects.toArray());
                    fillContainer(classEntry.getKey(), bean);
                }
            }
        }
        
    }

    private static void fillContainer (Class<?> type, Object bean) {
        Class<?>[] interfaces = type.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            container.put(anInterface, bean);
        }
    }



    public static IocFramework init (String pack) throws IllegalAccessException, InstantiationException, InvocationTargetException {
      findComponents(pack);
                annotationBeanProcessor();

return null;
    }

    public static <T> T getByInterface(Class<T> myinterface) {

        T bean = (T) container.get(myinterface);
        if (bean==null) throw  new RuntimeException();

        return bean;
    }

    private static void annotationBeanProcessor () throws IllegalAccessException {
        Collection<Object> beans = container.values();
        for (Object bean : beans) {
            Field [] declaredFields = bean.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (Objects.nonNull(declaredField.getAnnotation(Inject.class))){
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
        if (Objects.isNull(goFrameworkApp)) throw  new RuntimeException("Annotation not found");
        String path = goFrameworkApp.value();
        if (Strings.isNullOrEmpty(path)) path = mainClass.getPackage().getName();
        init(path);
    }
}
