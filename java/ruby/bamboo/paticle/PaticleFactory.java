package ruby.bamboo.paticle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PaticleFactory {
    @SideOnly(Side.CLIENT)
    public static void createColoerSomoke(World world, double posX, double posY, double posZ, int color) {
        if (world.isRemote) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ColorSmoke(world, posX, posY, posZ, 0, 0, 0, 1).setColor(color));
        }
    }
}
