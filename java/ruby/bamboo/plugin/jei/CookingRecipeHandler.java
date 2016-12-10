package ruby.bamboo.plugin.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ruby.bamboo.api.crafting.RecipeWrapper;
import ruby.bamboo.api.crafting.grind.IGrindRecipe;
import ruby.bamboo.plugin.jei.BambooJEIPlugin.CookingWrapper;

public class CookingRecipeHandler implements IRecipeHandler<CookingWrapper> {

	@Override
	public Class<CookingWrapper> getRecipeClass() {
		return CookingWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "bamboo.jei.campfire";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CookingWrapper recipe) {
		return new CookingRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(CookingWrapper recipe) {
		if(recipe == null || recipe.getOutput() == null){
			return false;
		}
		// 隠しレシピっぽいので隠しておく
		return recipe.getOutput().getItem() == Items.DIAMOND ? false : true;
	}

	@Override
	public String getRecipeCategoryUid(CookingWrapper recipe) {
		return getRecipeCategoryUid();
	}

}
