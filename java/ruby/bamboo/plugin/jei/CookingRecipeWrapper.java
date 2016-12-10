package ruby.bamboo.plugin.jei;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import ruby.bamboo.api.crafting.RecipeWrapper;
import scala.actors.threadpool.Arrays;

public class CookingRecipeWrapper implements IRecipeWrapper {

    private final RecipeWrapper recipe;

    @SuppressWarnings("unchecked")
    public CookingRecipeWrapper(RecipeWrapper recipe) {
        this.recipe = recipe;
    }

    @Override
    public List getInputs() {
        return Arrays.asList(recipe.getInputTable());
    }

    @Override
    public List getOutputs() {
        return ImmutableList.of(recipe.getRecipeOutput());
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
        int i = (102400 - recipe.getFuelCost()) * 32 / 102400;
        if (i > 30) {
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
                s.add("FUEL COST: " + recipe.getFuelCost());
                s.add("COOKING TIME: " + recipe.getTotalCookTime());
            }
        }
        return s.isEmpty() ? null : s;
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

}
