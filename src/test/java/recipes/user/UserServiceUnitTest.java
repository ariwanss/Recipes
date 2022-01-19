package recipes.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceUnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    User user;

    void initUser() {
        user = new User("someone@gmail.com", "password123");
        user.setId(1L);
        user.setRole("ROLE_USER");
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        initUser();
    }

    @Test
    void insertUser() {
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.insertUser(user));
    }

    @Test
    void insertUserAlreadyExists() {
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.insertUser(user), "User already exists!");
    }
}