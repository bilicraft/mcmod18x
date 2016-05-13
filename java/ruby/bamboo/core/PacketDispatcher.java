package ruby.bamboo.core;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ruby.bamboo.packet.ChangeAmmo;

public class PacketDispatcher {
    private static byte packetId = 0;
    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);

    public static void init() {
        INSTANCE.registerMessage(ChangeAmmo.ChangeAmmoHandler.class, ChangeAmmo.class, packetId++, Side.SERVER);
    }

    public static final void sendToServer(IMessage message) {
        PacketDispatcher.INSTANCE.sendToServer(message);
    }
}
