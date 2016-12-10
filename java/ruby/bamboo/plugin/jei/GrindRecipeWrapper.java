package ruby.bamboo.plugin.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import ruby.bamboo.api.crafting.grind.GrindInputOreItem;
import ruby.bamboo.api.crafting.grind.IGrindRecipe;

public class GrindRecipeWrapper implements IRecipeWrapper {

    private final List<ItemStack> input = new ArrayList<ItemStack>();
    private final List<ItemStack> output = new ArrayList<ItemStack>();
    private final IGrindRecipe rec;

    @SuppressWarnings("unchecked")
    public GrindRecipeWrapper(IGrindRecipe recipe) {
        rec = recipe;
        output.add(recipe.getOutput());
        if (recipe.getBonus() != null) {
            output.add(recipe.getBonus());
        }
        if (recipe.getInput() instanceof GrindInputOreItem) {
            GrindInputOreItem oreInput = (GrindInputOreItem) recipe.getInput();
            String name = oreInput.oreName;
            List<ItemStack> ores = OreDictionary.getOres(name);
            input.addAll(ores);
        } else {
            input.add(recipe.getInput().getItemStack());
        }
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
        int chance = rec.getBonus() == null ? 0 : (int) (rec.getBonusWeight() * 100);
        if (chance > 0) {
            mc.fontRendererObj.drawString(chance + "%", 131, 15, 0x0099FF, true);
        }
    }

    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

}
