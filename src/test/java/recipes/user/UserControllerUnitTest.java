package recipes.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import recipes.userDetail.UserDetailsServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    PasswordEncoder passwordEncoder;

    User user;
    Map<String, Object> userEmailAndPassword;

    void initUser() {
        user = new User("someone@gmail.com", "password123");
        user.setId(1L);
        user.setRole("ROLE_USER");

        userEmailAndPassword = new HashMap<>();
        userEmailAndPassword.put("id", 1);
        userEmailAndPassword.put("email", "someone@gmail.com");
        userEmailAndPassword.put("password", "password123");
    }

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        initUser();
    }

    @Test
    void registerUserOk() throws Exception {
        Mockito.when(userService.insertUser(user)).thenReturn(user);

        mockMvc.perform(
                post("/api/register")
                        //.accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEmailAndPassword))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("someone@gmail.com")))
        ;
    }

    @Test
    void registerUserAlreadyExists() throws Exception {
        Mockito.when(userService.insertUser(user))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists"));

        mockMvc.perform(
                post("/api/register")
                        //.accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEmailAndPassword))
        )
                .andExpect(status().isBadRequest());
    }
}