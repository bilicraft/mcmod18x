package ruby.bamboo.core.init;

import java.lang.reflect.Constructor;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
            e.printStackTrace();
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
            // マテリアルを持つコンストラクタはアノテーションでしたマテリアルで初期化する(継承対策)
            Class c = Class.forName(cls.getName());
            Block instance = null;
            try {
                Constructor cnst = c.getConstructor(Material.class);
                instance = (Block) cnst.newInstance(((BambooBlock) c.getAnnotation(BambooBlock.class)).material().MATERIAL);
            } catch (NoSuchMethodException e) {
                instance = (Block) c.newInstance();
            }

            BambooBlock anoData = cls.getAnnotation(BambooBlock.class);
            // 名前の指定がないときはクラス名小文字
            String name = anoData.name().isEmpty() ? cls.getSimpleName().toLowerCase() : anoData.name().toLowerCase();
            if (anoData.createiveTabs() != EnumCreateTab.NONE) {
                instance.setCreativeTab(anoData.createiveTabs().getTabInstance());
            }
            instance.setUnlocalizedName(name);
            GameRegistry.registerBlock(instance, anoData.itemBlock(), name);
            FMLLog.info("BLOCK: %s to Registed", name);
            registedList.add(name);
        } catch (Exception e) {
            FMLLog.warning("ブロックインスタンス登録例外:" + cls.getName());
            e.printStackTrace();
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

            registedList.add(name);
        } catch (Exception e) {
            FMLLog.warning("アイテムインスタンス登録例外:" + cls.getName());
            e.printStackTrace();
        }
    }

}
