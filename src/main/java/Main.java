import ee.blakcat.Controllers.UserController;
import ee.blakcat.Models.User;
import ru.blakcat.di.annotations.GoFrameworkApp;
import ru.blakcat.di.core.IocFramework;

import java.lang.reflect.InvocationTargetException;


@GoFrameworkApp ("ee.blakcat")
public class Main {

    public static void main(String[] args) {
            try {
                    IocFramework.run(Main.class);
            } catch (IllegalAccessException e) {
                    e.printStackTrace();
            } catch (InvocationTargetException e) {
                    e.printStackTrace();
            } catch (InstantiationException e) {
                    e.printStackTrace();
            }
            UserController userControllerToCMD = IocFramework.getByInterface(UserController.class);





        User user = new User("Vasja", "12345");
        User user1 = new User("katja", "12gsgfsf345");
        User user2 = new User("petja", "123gsgs45");
        User user3 = new User("sasa", "gdsfsd");
        User user4 = new User("rara", "fdfs");
        userControllerToCMD.save(user);
        userControllerToCMD.save(user1);
        userControllerToCMD.save(user2);
        userControllerToCMD.save(user3);
        userControllerToCMD.save(user4);
        System.out.println("**********************************************");
        System.out.println(userControllerToCMD.findAll());
        System.out.println("***************************************************");
        System.out.println(userControllerToCMD.findById(user1.getId()));

    }


}
