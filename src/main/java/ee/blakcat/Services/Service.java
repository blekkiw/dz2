package ee.blakcat.Services;

import ee.blakcat.Models.User;

import java.util.Set;

public interface Service {
    public User findById (String id);

    public User save (User user) ;

    public Set<User> findAll () ;
}
