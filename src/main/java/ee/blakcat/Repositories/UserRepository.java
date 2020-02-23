package ee.blakcat.Repositories;

import com.google.common.base.Strings;
import ee.blakcat.Models.User;

import java.util.HashSet;
import java.util.Set;

public interface UserRepository {


    public User findById (String id) ;

    public User save (User user) ;

    public Set<User> findAll ( ) ;
}
