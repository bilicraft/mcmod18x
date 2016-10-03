package ruby.bamboo.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.item.BambooPickaxe;

public abstract class BaseContainer extends Container{

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getHeldItemMainhand() != null && playerIn.getHeldItemMainhand().getItem() == DataManager.getItem(BambooPickaxe.class);
    }

}
