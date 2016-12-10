package ruby.bamboo.plugin.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ruby.bamboo.api.crafting.RecipeWrapper;
import ruby.bamboo.api.crafting.grind.GrindInputOreItem;
import ruby.bamboo.api.crafting.grind.IGrindRecipe;
import ruby.bamboo.plugin.jei.BambooJEIPlugin.CookingWrapper;
import scala.actors.threadpool.Arrays;

public class CookingRecipeWrapper implements IRecipeWrapper {

	private final List<Object> input = new ArrayList<Object>();
	private final List<ItemStack> output = new ArrayList<ItemStack>();;
	public final int cookTime;
	public final int cookCost;
	public Object[] inputObj;

	@SuppressWarnings("unchecked")
	public CookingRecipeWrapper(CookingWrapper recipe) {
		output.add(recipe.getOutput());
		cookTime = recipe.getCookTime();
		cookCost = recipe.getCookCost();
		inputObj = recipe.getInputs();
		input.addAll(Arrays.asList(inputObj));
	}

	@Override
	public List getInputs() {
		return input;
	}

	@Override
	public List getOutputs() {
		return output;
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return null;
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return null;
	}

	@Override
	public void drawInfo(Minecraft mc, int wid, int hei, int mouseX, int mouseY) {
		ResourceLocation location = new ResourceLocation("bamboomod", "textures/guis/campfire.png");
		mc.getTextureManager().bindTexture(location);
		// 燃焼ゲージ
		int i = (102400 - cookCost) * 32 / 102400;
		if(i > 30){
			i = 30; // 小さいと見えにくいので2ドット程度を最小に
		}
		mc.currentScreen.drawTexturedModalRect(12, 11, 176, 18, 12, i);
	}

	@Override
	public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {

	}

	@Override
	public List<String> getTooltipStrings(int x, int y) {
		List<String> s = new ArrayList<String>();

		if (x > 12 && x < 24) {
			if (y > 12 && y < 44) {
				s.add("FUEL COST: " + cookCost);
				s.add("COOKING TIME: " + cookTime);
			}
		}
		return s.isEmpty() ? null : s;
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

}
