package ruby.bamboo.plugin.jei;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GrindRecipeCategory implements IRecipeCategory {

	private final IDrawableStatic background;

	public GrindRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("bamboomod", "textures/guis/neiguimillstone.png");
		background = guiHelper.createDrawable(location, 8, 6, 160, 75);
	}

	@Override
	public String getUid() {
		return "bamboo.jei.millstone";
	}

	@Override
	public String getTitle() {
		return I18n.translateToLocal(getUid());
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft mc) {
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {

	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		if (!(recipeWrapper instanceof GrindRecipeWrapper))
			return;
		GrindRecipeWrapper wrapper = ((GrindRecipeWrapper) recipeWrapper);

		List inputs = wrapper.getInputs();
		List outputs = wrapper.getOutputs();

		recipeLayout.getItemStacks().init(0, true, 29, 28);
		recipeLayout.getItemStacks().set(0, inputs);

		recipeLayout.getItemStacks().init(1, false, 97, 28);
		recipeLayout.getItemStacks().set(1, (ItemStack) outputs.get(0));
		if (outputs.size() > 1) {
			recipeLayout.getItemStacks().init(2, false, 127, 28);
			recipeLayout.getItemStacks().set(2, (ItemStack) outputs.get(1));
		}
	}

}
