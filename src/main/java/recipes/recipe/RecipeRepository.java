package recipes.recipe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.recipe.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);
    List<Recipe> findByCategoryEqualsIgnoreCaseOrderByDateDesc(String category);
    //List<Recipe> findByNameContainingAndCategoryEqualsIgnoreCaseOrderByDate(String name, String Category);
    List<Recipe> findByNameInIgnoreCaseOrderByDateDesc(List<String> names);
}
