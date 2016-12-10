package ruby.bamboo.plugin.jei;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.core.init.BambooData.JEIIgnore;
import ruby.bamboo.crafting.CookingManager;
import ruby.bamboo.crafting.GrindManager;
import ruby.bamboo.util.ItemStackHelper.HashedStack;

@JEIPlugin
public class BambooJEIPlugin implements IModPlugin {

    private IJeiHelpers helper;

    @Override
    public void register(IModRegistry registry) {
        helper = registry.getJeiHelpers();
        registry.addRecipeCategories(new GrindRecipeCategory(helper.getGuiHelper()), new CookingRecipeCategory(helper.getGuiHelper()));
        registry.addRecipeHandlers(new GrindRecipeHandler(), new CookingRecipeHandler());

        registry.addRecipes(Lists.newArrayList(GrindManager.getRecipeList()));
        registry.addRecipes(CookingManager.getInstance().getRecipeList().stream().filter(rec -> !rec.hasEmptyList()).collect(Collectors.toList()));

        registry.addRecipeCategoryCraftingItem(new ItemStack(BambooBlocks.CAMPFIRE), CookingRecipeCategory.UID);

        registry.addRecipeCategoryCraftingItem(new ItemStack(BambooBlocks.MILLSTONE), GrindRecipeCategory.UID);

        Set<HashedStack> blackList = Sets.newHashSet();

        blackList.addAll(StreamSupport.stream(Item.REGISTRY.spliterator(), false).filter(i -> i.getClass().isAnnotationPresent(JEIIgnore.class)).map(i -> new HashedStack(i)).collect(Collectors.toList()));
        blackList.addAll(StreamSupport.stream(Block.REGISTRY.spliterator(), false).filter(i -> i.getClass().isAnnotationPresent(JEIIgnore.class)).map(i -> new HashedStack(i)).collect(Collectors.toList()));

        for (HashedStack is : blackList) {
            helper.getItemBlacklist().addItemToBlacklist(is.getItemStack());
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}

}
