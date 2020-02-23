package ee.blakcat.Repositories;

import com.google.common.base.Strings;
import ee.blakcat.Models.User;
import ru.blakcat.di.annotations.Component;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRepositoryToMap implements UserRepository {

    private HashMap <String, User> userHashMap;

    public UserRepositoryToMap() {
        userHashMap=new HashMap<>();
    }

    @Override
    public User findById (String id) {
if (Strings.isNullOrEmpty(id)) throw new RuntimeException("Id is null");
User user = userHashMap.get(id);
return user;
    }

    @Override
    public User save (User user) {

userHashMap.put(user.getId(), user);
return findById(user.getId());
    }

    @Override
    public Set<User> findAll ( ) {
        return new HashSet<>(userHashMap.values());
    }
}
