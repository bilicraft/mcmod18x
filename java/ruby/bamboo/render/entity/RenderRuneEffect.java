package ruby.bamboo.render.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import ruby.bamboo.core.Constants;
import ruby.bamboo.entity.EntityRuneEffect;

public class RenderRuneEffect extends Render {
    private final Cylinder cylinder;
    private static final ResourceLocation RESOUCE = new ResourceLocation(Constants.RESOURCED_DOMAIN + "textures/entitys/rune.png");

    public RenderRuneEffect(RenderManager manager) {
        super(manager);
        cylinder = new Cylinder();
        cylinder.setTextureFlag(true);

        cylinder.setNormals(GLU.GLU_SMOOTH);
        cylinder.setOrientation(GLU.GLU_INSIDE);
    }

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        this.render((EntityRuneEffect) entity, (float) d0, (float) d1, (float) d2);
    }

    private void render(EntityRuneEffect entity, float posX, float posY, float posZ) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        float red = ((entity.getRingColor() >> 16) & 0xFF) / 255F;
        float green = ((entity.getRingColor() >> 8) & 0xFF) / 255F;
        float blue = (entity.getRingColor() & 0xFF) / 255F;
        GL11.glColor4f(red, green, blue, 0.8F);
        GL11.glTranslatef(posX, posY + 1F, posZ);
        GL11.glRotatef(90, 1F, 0F, 0);
        GL11.glRotatef(entity.prevRotationPitch, 1F, 0, 0);
        GL11.glRotatef(entity.prevRotationYaw, 0, 1F, 0);
        GL11.glRotatef(entity.roll, 0, 0, 1F);
        float ringsize = entity.getRingsize();
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        //GL11.glLoadIdentity();
        GL11.glScalef(ringsize * 2, 1F, ringsize * 2);
        this.bindTexture(RESOUCE);
        cylinder.draw(ringsize, ringsize, 0.5F, 16, 1);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return RESOUCE;
    }

}
