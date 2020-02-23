package ee.blakcat.Controllers;

import ee.blakcat.Models.User;
import ee.blakcat.Services.Service;
import ee.blakcat.Services.UserService;
import ru.blakcat.di.annotations.Component;
import ru.blakcat.di.annotations.Inject;

import java.util.stream.Collectors;

@Component
public class UserControllerToCMD implements UserController{


    private Service service;

    @Inject
    public UserControllerToCMD(Service service) {
        this.service = service;
    }

    @Override
    public String findById (String id) {

        User user = service.findById(id);
       return user.toString();
    }

    @Override
    public String save (User user) {
        service.save(user);
       return findById(user.getId());
    }

    @Override
    public String findAll ( ) {
       return service.findAll().stream().map(User::toString).collect(Collectors.joining(", "));
    }
}
