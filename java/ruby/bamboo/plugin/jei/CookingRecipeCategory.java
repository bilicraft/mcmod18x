package ruby.bamboo.plugin.jei;

import java.util.Collection;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class CookingRecipeCategory implements IRecipeCategory<CookingRecipeWrapper> {

    public static final String UID = "bamboo.jei.campfire";

    private final IDrawableStatic background;

    private final IDrawableAnimated arrow;

    public CookingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("bamboomod", "textures/guis/campfire.png");
        background = guiHelper.createDrawable(location, 7, 16, 138, 54);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 176, 0, 24, 17);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public String getUid() {
        return UID;
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
    public void drawExtras(Minecraft mc) {}

    @Override
    public void drawAnimations(Minecraft minecraft) {
        arrow.draw(minecraft, 82, 20);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CookingRecipeWrapper recipeWrapper) {

        List inputs = recipeWrapper.getInputs();
        List outputs = recipeWrapper.getOutputs();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                int l = i * 3 + j;
                if (l < inputs.size() && inputs.get(l) != null) {

                    recipeLayout.getItemStacks().init(l, true, 22 + j * 18, 0 + i * 18);
                    if (inputs.get(l) instanceof ItemStack) {
                        recipeLayout.getItemStacks().set(l, (ItemStack) inputs.get(l));
                    } else if (inputs.get(l) instanceof Collection) {
                        recipeLayout.getItemStacks().set(l, (Collection<ItemStack>) inputs.get(l));
                    }

                }

            }
        }

        recipeLayout.getItemStacks().init(9, false, 116, 18);
        recipeLayout.getItemStacks().set(9, (ItemStack) outputs.get(0));
    }

}
