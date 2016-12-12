package ruby.bamboo.tileentity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import ruby.bamboo.api.Constants;
import ruby.bamboo.tileentity.TileMillStone;

public class RenderMillStone extends TileEntitySpecialRenderer<TileMillStone> {
    public static ModelMillStone model;
    public static RenderMillStone instance;
    private static ResourceLocation RESOURCE = new ResourceLocation(Constants.RESOURCED_DOMAIN + "textures/entitys/millstone.png");

    public RenderMillStone() {
        instance = this;
        model = new ModelMillStone();
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public void renderTileEntityAt(TileMillStone entity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (entity == null) {
            renderInv();
        } else {
            GL11.glPushMatrix();
            this.bindTexture(RESOURCE);
            GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            model.render((TileMillStone) entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
        }

    }

    public void renderInv() {
        GL11.glPushMatrix();
        this.bindTexture(RESOURCE);
        GL11.glTranslatef(1F, 0.125F, 0);
        GL11.glScalef(0.95F, 0.95F, 0.95F);
        model.renderInv();
        GL11.glPopMatrix();
    }
}
