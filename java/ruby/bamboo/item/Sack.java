package ruby.bamboo.item;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.BambooCore;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.core.client.KeyBindFactory.IItemUtilKeylistener;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.gui.GuiHandler;
import ruby.bamboo.packet.MessageBambooUtil;
import ruby.bamboo.packet.MessageBambooUtil.IMessagelistener;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class Sack extends Item implements IItemUtilKeylistener, IMessagelistener {
    // private static ItemStack backkup;
    private Field remainingHighlightTicks;

    public Sack() {
        super();
        setHasSubtypes(true);
        setMaxDamage(1025);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if (itemStack.getTagCompound() == null) {
            player.openGui(BambooCore.instance, GuiHandler.GUI_SACK, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
        }

        short count = itemStack.getTagCompound().getShort("count");
        String type = itemStack.getTagCompound().getString("type");
        short meta = itemStack.getTagCompound().getShort("meta");

        if (!isStorage(getItem(type))) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
        }

        RayTraceResult movingobjectposition = this.rayTrace(world, player, player.isSneaking());
        if (movingobjectposition == null) {
            ItemStack[] is = player.inventory.mainInventory;

            for (int i = 0; i < is.length; i++) {
                if (is[i] == null) {
                    continue;
                }

                if (is[i].getItem() == getItem(type) && is[i].getItemDamage() == meta) {
                    if ((count + is[i].stackSize) < getMaxDamage()) {
                        count += is[i].stackSize;
                        is[i] = null;
                    } else {
                        is[i].stackSize -= (getMaxDamage() - count - 1);
                        count = (short) (getMaxDamage() - 1);
                    }
                }
            }

            itemStack.getTagCompound().setShort("count", count);
            itemStack.setItemDamage(getMaxDamage() - count);
        } else {
            int stacksize = getItem(type).onItemRightClick(new ItemStack(getItem(type), count, meta), world, player, hand).getResult().stackSize;

            if (stacksize < count) {
                itemStack.setItemDamage(itemStack.getItemDamage() + count + stacksize);
                count -= stacksize;
                itemStack.getTagCompound().setShort("count", count);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
    }

    private Item getItem(String str) {
        return (Item) Item.REGISTRY.getObject(new ResourceLocation(str));
    }

    private boolean isStorage(Item item) {
        return item instanceof ItemBlock ? true : item instanceof ItemSeeds ? true : item instanceof ItemSeedFood ? true : false;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (stack.getTagCompound() == null) {
            return EnumActionResult.FAIL;
        }

        short count = stack.getTagCompound().getShort("count");
        String type = stack.getTagCompound().getString("type");
        short meta = stack.getTagCompound().getShort("meta");

        if (!isStorage(getItem(type))) {
            return EnumActionResult.FAIL;
        }

        if (!world.canMineBlockBody(player, pos)) {
            return EnumActionResult.FAIL;
        }

        if (count != 0) {
            ItemStack is = new ItemStack(getItem(type), 1, meta);
            if (Block.getBlockFromItem(getItem(type)) != Blocks.AIR) {
                if (getItem(type) instanceof ItemBlock) {
                    if (getItem(type).onItemUse(is, player, world, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
                        stack.setItemDamage(stack.getItemDamage() + 1);
                        count--;
                        stack.getTagCompound().setShort("count", count);
                        player.swingArm(hand);
                    }
                }
            }
            if (getItem(type) instanceof ItemSeeds || getItem(type) instanceof ItemSeedFood) {
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (getItem(type).onItemUse(is, player, world, pos.add(i, 0, j), hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
                            stack.setItemDamage(stack.getItemDamage() + 1);
                            count--;
                            stack.getTagCompound().setShort("count", count);
                            player.swingArm(hand);

                            if (count < 1) {
                                return EnumActionResult.SUCCESS;
                            }
                        }
                    }
                }
            }
        }
        return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    public void release(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par1ItemStack.getTagCompound() != null && !par2World.isRemote) {
            short count = par1ItemStack.getTagCompound().getShort("count");
            String type = par1ItemStack.getTagCompound().getString("type");
            short meta = par1ItemStack.getTagCompound().getShort("meta");
            double x = par3EntityPlayer.posX;
            double y = par3EntityPlayer.posY;
            double z = par3EntityPlayer.posZ;

            if (count > 0) {
                par2World.spawnEntityInWorld(new EntityItem(par2World, x, y, z, new ItemStack(getItem(type), count, meta)));
                count = 0;
            }

            clearContainer(par1ItemStack);
            par1ItemStack.setItemDamage(0);
        }
    }

    private void returnItem(EntityPlayer entity, ItemStack is) {
        if (!entity.inventory.addItemStackToInventory(is)) {
            entity.entityDropItem(is, 0.5F);
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderToolHighlight() {
        // リフレクションじゃないほうがいいかな～？
        try {
            if (remainingHighlightTicks == null) {
                for (int i = 0; i < GuiIngame.class.getDeclaredFields().length; i++) {
                    if (GuiIngame.class.getDeclaredFields()[i].getType() == ItemStack.class) {
                        remainingHighlightTicks = GuiIngame.class.getDeclaredFields()[i - 1];
                    }
                }
                remainingHighlightTicks.setAccessible(true);
            }
            remainingHighlightTicks.setInt(FMLClientHandler.instance().getClient().ingameGUI, 40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        String name = super.getItemStackDisplayName(par1ItemStack);
        if (par1ItemStack.getTagCompound() != null && getItem(par1ItemStack.getTagCompound().getString("type")) != null) {
            name += (":" + getItem(par1ItemStack.getTagCompound().getString("type"))
                    .getItemStackDisplayName(new ItemStack(getItem(par1ItemStack.getTagCompound().getString("type")), 1, par1ItemStack.getTagCompound().getShort("meta"))) + ":" + par1ItemStack.getTagCompound().getShort("count")).trim();
        }

        return name;
    }

    @Override
    public void onUpdate(ItemStack stack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (par5 && par2World.isRemote) {
            renderToolHighlight();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) {
        return par1ItemStack.getTagCompound() != null;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            return null;
        }
        short count = itemStack.getTagCompound().getShort("count");
        String type = itemStack.getTagCompound().getString("type");
        short meta = itemStack.getTagCompound().getShort("meta");
        return new ItemStack(getItem(type), count, meta);
    }

    public void setContainerItem(ItemStack target,ItemStack itemStack) {
        if(itemStack==null){
            return;
        }
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        short count = itemStack.getTagCompound().getShort("count");
        String type = itemStack.getTagCompound().getString("type");
        short meta = itemStack.getTagCompound().getShort("meta");
    }

    public void clearContainer(ItemStack itemStack) {
        itemStack.setTagCompound(null);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return (double)stack.getItemDamage() / (double)stack.getMaxDamage();
    }

    @Override
    public void exec(KeyBinding key) {
        EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        if (key.isPressed()) {
//            player.openGui(BambooCore.instance, GuiHandler.GUI_SACK, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
//            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == this) {
//                player.setActiveHand(EnumHand.MAIN_HAND);
//            } else {
//                player.setActiveHand(EnumHand.OFF_HAND);
//            }
            PacketDispatcher.sendToServer(new MessageBambooUtil());
        }
    }

    @Override
    public void call(EntityPlayer player, ItemStack is, byte data) {
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == this) {
            player.setActiveHand(EnumHand.MAIN_HAND);
        } else {
            player.setActiveHand(EnumHand.OFF_HAND);
        }
//        player.openGui(BambooCore.instance, GuiHandler.GUI_SACK, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        this.release(is, player.getEntityWorld(), player);
    }
}
