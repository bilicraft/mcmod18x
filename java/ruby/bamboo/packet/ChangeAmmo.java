package ruby.bamboo.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ruby.bamboo.item.BambooBow;

public class ChangeAmmo implements IMessage {

    public byte data;

    public ChangeAmmo() {
    }

    public ChangeAmmo(byte par1) {
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

    public static class ChangeAmmoHandler
            implements IMessageHandler<ChangeAmmo, IMessage> {

        @Override
        public IMessage onMessage(ChangeAmmo message, MessageContext ctx) {
            ItemStack is = ctx.getServerHandler().playerEntity.getCurrentEquippedItem();
            if (is != null && is.getItem() instanceof BambooBow) {
                is.getSubCompound(BambooBow.TAG_AMMO, true).setByte(BambooBow.AMMO_SLOT, message.data);
            }
            return null;
        }
    }
}
