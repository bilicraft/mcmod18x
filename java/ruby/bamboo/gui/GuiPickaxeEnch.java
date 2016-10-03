package ruby.bamboo.gui;

import static ruby.bamboo.enchant.EnchantConstants.*;

import java.io.IOException;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import ruby.bamboo.core.Constants;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.enchant.Entry;
import ruby.bamboo.enchant.IBambooEnchantable;
import ruby.bamboo.packet.MessagePickaxe;

public class GuiPickaxeEnch extends GuiPickaxeBase {
    EntityPlayer player;
    private int page = 0;
    private int maxPages = 1;
    private static final ResourceLocation RESORCE = new ResourceLocation(Constants.RESOURCED_DOMAIN + "textures/guis/bamboopickaxe_ench.png");
    private ItemStack pickAxe;
    private ImmutableList<Entry> entryList;
    private ImmutableList<NBTTagCompound> nbtList;

    public GuiPickaxeEnch(EntityPlayer player, ItemStack stack) {
        super(player, new ContainerPickaxeEnch(player.inventory, stack));
        this.player = player;
        this.pickAxe = stack;
        nbtList = ImmutableList.<NBTTagCompound> builder().addAll(IBambooEnchantable.getSpenchList(stack)).build();
        entryList = ImmutableList.<Entry> builder().addAll(nbtList.stream().map(e -> new Entry(e.getByte(ENCHANT_ID), e.getShort(ENCHANT_LV))).collect(Collectors.toList())).build();
        maxPages = (int) (Math.ceil(entryList.size() / 3D)) - 1;
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
    }

    @Override
    public void setTabs() {
        tabs = new Tab[3];
        tabs[0] = new Tab(0, false);
        tabs[1] = new Tab(1, false);
        tabs[2] = new Tab(2, true);
        tabs[0].stack = new ItemStack(Items.EXPERIENCE_BOTTLE);
        tabs[1].stack = new ItemStack(Items.NAME_TAG);
        tabs[2].stack = new ItemStack(Items.BOOK);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        buttonList.add(new GuiButton(101, guiLeft + xSize - 38, guiTop + 72, 10, 10, "<"));
        buttonList.add(new GuiButton(102, guiLeft + xSize - 18, guiTop + 72, 10, 10, ">"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 101) {
            page = Math.max(page - 1, 0);
        } else if (button.id == 102) {
            page = Math.min(page + 1, maxPages);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);
        if (IBambooEnchantable.canLevelUp(pickAxe)) {
            this.fontRendererObj.drawString("Page " + (page + 1) + " / " + (maxPages + 1), 130, 4, 4210752);
        }
        int k = 26;
        int l = 19;
        // マスク
        // えんちゃ
        int fontPosX = k;
        int fontPosY = l;
        String str;
        Entry entry;
        for (int i = 0; i < 3; i++) {
            if (i + (page * 3) < entryList.size()) {
                entry = entryList.get(i + (page * 3));
                str = entry.getPrefix() + "§l" + entry.getName(pickAxe) + " " + (entry.getBase().getMaxLevel() > 1 ? IBambooEnchantable.getDisplayEnchantStrength(entry.getAmount()) : "");
                int color = entry.getClolr();
                this.fontRendererObj.drawString(str, fontPosX, fontPosY + i * 19, color, true);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(RESORCE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        // マスク
        // えんちゃ
        int fontPosX = k + 5;
        int fontPosY = l + 13;
        String str;
        Entry entry;
        for (int i = 0; i < 3; i++) {
            if (i + (page * 3) < entryList.size()) {
                entry = entryList.get(i + (page * 3));
                this.drawTexturedModalRect(fontPosX, fontPosY + i * 19, 0, this.ySize + 57, 20, 20);
                if (nbtList.get(i + (page * 3)).getBoolean(ENCHANT_ENABLE)) {
                    this.drawTexturedModalRect(fontPosX, fontPosY + i * 19, 20, this.ySize + 57, 20, 20);
                }
            }
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int k = (this.width - this.xSize) / 2 + 25;
        int l = (this.height - this.ySize) / 2 + 13;
        // 横軸
        if (k < mouseX && mouseX < k + 140) {
            //縦軸
            if (l < mouseY && mouseY < l + 57) {
                byte data = (byte) (((mouseY - l) / 19) + (page * 3));
                if (data < nbtList.size()) {
                    nbtList.get(data).setBoolean(ENCHANT_ENABLE, !nbtList.get(data).getBoolean(ENCHANT_ENABLE));
                }
                PacketDispatcher.sendToServer(new MessagePickaxe((byte) GuiHandler.GUI_PICKAXE_ENCH).setSubData(data));
            }
        }
    }

    @Override
    public ResourceLocation getResource() {
        return RESORCE;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
