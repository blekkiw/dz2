package ee.blakcat.Services;

import ee.blakcat.Models.User;
import ee.blakcat.Repositories.UserRepository;
import ee.blakcat.Repositories.UserRepositoryToMap;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Objects;
import java.util.Set;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;
    User user;
     User  user1;

    @Before
    public void init() {
        when(userRepository.save(user)).thenReturn(user);
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
        user = new User("vasya", "petja");
        user1 = new User("petya", "petja");

    }




    @Test
    public void userServiceSaveTest () {
        User saved = userService.save(user);
        saved.toString();
        assertTrue(Objects.nonNull(saved));
        User saved2 = userService.save(user1);
        Set <User> users = userService.findAll();
        assertThat(users, contains(saved2, saved));

    }
}
