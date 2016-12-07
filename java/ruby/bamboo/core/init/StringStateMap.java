package ruby.bamboo.core.init;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StringStateMap extends StateMapperBase {
    private final String name;
    private final String suffix;
    private final List<IProperty<?>> ignored;

    private StringStateMap(String name, @Nullable String suffix, List<IProperty<?>> ignored) {
        this.name = name;
        this.suffix = suffix;
        this.ignored = ignored;
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.<IProperty<?>, Comparable<?>> newLinkedHashMap(state.getProperties());
        String s = name;

        if (this.suffix != null) {
            s = s + this.suffix;
        }

        for (IProperty<?> iproperty : this.ignored) {
            map.remove(iproperty);
        }

        return new ModelResourceLocation(s, this.getPropertyString(map));
    }

    @SideOnly(Side.CLIENT)
    public static class Builder {
        private String name;
        private String suffix;
        private final List<IProperty<?>> ignored = Lists.<IProperty<?>> newArrayList();

        public StringStateMap.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public StringStateMap.Builder withSuffix(String builderSuffixIn) {
            this.suffix = builderSuffixIn;
            return this;
        }

        /**
         * Add properties that will not be used to compute all possible states
         * of a block, used for block rendering
         * to ignore some property that does not alter block's appearance
         */
        public StringStateMap.Builder ignore(IProperty<?>... p_178442_1_) {
            Collections.addAll(this.ignored, p_178442_1_);
            return this;
        }

        public StringStateMap build() {
            return new StringStateMap(this.name, this.suffix, this.ignored);
        }
    }
}
