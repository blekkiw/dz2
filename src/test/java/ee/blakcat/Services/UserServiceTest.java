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

import java.util.HashSet;
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
        MockitoAnnotations.initMocks(this);
        user = new User("vasya", "petja");
        user1 = new User("petya", "petja");
        when(userRepository.findAll()).thenReturn(new HashSet<User>() {{
            add(user);
            add(user1);
        }});
        when(userRepository.findById(user.getId())).thenReturn(user);
        when(userRepository.findById(user1.getId())).thenReturn(user1);


        when(userRepository.save(user)).thenReturn(user);

        userService = new UserService(userRepository);


    }




    @Test
    public void userServiceSaveTest () {
        User saved = userService.save(user);
        saved.toString();
        assertTrue(Objects.nonNull(saved));
        User saved2 = userService.save(user1);
        Set <User> users = userService.findAll();
        assertThat(users, contains(saved, saved2));

    }
}
