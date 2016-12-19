package ruby.bamboo.core;

import java.io.File;
import java.util.function.Supplier;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class Config {

    public static final ConfigEntry<Integer> DIMID = new ConfigEntry<>("dimensionId", () -> DimensionManager.getNextFreeDimId());

    public static void load() {
        File file = new File(Loader.instance().getConfigDir(), "BambooConfig.cfg");
        Configuration conf = new Configuration(file);
        conf.load();

        DIMID.set(get(conf.get(Configuration.CATEGORY_GENERAL, DIMID.name, DIMID.getDefault()), DIMID.data.getClass()));

        conf.save();
        System.out.println();
    }

    private static <T> T get(Property prop, Class<T> type) {
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
            case STRING:
                o = prop.getString();
                break;
            default:
                o = null;
        }
        return type.cast(o);
    }

    public static class ConfigEntry<T> {
        private T data;
        private Supplier<T> defVal;
        private final String name;

        private ConfigEntry(String name, Supplier<T> defVal) {
            this.name = name;
            this.defVal = defVal;
        }

        public T get() {
            if (this.data != null) {
                return data;
            }
            throw new RuntimeException("Data not found.");
        }

        public void set(T data) {
            if (data != null) {
                this.data = data;
            }
        }

        private T getDefault() {
            return data = defVal.get();
        }
    }

}
