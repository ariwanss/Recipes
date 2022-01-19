package recipes.recipe;

import org.springframework.stereotype.Component;
import recipes.userDetail.UserDetailsImpl;

import java.util.Objects;

@Component(value = "recipeOwnerEvaluator")
public class RecipeOwnerEvaluator {

    private final RecipeService recipeService;

    public RecipeOwnerEvaluator(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public boolean evaluate(long id, UserDetailsImpl userDetails) {
        return Objects.equals(recipeService.getRecipe(id).getAuthor().getEmail(), userDetails.getUsername());
    }
}
