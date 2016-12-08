package ruby.bamboo.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ruby.bamboo.api.crafting.RecipeWrapper;

public class CookingManager {
    private static ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();
    private static final CookingManager instance = new CookingManager();
    private Container dummyContainer = new Container() {
        @Override
        public boolean canInteractWith(EntityPlayer var1) {
            return false;
        }
    };
    private InventoryCrafting crafting = new InventoryCrafting(dummyContainer, 3, 3);

    private CookingManager() {}

    public static void addRecipe(IRecipe recipe) {
        CookingManager.getInstance().getRecipeList().add(recipe);
    }

    public List<IRecipe> getRecipeList() {
        return recipes;
    }

    public static CookingManager getInstance() {
        return instance;
    }

    public RecipeEntry findMatchingRecipe(ItemStack[] itemStack, World par2World) {
        for (int i = 0; i < 9; i++) {
            crafting.setInventorySlotContents(i, itemStack[i]);
        }
        return findMatchingRecipe(crafting, par2World);
    }

    public RecipeEntry findMatchingRecipe(InventoryCrafting par1InventoryCrafting, World par2World) {
        int itemCount = 0;
        ItemStack is = null;

        for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); i++) {
            ItemStack is2 = par1InventoryCrafting.getStackInSlot(i);
            if (is2 != null) {
                if (itemCount == 0) {
                    is = is2;
                }
                itemCount++;
            }
        }

        //単一アイテムの場合、バニラ竈互換の精錬レシピをまずチェック、
        if (itemCount == 1) {
            is = FurnaceRecipes.instance().getSmeltingResult(is);
            if (is != null) {
                return new RecipeEntry(is.copy());
            }
        }

        //登録レシピチェック
        for (IRecipe irecipe : CookingManager.recipes) {
            if (irecipe.matches(par1InventoryCrafting, par2World)) {
                return new RecipeEntry(irecipe.getCraftingResult(par1InventoryCrafting), irecipe);
            }
        }

        return null;
    }

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

    public static class RecipeEntry {
        private ItemStack is;
        private int fuel;
        private int cooktime;
        private IRecipe irecipe;

        public RecipeEntry(ItemStack is) {
            this.is = is;
        }

        public RecipeEntry(ItemStack is, IRecipe irecipe) {
            this.is = is;
            this.irecipe = irecipe;
        }

        public ItemStack getItemStack() {
            return is;
        }

        public int getFuelCost() {
            if (irecipe instanceof RecipeWrapper) {
                return ((RecipeWrapper) irecipe).getFuelCost();
            }
            return 200;
        }

        public int getTotalCookTime() {
            if (irecipe instanceof RecipeWrapper) {
                return ((RecipeWrapper) irecipe).getTotalCookTime();
            }
            return 200;
        }
    }
}
