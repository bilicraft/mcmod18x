package ruby.bamboo.plugin.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ruby.bamboo.api.crafting.RecipeWrapper;
import ruby.bamboo.api.crafting.grind.IGrindInputItem;
import ruby.bamboo.api.crafting.grind.IGrindRecipe;
import ruby.bamboo.crafting.CookingManager;
import ruby.bamboo.crafting.GrindManager;
import scala.actors.threadpool.Arrays;

@JEIPlugin
public class BambooJEIPlugin  implements IModPlugin {

	private IJeiHelpers helper;

	@Override
	public void register(IModRegistry registry) {
		helper = registry.getJeiHelpers();
		registry.addRecipeCategories(new GrindRecipeCategory(helper.getGuiHelper()),new CookingRecipeCategory(helper.getGuiHelper()));
		registry.addRecipeHandlers(new GrindRecipeHandler(),new CookingRecipeHandler());

		grindList.addAll(GrindManager.getRecipeList());
		registry.addRecipes(grindList);

		for(IRecipe rec : CookingManager.getInstance().getRecipeList()){
			if (rec instanceof RecipeWrapper) {
				CookingWrapper wrapper = new CookingWrapper((RecipeWrapper) rec);
				if (!wrapper.hasEmptyList) {
					cookingList.add(wrapper);
				}
			} else {
				CookingWrapper wrapper = new CookingWrapper(new RecipeWrapper(rec));
				if (!wrapper.hasEmptyList) {
					cookingList.add(wrapper);
				}
			}
		}
		registry.addRecipes(cookingList);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

	private final List<IGrindRecipe> grindList = new ArrayList<IGrindRecipe>();
	private final List<CookingWrapper> cookingList = new ArrayList<CookingWrapper>();

	// wrapper in wrapper
	public class CookingWrapper {
		private final ItemStack output;
		private final int cookTime;
		private final int cookCost;
		private Object[] inputObj;
		private boolean hasEmptyList = false; // Empty OreDic を除外

		@SuppressWarnings("unchecked")
		public CookingWrapper(RecipeWrapper recipe) {
			output = recipe.getRecipeOutput();
			cookTime = recipe.getTotalCookTime();
			cookCost = recipe.getFuelCost();
			if (recipe.getRecipe() instanceof ShapedOreRecipe) {
				ShapedOreRecipe shaped = (ShapedOreRecipe) recipe.getRecipe();
				inputObj = new Object[shaped.getRecipeSize()];
				for (int i = 0 ; i < shaped.getRecipeSize() ; i++) {
					Object obj = shaped.getInput()[i];
					inputObj[i] = obj;
					if (obj != null && obj instanceof List) {
						if (((List)obj).isEmpty()){
							hasEmptyList = true;
						}
					}
				}
			} else if (recipe.getRecipe() instanceof ShapelessOreRecipe) {
				ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe.getRecipe();
				inputObj = new Object[shapeless.getRecipeSize()];
				for (int i = 0 ; i < shapeless.getRecipeSize() ; i++) {
					Object obj = shapeless.getInput().get(i);
					inputObj[i] = obj;
					if (obj != null && obj instanceof List) {
						if (((List)obj).isEmpty()){
							hasEmptyList = true;
						}
					}
				}
			} else if (recipe.getRecipe() instanceof ShapedRecipes) {
				ShapedRecipes shaped = (ShapedRecipes) recipe.getRecipe();
				inputObj = shaped.recipeItems;
			} else if (recipe.getRecipe() instanceof ShapelessRecipes) {
				ShapelessRecipes shapeless = (ShapelessRecipes) recipe.getRecipe();
				inputObj = new Object[shapeless.getRecipeSize()];
				for (int i = 0 ; i < shapeless.getRecipeSize() ; i++) {
					inputObj[i] = shapeless.recipeItems.get(i);
				}
			} else {
				inputObj = new Object[9];
			}
		}

		public int getCookTime() {
			return cookTime;
		}

		public int getCookCost() {
			return cookCost;
		}

		public ItemStack getOutput() {
			return output;
		}

		public Object[] getInputs() {
			return inputObj;
		}
	}

}
