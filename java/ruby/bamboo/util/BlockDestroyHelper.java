package ruby.bamboo.util;

import java.util.LinkedList;

import com.google.common.collect.Lists;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class BlockDestroyHelper {
    private World world;
    private LinkedList<BlockPos> list;

    private BlockDestroyHelper(World world, LinkedList<BlockPos> list) {
        this.world = world;
        this.list = list;
    }

    public static void addQueue(World target, Iterable<BlockPos> c) {
        LinkedList<BlockPos> list = Lists.newLinkedList(c);
        MinecraftForge.EVENT_BUS.register(new BlockDestroyHelper(target, list));
    }

    @SubscribeEvent
    public void tick(WorldTickEvent e) {
        if (!e.world.isRemote && e.world == this.world) {
            if (list != null && !list.isEmpty()) {
                BlockPos pos = list.poll();
                if (world.getBlockState(pos).getBlock().getBlockHardness(world, pos) > 0) {
                    e.world.destroyBlock(pos, true);
                }
            } else {
                list.clear();
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}
