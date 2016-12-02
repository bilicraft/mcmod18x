package ruby.bamboo.api.crafting.grind;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import ruby.bamboo.api.Constants;

public class GrindRegistory {
    /**
     * Grind
     *
     * @param output
     * @param input
     */
    public static void addRecipe(ItemStack output, Block input) {
        addRecipe(output, null, new ItemStack(input), 0);
    }

    /**
     * Grind
     *
     * @param output
     * @param input
     */
    public static void addRecipe(ItemStack output, Item input) {
        addRecipe(output, null, new ItemStack(input), 0);
    }

    /**
     * Grind
     *
     * @param output
     * @param bonus
     * @param input
     * @param bonusWeight (rand.nectFloat()<=bonusWeight)
     */
    public static void addRecipe(ItemStack output, ItemStack bonus, Block input, float bonusWeight) {
        addRecipe(output, bonus, new ItemStack(input), bonusWeight);
    }

    /**
     * Grind
     *
     * @param output
     * @param bonus
     * @param input
     * @param bonusWeight (rand.nectFloat()<=bonusWeight)
     */
    public static void addRecipe(ItemStack output, ItemStack bonus, Item input, float bonusWeight) {
        addRecipe(output, bonus, new ItemStack(input), bonusWeight);
    }

    /**
     * Grind
     *
     * @param output
     * @param input
     */
    public static void addRecipe(ItemStack output, ItemStack input) {
        addRecipe(output, null, input, 0);
    }

    /**
     * Grind
     *
     * @param output
     * @param bonus
     * @param input
     * @param bonusWeight (rand.nectFloat()<=bonusWeight)
     */
    public static void addRecipe(ItemStack output, ItemStack bonus, ItemStack input, float bonusWeight) {
        if (Loader.isModLoaded(Constants.MODID)) {
            ruby.bamboo.crafting.GrindManager.addRecipe(output, bonus, input, bonusWeight);
        }
    }
}
