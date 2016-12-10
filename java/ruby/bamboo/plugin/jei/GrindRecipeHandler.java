package ruby.bamboo.plugin.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import ruby.bamboo.api.crafting.grind.IGrindRecipe;

public class GrindRecipeHandler implements IRecipeHandler<IGrindRecipe> {

    @Override
    public Class<IGrindRecipe> getRecipeClass() {
        return IGrindRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return GrindRecipeCategory.UID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(IGrindRecipe recipe) {
        return new GrindRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(IGrindRecipe recipe) {
        return recipe.getInput() != null;
    }

    @Override
    public String getRecipeCategoryUid(IGrindRecipe recipe) {
        return getRecipeCategoryUid();
    }

}
