package ruby.bamboo.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import ruby.bamboo.api.Constants;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.packet.MessagePickaxe;

public abstract class GuiPickaxeBase extends GuiContainer {

    public static final ResourceLocation RESORCE = new ResourceLocation(Constants.RESOURCED_DOMAIN + "textures/guis/tab.png");
    Tab[] tabs;
    EntityPlayer player;

    public GuiPickaxeBase(EntityPlayer player, Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.setTabs();
        this.player = player;
    }

    public abstract void setTabs();

    public abstract ResourceLocation getResource();

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        if (player.getHeldItemMainhand() != null) {
            String s = player.getHeldItemMainhand().getDisplayName();
            this.fontRendererObj.drawString(s, 10, 4, 4210752);
            this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        for (int i = 0; i < tabs.length; i++) {
            this.mc.getTextureManager().bindTexture(RESORCE);
            tabs[i].draw(this, k + this.xSize, l + i * 22);
        }
    }

    @Override
    protected boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        if (pointX > (k + this.xSize) && (k + this.xSize + 23) > pointX && l < pointY && pointY < l + 69) {
            if (pointY < l + 23) {
                tabs[0].addAnimetion(0.12F);
            } else if (pointY < l + 46) {
                tabs[1].addAnimetion(0.12F);
            } else if (pointY < l + 69) {
                tabs[2].addAnimetion(0.12F);
            }
        }
        for (Tab t : tabs) {
            t.addAnimetion(-0.1F);
        }
        return super.isPointInRegion(left, top, right, bottom, pointX, pointY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        if (mouseX > (k + this.xSize) && (k + this.xSize + 23) > mouseX && l < mouseY && mouseY < l + 69) {
            EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
            if (mouseY < l + 23) {
                onTabClicked(GuiHandler.GUI_PICKAXE_LV);
            } else if (mouseY < l + 46) {
                onTabClicked(GuiHandler.GUI_PICKAXE_NAME);
            } else if (mouseY < l + 69) {
                onTabClicked(GuiHandler.GUI_PICKAXE_ENCH);
            }
        }
    }

    public void onTabClicked(int tabId) {
        switch (tabId) {
            case GuiHandler.GUI_PICKAXE_LV:
                GuiHandler.openGui(player, GuiHandler.GUI_PICKAXE_LV);
                PacketDispatcher.sendToServer(new MessagePickaxe((byte) GuiHandler.GUI_PICKAXE_LV));
                break;
            case GuiHandler.GUI_PICKAXE_NAME:
                GuiHandler.openGui(player, GuiHandler.GUI_PICKAXE_NAME);
                PacketDispatcher.sendToServer(new MessagePickaxe((byte) GuiHandler.GUI_PICKAXE_NAME));
                break;
            case GuiHandler.GUI_PICKAXE_ENCH:
                GuiHandler.openGui(player, GuiHandler.GUI_PICKAXE_ENCH);
                PacketDispatcher.sendToServer(new MessagePickaxe((byte) GuiHandler.GUI_PICKAXE_ENCH));
                break;
        }
    }

    static class Tab {
        public int id;
        public int posX;
        public int posY;
        public int sizeX;
        public int sizeY;
        public float animetionX;
        public ItemStack stack;

        Tab(int id, boolean isActive) {
            this.id = id;
            this.posX = 0;
            this.posY = isActive ? 0 : 22;
            this.sizeX = 32;
            this.sizeY = 23;
        }

        public void draw(GuiContainer gui, int drawPosX, int drawPosY) {
            gui.drawTexturedModalRect(drawPosX + animetionX - 10, drawPosY, posX, posY, sizeX, sizeY);
            if (stack != null) {
                gui.mc.getRenderItem().renderItemIntoGUI(stack, (int) (drawPosX + Math.ceil(animetionX)) + 2, drawPosY + 3);
            }
        }

        public void addAnimetion(float num) {
            if (num > 0) {
                if (animetionX < 5) {
                    animetionX += num;
                } else {
                    animetionX = 5;
                }
            } else {
                if (animetionX > 0) {
                    animetionX += num;
                } else {
                    animetionX = 0;
                }
            }
        }
    }
}
