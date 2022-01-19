package recipes.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import recipes.recipe.Recipe;
import recipes.recipe.RecipeService;
import recipes.user.User;
import recipes.user.UserService;

//@Component
public class DatabaseBootstrap implements CommandLineRunner {

    private final UserService userService;
    private final RecipeService recipeService;

    public DatabaseBootstrap(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User("ariwan@gmail.com", "password123");


        Recipe recipe1 = new Recipe(
                "Super Delicious Ice Cream",
                "Dessert",
                "A super delicious ice cream",
                new String[]{"ingredient1", "ingredient2", "ingredient3"},
                new String[]{"step1", "step2", "step3", "step4", "step5"}
        );

        Recipe recipe2 = new Recipe(
                "Boba Milk Tea",
                "Beverages",
                "Milk tea with tapioca pearls",
                new String[]{"ingredient1", "ingredient2", "ingredient3", "ingredient4"},
                new String[]{"step1", "step2", "step3", "step4", "step5", "step6"}
        );

        Recipe recipe3 = new Recipe(
                "Pepperoni Pizza",
                "Food",
                "A pepperoni pizza",
                new String[]{"ingredient1", "ingredient2", "ingredient3", "ingredient4", "ingredient5"},
                new String[]{"step1", "step2", "step3", "step4", "step5", "step6", "step7"}
        );

        user1.getRecipes().add(recipe1);
        user1.getRecipes().add(recipe2);
        user1.getRecipes().add(recipe3);
        recipe1.setAuthor(user1);
        recipe2.setAuthor(user1);
        recipe3.setAuthor(user1);

        userService.insertUser(user1);
        recipeService.insertRecipe(recipe1);
        recipeService.insertRecipe(recipe2);
        recipeService.insertRecipe(recipe3);

        System.out.println("User count: " + userService.count());
        System.out.println("Recipe count: " + recipeService.count());

        User user2 = new User("januar@gmail.com", "12345678");

    }
}
