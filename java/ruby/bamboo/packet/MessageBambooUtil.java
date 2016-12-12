package ruby.bamboo.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBambooUtil implements IMessage {

    public byte data;

    public MessageBambooUtil() {}

    public MessageBambooUtil(byte par1) {
        this.data = par1;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.data = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.data);
    }

    public static class MessageBambooUtilHandler implements IMessageHandler<MessageBambooUtil, IMessage> {

        @Override
        public IMessage onMessage(MessageBambooUtil message, MessageContext ctx) {
            ItemStack is = ctx.getServerHandler().playerEntity.getHeldItemMainhand();
            if (is != null && is.getItem() instanceof IMessagelistener) {
                ((IMessagelistener) is.getItem()).call(ctx.getServerHandler().playerEntity, is, message.data);
            }
            return null;
        }
    }

    public interface IMessagelistener {
        public void call(EntityPlayer playerEntity, ItemStack is, byte data);
    }
}
