package recipes.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import recipes.user.User;
import recipes.userDetail.UserDetailsImpl;
import recipes.userDetail.UserDetailsServiceImpl;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
class RecipeControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService recipeService;

    @MockBean(name = "userDetailsService")
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean(name = "recipeOwnerEvaluator")
    private RecipeOwnerEvaluator recipeOwnerEvaluator;

    Recipe recipe1;
    Recipe recipe2;

    void initRecipes() {
        recipe1 = new Recipe(
                "Super Delicious Ice Cream",
                "Dessert",
                "A super delicious ice cream",
                new String[]{"ingredient1", "ingredient2", "ingredient3"},
                new String[]{"step1", "step2", "step3", "step4", "step5"}
        );
        recipe1.setId(1);

        recipe2 = new Recipe(
                "Not So Delicious Ice Cream",
                "Dessert",
                "Mediocre ice cream",
                new String[]{"ingredient1", "ingredient2", "ingredient3", "ingredient4"},
                new String[]{"step1", "step2", "step3", "step4", "step5", "step6"}
        );
        recipe2.setId(2);
    }

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        initRecipes();
    }

    @Test
    @WithMockUser(username = "someone")
    void postRecipeOk() throws Exception {
        Mockito.when(recipeService.insertRecipe(recipe1)).thenReturn(recipe1);

        mockMvc.perform(
                post("/api/recipe/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Super Delicious Ice Cream")))
                ;
    }

    @Test
    void postRecipeUnauthorized() throws Exception {
        Mockito.when(recipeService.insertRecipe(recipe1)).thenReturn(recipe1);

        mockMvc.perform(
                        post("/api/recipe/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(recipe1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getRecipeByIdOk() throws Exception {
        Mockito.when(recipeService.getRecipe(1)).thenReturn(recipe1);

        mockMvc.perform(get("/api/recipe/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getRecipeByIdUnauthorized() throws Exception {
        mockMvc.perform(get("/api/recipe/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getRecipeByIdNotFound() throws Exception {
        Mockito.when(recipeService.getRecipe(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/recipe/1")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void searchRecipeByCategoryOk() throws Exception {
        Set<Recipe> toReturn = Set.of(recipe1, recipe2);

        Mockito.when(recipeService.searchRecipes(null, "dessert")).thenReturn(toReturn);

        mockMvc.perform(get("/api/recipe/search?category=dessert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser
    void searchRecipeByNameOk() throws Exception {
        Set<Recipe> toReturn = Set.of(recipe1, recipe2);

        Mockito.when(recipeService.searchRecipes("ice+cream", null)).thenReturn(toReturn);

        mockMvc.perform(get("/api/recipe/search?name=ice+cream"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void searchRecipeUnauthorized() throws Exception {
        mockMvc.perform(get("/api/recipe/search?name=ice+cream"))
                .andExpect(status().isUnauthorized());
    }

    /*@Test
    @WithMockUser(username = "someone@gmail.com", password = "password123")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
    void updateRecipeOk() throws Exception {

        User user = new User("someone@gmail.com", passwordEncoder.encode("password123"));
        user.setRole("ROLE_USER");

        Recipe recipe1 = new Recipe(
                "Super Delicious Ice Cream",
                "Dessert",
                "A super delicious ice cream with updated descriptions",
                new String[]{"ingredient1", "ingredient2", "ingredient3"},
                new String[]{"step1", "step2", "step3", "step4", "step5"}
        );
        recipe1.setId(1);
        recipe1.setAuthor(user);

        Mockito.when(recipeOwnerEvaluator.evaluate(1, new UserDetailsImpl(user))).thenReturn(true);
        Mockito.when(recipeService.updateRecipe(1, recipe1)).thenReturn(recipe1);

        mockMvc.perform(
                put("/api/recipe/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe1))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Asuper delicious ice cream with updated descriptions")));
    }*/
}