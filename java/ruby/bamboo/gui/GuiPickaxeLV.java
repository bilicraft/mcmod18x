package ruby.bamboo.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ruby.bamboo.api.Constants;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.enchant.EnchantFactory;
import ruby.bamboo.enchant.Entry;
import ruby.bamboo.enchant.IBambooEnchantable;
import ruby.bamboo.packet.MessagePickaxe;

public class GuiPickaxeLV extends GuiPickaxeBase {
    EntityPlayer player;
    private int page = 0;
    private int maxPages = 1;
    private static final ResourceLocation RESORCE = new ResourceLocation(Constants.RESOURCED_DOMAIN + "textures/guis/bamboopickaxe_level.png");
    private ItemStack pickAxe;
    private List<Entry> list;
    private boolean isLevelUp;

    public GuiPickaxeLV(EntityPlayer player, ItemStack stack) {
        super(player, new ContainerPickaxeLV(player.inventory, stack));
        this.player = player;
        this.pickAxe = stack;
        isLevelUp = IBambooEnchantable.canLevelUp(pickAxe);
        list = EnchantFactory.createViewList(pickAxe);
        maxPages = (int) (Math.ceil(list.size() / 3D)) - 1;
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
    }

    @Override
    public void setTabs() {
        tabs = new Tab[3];
        tabs[0] = new Tab(0, true);
        tabs[1] = new Tab(1, false);
        tabs[2] = new Tab(2, false);
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
        if (this.isLevelUp) {
            // えんちゃ
            int fontPosX = k + 26;
            int fontPosY = l + 19;
            String str;
            Entry entry;
            for (int i = 0; i < 3; i++) {
                if (i + (page * 3) < list.size()) {
                    entry = list.get(i + (page * 3));
                    str = entry.getPrefix() + "§l" + entry.getName(pickAxe) + " " + (entry.getBase().getMaxLevel() > 1 ? IBambooEnchantable.getDisplayEnchantStrength(entry.getAmount()) : "");
                    int color = entry.getClolr();
                    if (IBambooEnchantable.getLV(pickAxe) > entry.getCurseLv()) {
                        color = 0xAA0000;
                    }
                    this.fontRendererObj.drawString(str, fontPosX, fontPosY + i * 19, color, true);
                }
            }
        } else {
            // レベルアップ不可
            for (int i = 0; i < 3; i++) {
                this.drawTexturedModalRect(k + 24, l + 14 + i * 19, 0, 185, 144, 19);
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
                if (isLevelUp) {
                    PacketDispatcher.sendToServer(new MessagePickaxe((byte) GuiHandler.GUI_PICKAXE_LV).setSubData((byte) (((mouseY - l) / 19) + (page * 3))));
                    this.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                    isLevelUp = false;
                }
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
