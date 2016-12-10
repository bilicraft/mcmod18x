package ruby.bamboo.plugin.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Items;
import ruby.bamboo.api.crafting.RecipeWrapper;

public class CookingRecipeHandler implements IRecipeHandler<RecipeWrapper> {

    @Override
    public Class<RecipeWrapper> getRecipeClass() {
        return RecipeWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "bamboo.jei.campfire";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(RecipeWrapper recipe) {
        return new CookingRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(RecipeWrapper recipe) {
        if (recipe == null || recipe.getRecipeOutput() == null) {
            return false;
        }
        // 隠しレシピっぽいので隠しておく
        return recipe.getRecipeOutput().getItem() == Items.DIAMOND ? false : true;
    }

    @Override
    public String getRecipeCategoryUid(RecipeWrapper recipe) {
        return getRecipeCategoryUid();
    }

}
