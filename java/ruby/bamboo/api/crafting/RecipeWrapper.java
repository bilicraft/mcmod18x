package ruby.bamboo.api.crafting;

import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeWrapper implements IRecipe {

    private IRecipe recipe;
    private int time;
    private int cost;
    private Object[] input;
    private boolean hasEmptyList = false;

    public RecipeWrapper(IRecipe recipe) {
        this.recipe = recipe;
        this.time = 200;
        this.cost = 200;

        if (recipe instanceof ShapedOreRecipe) {
            ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
            input = shaped.getInput();
            for (Object in : input) {
                if (in != null && in instanceof List) {
                    if (((List) in).isEmpty()) {
                        hasEmptyList = true;
                    }
                }
            }
        } else if (recipe instanceof ShapelessOreRecipe) {
            ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;
            input = shapeless.getInput().toArray(new Object[0]);
            for (Object in : input) {
                if (in != null && in instanceof List) {
                    if (((List) in).isEmpty()) {
                        hasEmptyList = true;
                    }
                }
            }
        } else if (recipe instanceof ShapedRecipes) {
            ShapedRecipes shaped = (ShapedRecipes) recipe;
            input = shaped.recipeItems;
        } else if (recipe instanceof ShapelessRecipes) {
            ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
            input = shapeless.recipeItems.toArray(new Object[0]);
        } else {
            input = new Object[9];
        }
    }

    public RecipeWrapper setTotalCookTime(int time) {
        this.time = time;
        return this;
    }

    public RecipeWrapper setFuelCost(int cost) {
        this.cost = cost;
        return this;
    }

    public boolean hasEmptyList() {
        return this.hasEmptyList;
    }

    public Object[] getInputTable() {
        return input;
    }

    public IRecipe getRecipe() {
        return recipe;
    }

    public int getTotalCookTime() {
        return time;
    }

    public int getFuelCost() {
        return cost;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return recipe.matches(inv, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return recipe.getCraftingResult(inv);
    }

    @Override
    public int getRecipeSize() {
        return recipe.getRecipeSize();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return recipe.getRecipeOutput();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return recipe.getRemainingItems(inv);
    }
}
