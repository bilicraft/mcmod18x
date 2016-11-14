package ruby.bamboo.item.itemblock;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import ruby.bamboo.api.Constants;

public class ItemBambooPane extends ItemMultiTexture implements ISubTexture {
    enum Textuer implements IEnumTex {
        NOMAL(0, "bamboopane"),
        TAN1(1, "bamboopane2"),
        TAN2(2, "bamboopane3"),
        RANMA(3, "ranma");

        int id;
        String json;

        Textuer(int id, String json) {
            this.id = id;
            this.json = json;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getJsonName() {
            return Constants.RESOURCED_DOMAIN + json;
        }

    }

    public ItemBambooPane(Block block) {
        super(block, block, Arrays.stream(Textuer.values()).map(e -> e.toString().toLowerCase()).collect(Collectors.toList()).toArray(new String[] {}));
    }

    @Override
    public IEnumTex[] getName() {
        return Textuer.values();
    }

}
