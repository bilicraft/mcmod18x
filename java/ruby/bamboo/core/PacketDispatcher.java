package ruby.bamboo.core;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ruby.bamboo.api.Constants;
import ruby.bamboo.packet.MessageBambooUtil;
import ruby.bamboo.packet.MessagePickaxe;

public class PacketDispatcher {
    private static byte packetId = 0;
    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);

    public static void init() {
        INSTANCE.registerMessage(MessageBambooUtil.MessageBambooUtilHandler.class, MessageBambooUtil.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(MessagePickaxe.MessagePickaxeHandler.class, MessagePickaxe.class, packetId++, Side.SERVER);
    }

    public static final void sendToServer(IMessage message) {
        PacketDispatcher.INSTANCE.sendToServer(message);
    }
}
