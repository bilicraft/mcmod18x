package ruby.bamboo.gui;

import static ruby.bamboo.enchant.EnchantConstants.*;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ruby.bamboo.core.Constants;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.packet.MessagePickaxe;

public class GuiPickaxeName extends GuiPickaxeBase {
    EntityPlayer player;
    private GuiTextField nameField;
    private ContainerPickaxeName container;
    private static final ResourceLocation RESORCE = new ResourceLocation(Constants.RESOURCED_DOMAIN + "textures/guis/bamboopickaxe_rename.png");
    private ItemStack pickAxe;
    private GuiButton button;

    public GuiPickaxeName(EntityPlayer player, ItemStack stack) {
        super(player, new ContainerPickaxeName(player, stack));
        this.player = player;
        this.pickAxe = stack;

        this.container = (ContainerPickaxeName) this.inventorySlots;

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
    }

    @Override
    public void setTabs() {
        tabs = new Tab[3];
        tabs[0] = new Tab(0, false);
        tabs[1] = new Tab(1, true);
        tabs[2] = new Tab(2, false);
        tabs[0].stack = new ItemStack(Items.experience_bottle);
        tabs[1].stack = new ItemStack(Items.name_tag);
        tabs[2].stack = new ItemStack(Items.book);
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.buttonList.clear();
        button = new GuiButton(101, guiLeft + xSize - 38, guiTop + 45, 30, 18, "OK");
        buttonList.add(button);
        this.nameField = new GuiTextField(0, this.fontRendererObj, i + 62, j + 24, 103, 12);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(30);
        this.onButtonEnable();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 101) {
            renameItem();
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
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        //        this.fontRendererObj.drawString(I18n.format("container.repair", new Object[0]), 60, 6, 4210752);

        int i = 0x22EE22;
        boolean flag = true;
        String s = I18n.format("RENAME COST", new Object[] { 40 });
        s += " : ";
        s += this.getCost();

        if (flag) {
            int baseX = (this.width - this.xSize) / 2 + 10;
            int baseY = 50;
            int color = 0x00FF00;
            int fixedX = baseX - 10 - this.fontRendererObj.getStringWidth(s);

            if (this.fontRendererObj.getUnicodeFlag()) {
                drawRect(fixedX - 3, baseY - 2, baseX - 7, baseY + 10, -16777216);
                drawRect(fixedX - 2, baseY - 1, baseX - 8, baseY + 9, -12895429);
            } else {
                this.fontRendererObj.drawString(s, fixedX, baseY + 1, color);
                this.fontRendererObj.drawString(s, fixedX + 1, baseY, color);
                this.fontRendererObj.drawString(s, fixedX + 1, baseY + 1, color);
            }

            this.fontRendererObj.drawString(s, fixedX, baseY, i);
        }

        GlStateManager.enableLighting();
    }

    @Override
    public ResourceLocation getResource() {
        return RESORCE;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.nameField.textboxKeyTyped(typedChar, keyCode)) {
            //this.renameItem();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void renameItem() {
        String s = this.nameField.getText();

        if (pickAxe != null && !pickAxe.hasDisplayName() && s.equals(pickAxe.getDisplayName())) {
            s = "";
        }

        this.container.updateItemName(s);
        PacketDispatcher.sendToServer(new MessagePickaxe((byte) GuiHandler.GUI_PICKAXE_NAME).setSubData(s));

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.onButtonEnable();
    }

    private void onButtonEnable() {
        if (player.experienceLevel >= getCost() || player.capabilities.isCreativeMode) {
            button.enabled = true;
        } else {
            button.enabled = false;
        }
    }

    private int getCost() {
        return this.inventorySlots.getSlot(0).getHasStack() ? 0 : COST;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

}
