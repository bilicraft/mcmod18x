package ruby.bamboo.core;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.ArrayListMultimap;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.item.BambooBow;

@SideOnly(Side.CLIENT)
public class KeyBindFactory {

    public static final KeyBinding KEY_UTIL = new KeyBinding("key.bamboomod_utilkey", Keyboard.KEY_V, "key.categories.gameplay");
    private static ArrayListMultimap<KeyBinding, IBambooKeylistener> map = ArrayListMultimap.create();

    public static void preInit() {
        // 汎用キー
        ClientRegistry.registerKeyBinding(KEY_UTIL);
        MinecraftForge.EVENT_BUS.register(new KeyBindFactory());
    }

    public static void init() {

        KeyBindFactory.regist(KeyBindFactory.KEY_UTIL, DataManager.getItem(BambooBow.class));
    }

    public static void regist(KeyBinding key, IBambooKeylistener obj) {
        map.put(key, obj);
    }

    @SubscribeEvent
    public void ticker(TickEvent.ClientTickEvent e) {
        for (KeyBinding key : map.keySet()) {
            if (key.isKeyDown()) {
                for (IBambooKeylistener lsn : map.get(key)) {
                    lsn.exec(key);
                }
            }
        }
    }

    public interface IBambooKeylistener {
        public void exec(KeyBinding key);
        public void callback(EntityPlayer playerEntity, ItemStack is, byte data);
    }
}
