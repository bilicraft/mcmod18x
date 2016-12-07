package ruby.bamboo.core.init;

import java.lang.reflect.Constructor;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruby.bamboo.api.Constants;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.BambooData.BambooItem;

/**
 * ブロック自動登録
 */
public class DataLoader extends ClassFinder {

    /**
     * 初期処理
     */
    public List<String> init(String packagePath) {
        // json登録用
        List<String> registedList = Lists.newArrayList();
        try {
            for (Class entry : search(packagePath)) {
                if (entry.isAnnotationPresent(BambooBlock.class)) {
                    registBlock(entry, registedList);
                } else if (entry.isAnnotationPresent(BambooItem.class)) {
                    registItem(entry, registedList);
                }
            }
        } catch (Exception e) {
            FMLLog.bigWarning("ブロック初期化例外");
            throw new RuntimeException(e);
        }
        return registedList;
    }

    /**
     * ブロック側アノテーション処理
     *
     * @param cls
     * @param registedList
     */
    private void registBlock(Class<? extends Block> cls, List<String> registedList) {
        try {
            FMLLog.info(cls.getName() + " to Loading");
            // マテリアルを持つコンストラクタはアノテーションでしたマテリアルで初期化する(継承対策)
            Class c = Class.forName(cls.getName());
            Block instance = null;
            BambooBlock anoData = cls.getAnnotation(BambooBlock.class);
            // 通常単一ブロック登録
            if (anoData.subblock() == SubBlockBase.class) {
                try {
                    Constructor cnst = c.getDeclaredConstructor(Material.class);
                    cnst.setAccessible(true);

                    instance = (Block) cnst.newInstance(anoData.material().MATERIAL);
                } catch (NoSuchMethodException e) {
                    instance = (Block) c.newInstance();
                }

                // 名前の指定がないときはクラス名小文字
                String name = anoData.name().isEmpty() ? cls.getSimpleName().toLowerCase() : anoData.name().toLowerCase();
                if (anoData.createiveTabs() != EnumCreateTab.NONE) {
                    instance.setCreativeTab(anoData.createiveTabs().getTabInstance());
                }
                instance.setUnlocalizedName(name);

                GameRegistry.registerBlock(instance, anoData.itemBlock(), name);

                FMLLog.info("BLOCK: %s to Registed", name);
                registedList.add(Constants.RESOURCED_DOMAIN + name);
            } else {
                // 同一クラス使用連続ブロック登録
                for (SubBlockBase sub : anoData.subblock().newInstance().getList()) {

                    try {
                        Constructor cnst = c.getDeclaredConstructor(SubBlockBase.class);
                        cnst.setAccessible(true);

                        instance = (Block) cnst.newInstance(sub);
                    } catch (NoSuchMethodException e) {
                        throw new Exception(e);
                    }
                    // tile.抜き
                    String name = instance.getUnlocalizedName().substring(5);
                    if (anoData.createiveTabs() != EnumCreateTab.NONE) {
                        instance.setCreativeTab(anoData.createiveTabs().getTabInstance());
                    }
                    GameRegistry.registerBlock(instance, anoData.itemBlock(), name);

                    FMLLog.info("BLOCK: %s to Registed", name);
                    registedList.add(Constants.RESOURCED_DOMAIN + name);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
            //            e.printStackTrace();
        }
    }

    /**
     * アイテム側アノテーション処理
     *
     * @param cls
     * @param registedList
     */
    private void registItem(Class<? extends Item> cls, List<String> registedList) {
        try {
            FMLLog.info(cls.getName() + " to Loading");
            Class c = Class.forName(cls.getName());
            Item instance = (Item) c.newInstance();

            BambooItem anoData = cls.getAnnotation(BambooItem.class);
            // 名前の指定がないときはクラス名小文字
            String name = anoData.name().isEmpty() ? cls.getSimpleName().toLowerCase() : anoData.name().toLowerCase();
            if (anoData.createiveTabs() != EnumCreateTab.NONE) {
                instance.setCreativeTab(anoData.createiveTabs().getTabInstance());
            }
            instance.setUnlocalizedName(name);

            GameRegistry.registerItem(instance, name);
            FMLLog.info("ITEM: %s to Registed", name);

            registedList.add(Constants.RESOURCED_DOMAIN + name);
        } catch (Exception e) {
            FMLLog.warning("アイテムインスタンス登録例外:" + cls.getName());
            e.printStackTrace();
        }
    }

}
