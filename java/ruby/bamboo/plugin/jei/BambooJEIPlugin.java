package ruby.bamboo.plugin.jei;

import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import ruby.bamboo.crafting.CookingManager;
import ruby.bamboo.crafting.GrindManager;

@JEIPlugin
public class BambooJEIPlugin implements IModPlugin {

    private IJeiHelpers helper;

    @Override
    public void register(IModRegistry registry) {
        helper = registry.getJeiHelpers();
        registry.addRecipeCategories(new GrindRecipeCategory(helper.getGuiHelper()), new CookingRecipeCategory(helper.getGuiHelper()));
        registry.addRecipeHandlers(new GrindRecipeHandler(), new CookingRecipeHandler());

        registry.addRecipes(Lists.newArrayList(GrindManager.getRecipeList()));
        registry.addRecipes(CookingManager.getInstance().getRecipeList().stream().filter(rec->!rec.hasEmptyList()).collect(Collectors.toList()));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}

}
