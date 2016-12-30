package ruby.bamboo.core;

import java.io.File;
import java.util.function.Supplier;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class Config {

    public static final ConfigEntry<Integer> DIMID = new ConfigEntry<>("dimensionId", new DefaultSupplier<>(DimensionManager.getNextFreeDimId()));

    private static final Configuration conf;
    static {
        File file = new File(Loader.instance().getConfigDir(), "BambooConfig.cfg");
        conf = new Configuration(file);
        conf.load();
    }

    // インゲームUIからの変更保存
    public static void save() {
        conf.save();
    }

    public static class ConfigEntry<T> {
        private Supplier<T> defVal;
        private final String key;
        private String category;

        private ConfigEntry(String key, Supplier<T> defVal) {
            this.key = key;
            this.defVal = defVal;
        }

        private ConfigEntry(String key, String category, Supplier<T> defVal) {
            this(key, defVal);
            this.category = category;
        }

        public T get() {
            return this.getData(this.getProperty());
        }

        //別途save
        public void set(T data) {
            this.getProperty().set(data.toString());
        }

        private void setComment(String comment) {
            this.getProperty().setComment(comment);
        }

        private Property getProperty() {
            ConfigCategory catIn = conf.getCategory(this.getCategory());
            if (catIn.containsKey(key)) {
                return conf.getCategory(this.getCategory()).get(this.key);
            } else {
                // デフォルト設定
                Property prop = new Property(key, this.getDefault().toString(), this.getPropType());
                catIn.put(key, prop);
                conf.save();
                return prop;
            }
        }

        private String getCategory() {
            if (category != null) {
                return category;
            }
            return Configuration.CATEGORY_GENERAL;
        }

        private T getDefault() {
            return defVal.get();
        }

        private T getData(Property prop) {
            Object o;
            switch (prop.getType()) {
                case BOOLEAN:
                    o = prop.getBoolean();
                    break;
                case DOUBLE:
                    o = prop.getDouble();
                    break;
                case INTEGER:
                    o = prop.getInt();
                    break;
                default:
                    o = prop.getString();
                    break;
            }
            return (T) o;
        }

        private Property.Type getPropType() {
            T obj = getDefault();
            if (obj instanceof Boolean) {
                return Property.Type.BOOLEAN;
            } else if (obj instanceof Double) {
                return Property.Type.DOUBLE;
            } else if (obj instanceof Integer) {
                return Property.Type.INTEGER;
            } else {
                return Property.Type.STRING;
            }
        }
    }

    private static class DefaultSupplier<T> implements Supplier<T> {
        private T data;

        private DefaultSupplier(T data) {
            this.data = data;
        }

        @Override
        public T get() {
            return data;
        }

    }

}
