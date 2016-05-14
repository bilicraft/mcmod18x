package ruby.bamboo.item;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.Constants;
import ruby.bamboo.core.KeyBindFactory.IBambooKeylistener;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.item.arrow.IBambooArrow;
import ruby.bamboo.item.itemblock.IEnumTex;
import ruby.bamboo.item.itemblock.ISubTexture;
import ruby.bamboo.packet.ChangeAmmo;
import ruby.bamboo.util.ItemStackHelper;
import ruby.bamboo.util.ItemStackHelper.HashedStack;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class BambooBow extends ItemBow
        implements ISubTexture, IBambooKeylistener {
    public final static String TAG_AMMO = "ammo";
    public final static String AMMO_SLOT = "slot";
    public final static String ICON_NAME = Constants.RESOURCED_DOMAIN + "bamboobow";
    public final static String[] ICON_PULL_NAMES = new String[4];

    static {
        for (int i = 0; i < ICON_PULL_NAMES.length; i++) {
            ICON_PULL_NAMES[i] = ICON_NAME + "_pull_" + i;
        }
    }

    public BambooBow() {
        super();
        this.setMaxDamage(400);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
        int chargeFrame = this.getMaxItemUseDuration(par1ItemStack) - par4;
        ArrowLooseEvent event = new ArrowLooseEvent(par3EntityPlayer, par1ItemStack, chargeFrame);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }

        IInventory inv = par3EntityPlayer.inventory;
        chargeFrame = event.charge;
        boolean isNoResource = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

        if (this.hasInventoryBambooArrow(inv)) {
            float power = chargeFrame / 10.0F;
            power = (power * power + power * 2.0F) / 3.0F;

            if (power < 0.1D) {
                return;
            }

            if (power > 1.0F) {
                power = 1.0F;
            }

            int slotNum = getSelectedInventorySlotContainItem(inv, par1ItemStack);
            ItemStack stack = inv.getStackInSlot(slotNum);
            IBambooArrow arrow = (IBambooArrow) stack.getItem();

            arrow.execute(par2World, par1ItemStack, stack, power, chargeFrame, par3EntityPlayer);

            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + power * 0.5F);
            //ItemStackHelper.decrStackSize(inv, stack, 1);
        }

        if (!isNoResource) {
            par1ItemStack.damageItem(1, par3EntityPlayer);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        ArrowNockEvent event = new ArrowNockEvent(par3EntityPlayer, par1ItemStack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return event.result;
        }
        InventoryPlayer inv = par3EntityPlayer.inventory;

        if (par3EntityPlayer.capabilities.isCreativeMode || this.hasInventoryBambooArrow(inv) || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0) {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

    private boolean hasInventoryBambooArrow(IInventory inv) {
        return this.getInventorySlotContainItem(inv) >= 0;
    }

    private int getInventorySlotContainItem(IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() instanceof IBambooArrow) {
                return i;
            }
        }

        return -1;
    }

    private int getSelectedInventorySlotContainItem(IInventory inv, ItemStack stack) {
        HashedStack[] arrows = getArrowTypes(inv).toArray(new HashedStack[0]);
        int arrowSlotNum = getArrowSlot(stack);
        if (arrows.length > arrowSlotNum) {
            return ItemStackHelper.getSlotNum(inv, arrows[arrowSlotNum].getItemStack());
        } else {
            // こちらには基本来ないはず…
            return 0;
        }
    }

    /**
     * 矢の合計取得
     */
    private Map<HashedStack, Integer> getArrowMap(InventoryPlayer inv) {
        Map<HashedStack, Integer> map = new LinkedHashMap<>();

        for (ItemStack stack : inv.mainInventory) {
            if (stack != null && stack.getItem() instanceof IBambooArrow) {
                Item item = stack.getItem();
                int size = stack.stackSize;
                HashedStack hashStack = new HashedStack(stack);
                if (map.containsKey(hashStack)) {
                    size += map.get(hashStack);
                }
                map.put(hashStack, size);
            }
        }

        return map;
    }

    // 残弾表示系
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderTip(RenderGameOverlayEvent.Text e) {
        EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == this) {
            Map<HashedStack, Integer> map = getArrowMap(player.inventory);
            GuiIngame gui = FMLClientHandler.instance().getClient().ingameGUI;
            // 最長名前取得
            int maxWidth = 0;
            for (HashedStack stack : map.keySet()) {
                int width = gui.getFontRenderer().getStringWidth(stack.item.getItemStackDisplayName(stack.getItemStack()) + ":" + map.get(stack));
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }

            byte slotPos = getArrowSlot(player.getCurrentEquippedItem());
            byte nuwPos = 0;
            for (HashedStack stack : map.keySet()) {
                draw(e, maxWidth, stack.item.getItemStackDisplayName(stack.getItemStack()) + ":" + map.get(stack), slotPos == nuwPos++ ? 0xE06060 : 0xE0E0E0);
            }
        }
    }

    private void draw(RenderGameOverlayEvent.Text e, int startLeftpos, String msg, int color) {
        GuiIngame gui = FMLClientHandler.instance().getClient().ingameGUI;
        int top = 2 + gui.getFontRenderer().FONT_HEIGHT * e.left.size();

        int w = gui.getFontRenderer().getStringWidth(msg);
        int left = e.resolution.getScaledWidth() - 2 - startLeftpos;
        gui.drawRect(1, top - 1, startLeftpos + 1, top + gui.getFontRenderer().FONT_HEIGHT - 1, -0x6FAFAFB0);
        gui.getFontRenderer().drawString(msg, 1, top, color);
        // 描画ポジション予約
        e.left.add(null);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (par5 && par3Entity instanceof EntityPlayer && !par2World.isRemote) {
            EntityPlayer player = (EntityPlayer) par3Entity;
            byte types = (byte) getArrowTypes(player.inventory).size();
            // リセット処理
            if (getArrowSlot(par1ItemStack) >= types && types > 0) {
                par1ItemStack.getSubCompound(TAG_AMMO, true).setByte(AMMO_SLOT, --types);
            }
        }
    }

    // 弓を引く時のアニメーション系
    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        ModelResourceLocation modelresourcelocation = new ModelResourceLocation(ICON_PULL_NAMES[0], "inventory");
        InventoryPlayer inv = player.inventory;
        if (stack.getItem() == this && player.getItemInUse() != null) {
            int chargeFrame = this.getMaxItemUseDuration(stack) - useRemaining;
            if (this.hasInventoryBambooArrow(inv)) {
                IBambooArrow arrow = (IBambooArrow) inv.getStackInSlot(getSelectedInventorySlotContainItem(inv, stack)).getItem();
                modelresourcelocation = arrow.getBowModel(chargeFrame);
            } else {
                if (chargeFrame >= 40) {
                    modelresourcelocation = new ModelResourceLocation(ICON_PULL_NAMES[3], "inventory");
                } else if (chargeFrame > 25) {
                    modelresourcelocation = new ModelResourceLocation(ICON_PULL_NAMES[2], "inventory");
                } else if (chargeFrame > 0) {
                    modelresourcelocation = new ModelResourceLocation(ICON_PULL_NAMES[1], "inventory");
                }
            }
        }
        return modelresourcelocation;
    }

    @Override
    public IEnumTex[] getName() {
        IEnumTex[] ret = new IEnumTex[ICON_PULL_NAMES.length];
        for (int i = 0; i < ret.length; i++) {
            final int id = i;
            ret[i] = new IEnumTex() {

                @Override
                public int getId() {
                    return id;
                }

                @Override
                public String getJsonName() {
                    return ICON_PULL_NAMES[id];
                }

            };
        }
        return ret;
    }

    // 汎用キーによる弾の切り替え
    @SideOnly(Side.CLIENT)
    @Override
    public void exec(KeyBinding key) {
        EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == this) {
            if (key.isPressed()) {
                ItemStack is = player.getCurrentEquippedItem();
                byte slotNum = getArrowSlot(is);
                int invArrowTypes = getArrowTypes(player.inventory).size();
                if (++slotNum >= invArrowTypes) {
                    slotNum = 0;
                }
                PacketDispatcher.sendToServer(new ChangeAmmo(slotNum));
            }
        }
    }

    /**
     * NBTより現在のスロットナンバー取得
     */
    public byte getArrowSlot(ItemStack is) {
        if (!is.hasTagCompound()) {
            is.getSubCompound(TAG_AMMO, true).setByte(AMMO_SLOT, (byte) 0);
        }
        return is.getSubCompound(TAG_AMMO, true).getByte(AMMO_SLOT);
    }

    /**
     * 矢の種類
     */
    public Set<HashedStack> getArrowTypes(IInventory inv) {
        Set<HashedStack> stackSet = new LinkedHashSet<>();
        ItemStack stack;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            stack = inv.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof IBambooArrow) {
                HashedStack hashStack = new HashedStack(stack);
                stackSet.add(hashStack);
            }
        }
        return stackSet;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

}
