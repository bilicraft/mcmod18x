package ruby.bamboo.plugin.jei;

import java.util.Collection;
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

public class CookingRecipeCategory implements IRecipeCategory {

	private final IDrawableStatic background;

	public CookingRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("bamboomod", "textures/guis/campfire.png");
		background = guiHelper.createDrawable(location, 6, 6, 160, 75, 0, 0, 8, 0);
	}

	@Override
	public String getUid() {
		return "bamboo.jei.campfire";
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
		if (!(recipeWrapper instanceof CookingRecipeWrapper))
			return;
		CookingRecipeWrapper wrapper = ((CookingRecipeWrapper) recipeWrapper);

		Object[] inputs = wrapper.inputObj;
		List outputs = wrapper.getOutputs();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int l = i * 3 + j;
				if (l < inputs.length && inputs[l] != null) {
					recipeLayout.getItemStacks().init(l, true, 31 + j * 18, 10 + i * 18);
					if (inputs[l] instanceof ItemStack) {
						recipeLayout.getItemStacks().set(l, (ItemStack) inputs[l]);
					} else if (inputs[l] instanceof Collection) {
						recipeLayout.getItemStacks().set(l, (Collection<ItemStack>) inputs[l]);
					}
				}
			}
		}

		recipeLayout.getItemStacks().init(9, false, 125, 28);
		recipeLayout.getItemStacks().set(9, (ItemStack) outputs.get(0));
	}

}
