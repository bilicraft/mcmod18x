package ruby.bamboo.core;

import java.io.File;
import java.util.function.Supplier;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class Config<T> {

    public static final Config<Integer> TEST = new Config<>();
    private Supplier<T> sup;

    private Config() {}

    private void setSup(Supplier<T> sup) {
        this.sup = sup;
    }

    public static void load() {
        File file = new File(Loader.instance().getConfigDir(), "BambooConfig.cfg");
        Configuration configuration = new Configuration(file);
        TEST.sup = () -> 1;
        int str = TEST.get();
        System.out.println(str);
    }

    public <T> T get() {
        if (this.sup == null) {
            throw new RuntimeException("No setting config");
        }
        return (T) this.sup.get();
    }


}
