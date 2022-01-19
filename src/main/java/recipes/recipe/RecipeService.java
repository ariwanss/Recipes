package recipes.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe insertRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe getRecipe(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Set<Recipe> getRecipesByName(String name) {
        String[] names = name.split("\\+");
        Set<Recipe> toReturn = new HashSet<>();
        for (String n : names) {
            if (toReturn.isEmpty()) {
                toReturn.addAll(recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(n));
            } else {
                toReturn.retainAll(recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(n));
            }
        }
        return toReturn;
    }

    public Set<Recipe> getRecipesByCategory(String category) {
        return new HashSet<>(recipeRepository.findByCategoryEqualsIgnoreCaseOrderByDateDesc(category));
    }

    public Set<Recipe> searchRecipes(String name, String category) {
        if (name == null && category == null) {
            return (HashSet<Recipe>) recipeRepository.findAll();
        } else if (name == null) {
            return getRecipesByCategory(category);
        } else if (category == null) {
            return getRecipesByName(name);
        } else {
            Set<Recipe> toReturn = getRecipesByName(name);
            toReturn.retainAll(getRecipesByCategory(category));
            return toReturn;
        }
    }

    public void deleteRecipe(long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
        }
    }

    public Recipe updateRecipe(long id, Recipe recipe) {
        if (recipeRepository.existsById(id)) {
            Recipe recipeInDb = recipeRepository.findById(id).get();
            recipeInDb.setName(recipe.getName());
            recipeInDb.setCategory(recipe.getCategory());
            recipeInDb.setDescription(recipe.getDescription());
            recipeInDb.setIngredients(recipe.getIngredients());
            recipeInDb.setDirections(recipe.getDirections());
            return recipeRepository.save(recipeInDb);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
        }
    }

    public long count() {
        return recipeRepository.count();
    }
}
