package recipes.recipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipeServiceUnitTest {

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    RecipeService recipeService;

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
        MockitoAnnotations.openMocks(this);
        initRecipes();
    }

    @Test
    void insertRecipe() {
        Mockito.when(recipeRepository.save(recipe1)).thenReturn(recipe1);

        assertEquals(recipe1, recipeService.insertRecipe(recipe1));
    }

    @Test
    void getRecipe() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe1));

        assertEquals(recipe1, recipeService.getRecipe(1));
    }

    @Test
    void getRecipeNotFound() {
        Mockito.when(recipeRepository.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        assertThrows(ResponseStatusException.class, () -> recipeService.getRecipe(1), 
                HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    void getRecipesByName() {
        List<Recipe> toReturn = List.of(recipe1, recipe2);

        Mockito.when(recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc("ice")).thenReturn(toReturn);
        Mockito.when(recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc("cream")).thenReturn(toReturn);

        Set<Recipe> actual = recipeService.getRecipesByName("ice+cream");
        Set<Recipe> expected = Set.of(toReturn.toArray(Recipe[]::new));

        assertEquals(expected, actual);
    }

    @Test
    void getRecipesByCategory() {
        List<Recipe> toReturn = List.of(recipe1, recipe2);

        Mockito.when(recipeRepository.findByCategoryEqualsIgnoreCaseOrderByDateDesc("dessert")).thenReturn(toReturn);

        Set<Recipe> actual = recipeService.getRecipesByCategory("dessert");
        Set<Recipe> expected = Set.of(toReturn.toArray(Recipe[]::new));

        assertEquals(expected, actual);
    }

    @Test
    void searchRecipeByName() {
        List<Recipe> toReturn = List.of(recipe1, recipe2);

        Mockito.when(recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc("ice")).thenReturn(toReturn);
        Mockito.when(recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc("cream")).thenReturn(toReturn);

        Set<Recipe> actual = recipeService.searchRecipes("ice+cream", null);
        Set<Recipe> expected = Set.of(toReturn.toArray(Recipe[]::new));

        assertEquals(expected, actual);
    }

    @Test
    void searchRecipeByCategory() {
        List<Recipe> toReturn = List.of(recipe1, recipe2);

        Mockito.when(recipeRepository.findByCategoryEqualsIgnoreCaseOrderByDateDesc("dessert")).thenReturn(toReturn);

        Set<Recipe> actual = recipeService.searchRecipes(null, "dessert");
        Set<Recipe> expected = Set.of(toReturn.toArray(Recipe[]::new));

        assertEquals(expected, actual);
    }

    @Test
    void updateRecipe() {
        recipe1.setDescription("A new descriptions");

        Mockito.when(recipeRepository.existsById(1L)).thenReturn(true);
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe1));
        Mockito.when(recipeRepository.save(recipe1)).thenReturn(recipe1);

        assertEquals(recipe1.getDescription(), recipeService.updateRecipe(1, recipe1).getDescription());
    }

    @Test
    void updateRecipeNotFound() {
        recipe1.setDescription("A new descriptions");

        Mockito.when(recipeRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(ResponseStatusException.class, () -> recipeService.updateRecipe(1, recipe1), 
                HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    void deleteRecipe() {
        Mockito.when(recipeRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(recipeRepository).deleteById(1L);
    }

    @Test
    void deleteRecipeNotFound() {
        Mockito.when(recipeRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> recipeService.deleteRecipe(1),
                HttpStatus.NOT_FOUND.getReasonPhrase());
    }
}