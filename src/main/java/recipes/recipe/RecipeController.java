package recipes.recipe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/recipe")
@Validated
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Recipe> postRecipe(@Valid @RequestBody Recipe recipe) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(recipeService.insertRecipe(recipe));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(recipeService.getRecipe(id));
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Set<Recipe>> searchRecipe(@RequestParam(name = "category", required = false) String category,
                                                     @RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(recipeService.searchRecipes(name, category));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@recipeOwnerEvaluator.evaluate(#id, principal)")
    public void deleteRecipe(@PathVariable long id) {
        recipeService.deleteRecipe(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@recipeOwnerEvaluator.evaluate(#id, principal)")
    public ResponseEntity<Recipe> updateCode(@PathVariable long id, @Valid @RequestBody Recipe recipe) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(recipeService.updateRecipe(id, recipe));
    }
}
