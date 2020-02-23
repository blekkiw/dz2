package ee.blakcat.Services;

import com.google.common.base.Strings;
import ee.blakcat.Controllers.UserController;
import ee.blakcat.Models.User;
import ee.blakcat.Repositories.UserRepository;
import ee.blakcat.Repositories.UserRepositoryToMap;
import ru.blakcat.di.annotations.Component;
import ru.blakcat.di.annotations.Inject;

import java.util.Set;

@Component
public class UserService implements Service {


    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById (String id) {

        User user = userRepository.findById(id);
        return user;
    }

    @Override
    public User save (User user) {
        userRepository.save(user);
        return findById(user.getId());
    }

    @Override
    public Set<User> findAll ( ) {

        return userRepository.findAll();
    }


    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
