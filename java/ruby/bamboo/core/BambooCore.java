package ruby.bamboo.core;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import ruby.bamboo.api.Constants;
import ruby.bamboo.command.ATCommand;
import ruby.bamboo.command.JumpCommand;
import ruby.bamboo.dimension.BambooDimension;
import ruby.bamboo.proxy.CommonProxy;

@Mod(modid = Constants.MODID, name = Constants.MODID, version = "Minecraft" + Constants.MC_VER + " var" + Constants.BAMBOO_VER, useMetadata = true)
// フォージバージョン制限、必要になれば。 dependencies = "required-after:Forge@[10.13.2.1230,)")
public class BambooCore {

    public static final boolean DEBUGMODE = isDevelopment();

    @SidedProxy(serverSide = "ruby.bamboo.proxy.CommonProxy", clientSide = "ruby.bamboo.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Instance(Constants.MODID)
    public static BambooCore instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit();
        new BambooDimension();
        new Thread(new UpdateCheck()).start();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ATCommand());
        event.registerServerCommand(new JumpCommand());
    }

    /**
     * デバッグ環境判定用
     *
     * @return
     */
    private static boolean isDevelopment() {
        boolean result;
        try {
            result = ReflectionHelper.getPrivateValue(CoreModManager.class, null, "deobfuscatedEnvironment");
        } catch (UnableToAccessFieldException e) {
            FMLLog.warning("Debug mode forced false!");
            result = false;
        }
        return result;
    }

}
