package ruby.bamboo.tileentity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import ruby.bamboo.api.Constants;
import ruby.bamboo.tileentity.TileCampfire;

public class RenderCampfire extends TileEntitySpecialRenderer<TileCampfire> {
    public static ModelCampfire model;
    public static RenderCampfire instance;
    private static ResourceLocation RESOURCE = new ResourceLocation(Constants.RESOURCED_DOMAIN + "textures/entitys/campfire.png");

    public RenderCampfire() {
        instance = this;
        model = new ModelCampfire();
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public void renderTileEntityAt(TileCampfire entity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (entity == null) {
            renderInv();
        } else {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
            this.bindTexture(RESOURCE);
            GL11.glRotatef(entity.getRotate(), 0.0F, 1.0F, 0.0F);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            model.renderWood();

            switch (entity.getBakeType()) {
                case ATHER:
                    model.renderRods();
                    model.renderPot();
                    break;
                case FISH:
                    model.renderFish();
                    break;
                case MEAT:
                    model.renderRods();
                    model.renderMeat(entity.getMeatroll());
                    break;
                case NONE:
                default:
                    break;
            }

            GL11.glPopMatrix();
        }

    }

    public void renderInv() {
        GL11.glPushMatrix();
        this.bindTexture(RESOURCE);
        GL11.glTranslatef(1F, 0, 0);
        GL11.glScalef(1.68F, 1.68F, 1.68F);
        RenderCampfire.model.renderWood();
        GL11.glPopMatrix();
    }
}
