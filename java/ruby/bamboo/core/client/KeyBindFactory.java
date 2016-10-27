package ruby.bamboo.core.client;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyBindFactory {

    public static final KeyBinding KEY_UTIL = new KeyBinding("key.bamboomod_utilkey", Keyboard.KEY_V, "key.categories.gameplay");
    private static KeyBinding[] keyArray;
    private static Map<KeyBinding, IKey> map = new HashMap<>();

    public static void preInit() {
        // 汎用キー
        ClientRegistry.registerKeyBinding(KEY_UTIL);
        MinecraftForge.EVENT_BUS.register(new KeyBindFactory());
        keyArray = new KeyBinding[] { KEY_UTIL };
    }

    public static void init() {

        KeyBindFactory.regist(KeyBindFactory.KEY_UTIL, key -> {
            EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
            if (key.isKeyDown()) {
                ItemStack stack = player.getHeldItemMainhand();
                if (stack != null && stack.getItem() instanceof IItemUtilKeylistener) {
                    ((IItemUtilKeylistener) stack.getItem()).exec(key);
                }
            }
        });
    }

    public static void regist(KeyBinding key, IKey ikey) {
        map.put(key, ikey);
    }

    @SubscribeEvent
    public void ticker(TickEvent.ClientTickEvent e) {
        for (KeyBinding key : keyArray) {
            map.get(key).exec(key);
        }
    }

    public interface IItemUtilKeylistener {
        /**
         * キーが押された時コールされる、クライアント専用。
         */
        @SideOnly(Side.CLIENT)
        public void exec(KeyBinding key);

    }

    private interface IKey {
        public void exec(KeyBinding key);
    }

}
