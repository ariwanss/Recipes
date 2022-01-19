package recipes.user;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerUserOk() throws Exception {

        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"someone@gmail.com\",\"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.email", Matchers.is("someone@gmail.com")))
                ;

        /*mockMvc.perform(
                        post("/api/register")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{" +
                                                "\"email\": \"someone@gmail.com\"," +
                                                "\"password\": \"password123\"" +
                                                "}"
                                )
                )
                .andExpect(status().isOk());*/
    }

    @Test
    void registerUserAlreadyExists() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"someone@gmail.com\",\"password\": \"password123\"}"));

        mockMvc.perform(
                        post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\": \"someone@gmail.com\",\"password\": \"password123\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUserNullEmail() throws Exception {
        mockMvc.perform(
                        post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"password\": \"password123\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUserBlankEmail() throws Exception {
        mockMvc.perform(
                        post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\": \" \",\"password\": \"password123\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUserNullPassword() throws Exception {
        mockMvc.perform(
                        post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\": \"someone@gmail.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUserBlankPassword() throws Exception {
        mockMvc.perform(
                        post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\": \"someone@gmail.com\",\"password\": \"        \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUserLessThanEightCharsPassword() throws Exception {
        mockMvc.perform(
                        post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\": \"someone@gmail.com\",\"password\": \"passwor\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUser() {
    }
}