package ruby.bamboo.api.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ruby.bamboo.api.Constants;

public class CookingRegistory {
    public static void addShapedRecipe(Block out, Object... recipe) {
        addRecipe(new ShapedOreRecipe(out, recipe));
    }

    public static void addShapedRecipe(Item out, Object... recipe) {
        addRecipe(new ShapedOreRecipe(out, recipe));
    }

    public static void addShapedRecipe(ItemStack out, Object... recipe) {
        addRecipe(new ShapedOreRecipe(out, recipe));
    }

    public static void addShapelessRecipe(ItemStack out, Object... recipe) {
        addRecipe(new ShapelessOreRecipe(out, recipe));
    }

    public static void addRecipe(IRecipe recipe) {
        if (Loader.isModLoaded(Constants.MODID)) {
            ruby.bamboo.crafting.CookingManager.addRecipe(recipe);
        }
    }
}
