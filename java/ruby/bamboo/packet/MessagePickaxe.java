package ruby.bamboo.packet;

import static ruby.bamboo.enchant.EnchantConstants.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import com.google.common.primitives.Bytes;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ruby.bamboo.enchant.EnchantFactory;
import ruby.bamboo.enchant.IBambooEnchantable;
import ruby.bamboo.gui.ContainerPickaxeName;
import ruby.bamboo.gui.GuiHandler;

public class MessagePickaxe implements IMessage {
    //byte[]{GuiID(,empty,subdata...)}
    public byte[] data;
    private final byte[] empty = new byte[] { 0 };

    public MessagePickaxe() {
    }

    public MessagePickaxe(byte guiID) {
        this.data = new byte[] { guiID };
    }

    public MessagePickaxe setSubData(String data) {
        try {
            // ""パータンの時getBytesがbyte[0]になるため、判別用にスペーサーが必要。
            this.data = Bytes.concat(this.data, empty, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        return this;
    }

    public MessagePickaxe setSubData(byte data) {
        // Stringの仕様に合わせてスペーサとしてbyte[]{0}を入れている。
        this.data = Bytes.concat(this.data, empty, new byte[] { data });
        return this;
    }

    public boolean hasSubdata() {
        return data.length > 1;
    }

    public byte getGuiID() {
        return data[0];
    }

    private byte[] getSubData() {
        return hasSubdata() ? Arrays.copyOfRange(data, 2, data.length) : empty;
    }

    public byte getSubDataToByte() {
        return getSubData()[0];
    }

    public String getSubDataToString() {
        try {
            return new String(getSubData(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.data = new byte[buf.readableBytes()];
        buf.readBytes(data);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBytes(data);
    }

    public static class MessagePickaxeHandler implements IMessageHandler<MessagePickaxe, IMessage> {

        @Override
        public IMessage onMessage(MessagePickaxe message, MessageContext ctx) {
            if (!message.hasSubdata()) {
                GuiHandler.openGui(ctx.getServerHandler().playerEntity, message.getGuiID());
            } else {
                switch (message.getGuiID()) {
                    case GuiHandler.GUI_PICKAXE_LV:
                        this.onGuiLvSub(ctx.getServerHandler().playerEntity, message.getSubDataToByte());
                        break;
                    case GuiHandler.GUI_PICKAXE_NAME:
                        this.onGuiNameSub(ctx.getServerHandler().playerEntity, message.getSubDataToString());
                        break;
                    case GuiHandler.GUI_PICKAXE_ENCH:
                        this.onGuiEnchSub(ctx.getServerHandler().playerEntity, message.getSubDataToByte());
                        break;
                }
            }
            return null;
        }

        private void onGuiLvSub(EntityPlayerMP playerEntity, byte data) {
            EnchantFactory.setEnchant(playerEntity.getCurrentEquippedItem(), data);
        }

        private void onGuiNameSub(EntityPlayerMP playerEntity, String name) {
            ContainerPickaxeName container = (ContainerPickaxeName) playerEntity.openContainer;
            container.updateItemName(name);
        }

        private void onGuiEnchSub(EntityPlayerMP playerEntity, byte data) {
            ItemStack stack = playerEntity.getCurrentEquippedItem();
            List<NBTTagCompound> nbtList = IBambooEnchantable.getSpenchList(stack);
            if (data < nbtList.size()) {
                nbtList.get(data).setBoolean(ENCHANT_ENABLE, !nbtList.get(data).getBoolean(ENCHANT_ENABLE));
            }
        }
    }

    public interface IMessagelistener {
        public void call(EntityPlayer playerEntity, ItemStack is, byte data);
    }

}