package ruby.bamboo.api.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeWrapper implements IRecipe {

    private IRecipe recipe;
    private int time;
    private int cost;

    public RecipeWrapper(IRecipe recipe) {
        this.recipe = recipe;
        this.time = 200;
        this.cost = 200;
    }

    public RecipeWrapper setTotalCookTime(int time) {
        this.time = time;
        return this;
    }

    public RecipeWrapper setFuelCost(int cost) {
        this.cost = cost;
        return this;
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
