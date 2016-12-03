package ruby.bamboo.texture;

import java.util.Map;

/**
 * ItemLayerModel専用
 */
public interface IMultiTextuerItem {
    /**
     * テクスチャマップの取得
     *
     * @return Key:ダメージ値,Val:パス
     */
    Map<Integer, String> getTexName();

}
