package ruby.bamboo.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.entity.Wind;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class FoldingFan extends Item {

    public FoldingFan() {
        super();
        setMaxDamage(100);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        itemStack.damageItem(1, player);
        Wind entity = new Wind(world, player);
        world.spawnEntityInWorld(entity);
        return new ActionResult(EnumActionResult.PASS, itemStack);
    }
}
