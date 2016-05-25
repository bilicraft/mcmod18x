package ruby.bamboo.item.arrow;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;

public abstract class ArrowBase extends Item implements IBambooArrow{
    public abstract Class<? extends EntityArrow> getArrowClass();
}
