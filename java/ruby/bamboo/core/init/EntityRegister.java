package ruby.bamboo.core.init;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.BambooCore;
import ruby.bamboo.entity.EntityRuneEffect;
import ruby.bamboo.entity.SakuraPetal;
import ruby.bamboo.entity.SlideDoor;
import ruby.bamboo.entity.Wind;
import ruby.bamboo.entity.arrow.EntityAntiArrow;
import ruby.bamboo.entity.arrow.EntityBambooArrow;
import ruby.bamboo.entity.arrow.EntityExplodeArrow;
import ruby.bamboo.entity.arrow.EntityLightArrow;
import ruby.bamboo.entity.arrow.EntityTorchArrow;
import ruby.bamboo.entity.arrow.TimerBomb;
import ruby.bamboo.render.entity.RenderBambooArrow;
import ruby.bamboo.render.entity.RenderPetal;
import ruby.bamboo.render.entity.RenderRuneEffect;
import ruby.bamboo.render.entity.RenderSlideDoor;
import ruby.bamboo.render.entity.RenderWind;

public class EntityRegister {

    public void entityRegist() {
        int entityId = 0;
        registerEntity(Wind.class, "wind", entityId++);
        registerEntity(SlideDoor.class, "slideDoor", entityId++);
        registerEntity(EntityBambooArrow.class, "bambooArrow", entityId++);
        registerEntity(EntityTorchArrow.class, "torchArrow", entityId++);
        registerEntity(EntityAntiArrow.class, "antiArrow", entityId++);
        registerEntity(EntityLightArrow.class, "lightArrow", entityId++);
        registerEntity(EntityExplodeArrow.class, "explodeArrow", entityId++);
        registerEntity(TimerBomb.class, "timerBomb", entityId++);
    }

    @SideOnly(Side.CLIENT)
    public void renderRegist() {
        this.registRender(Wind.class, RenderWind.class);
        this.registRender(SlideDoor.class, RenderSlideDoor.class);
        this.registRender(SakuraPetal.class, RenderPetal.class);
        this.registRender(EntityBambooArrow.class, RenderBambooArrow.class);
        this.registRender(EntityTorchArrow.class, RenderBambooArrow.class);
        this.registRender(EntityAntiArrow.class, RenderBambooArrow.class);
        this.registRender(EntityLightArrow.class, RenderBambooArrow.class);
        this.registRender(EntityExplodeArrow.class, RenderBambooArrow.class);
        this.registRender(EntityRuneEffect.class, RenderRuneEffect.class);
        this.registRender(TimerBomb.class, RenderWind.class);
    }

    private void registerEntity(Class<? extends Entity> entityClass, String entityName, int id) {
        this.registerEntity(entityClass, entityName, id, 80, 3, true);
    }

    /**
     *
     * @param entityClass
     * @param entityName
     * @param id
     * @param trackingRange 追跡範囲？
     * @param updateFrequency 更新間隔tick？
     * @param sendsVelocityUpdates クライアントへのmotion値送信？
     */
    void registerEntity(Class<? extends Entity> entityClass, String entityName, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {

        EntityRegistry.registerModEntity(entityClass, entityName, id, BambooCore.instance, trackingRange, updateFrequency, sendsVelocityUpdates);

    }

    private void registRender(Class<? extends Entity> cls, Class<? extends Render> render) {
        RenderingRegistry.registerEntityRenderingHandler(cls, manager -> {
            try {
                return render.getConstructor(manager.getClass()).newInstance(manager);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

    }

}
