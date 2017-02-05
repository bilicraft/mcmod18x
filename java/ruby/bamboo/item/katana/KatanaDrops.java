package ruby.bamboo.item.katana;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.api.katana.KatanaDropManager;
import ruby.bamboo.api.katana.KatanaDropManager.KatanaDropItem;

public class KatanaDrops {

    public void regist() {
        // Zombie
        KatanaDropManager.addDrop(EntityZombie.class, new KatanaDropItem(Items.LEATHER, 0.2F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityZombie.class, new KatanaDropItem(Items.BONE, 0.2F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityZombie.class, new KatanaDropItem(new ItemStack(Items.SKULL, 1, 2), 0.002F));
        // Skeleton
        KatanaDropManager.addDrop(EntitySkeleton.class, new KatanaDropItem(new ItemStack(Items.SKULL, 1, 0), 0.002F));
        KatanaDropManager.addDrop(EntitySkeleton.class, new KatanaDropItem(new ItemStack(Items.SKULL, 1, 1), 0.001F));
        // Creeper
        KatanaDropManager.addDrop(EntityCreeper.class, new KatanaDropItem(Items.GUNPOWDER, 0.2F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityCreeper.class, new KatanaDropItem(Blocks.TNT, 0.01F).setRandomAddAmount(2));
        //        KatanaDropManager.addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(BambooInit.firecracker, 1, 0), 0.5F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(Items.SKULL, 1, 4), 0.002F));
        // Spider
        KatanaDropManager.addDrop(EntitySpider.class, new KatanaDropItem(Blocks.WEB, 0.1F));
        // Ghast
        KatanaDropManager.addDrop(EntityGhast.class, new KatanaDropItem(Items.FIRE_CHARGE, 0.5F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityGhast.class, new KatanaDropItem(Blocks.TNT, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityGhast.class, new KatanaDropItem(Items.GLOWSTONE_DUST, 0.5F).setRandomAddAmount(8));
        // Ender
        KatanaDropManager.addDrop(EntityEnderman.class, new KatanaDropItem(Blocks.RED_FLOWER, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityEnderman.class, new KatanaDropItem(Blocks.YELLOW_FLOWER, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityEnderman.class, new KatanaDropItem(Blocks.BROWN_MUSHROOM, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityEnderman.class, new KatanaDropItem(Blocks.RED_MUSHROOM, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityEnderman.class, new KatanaDropItem(Items.ENDER_EYE, 0.05F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityEnderman.class, new KatanaDropItem(BambooBlocks.BAMBOOSHOOT, 0.05F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityEnderman.class, new KatanaDropItem(BambooBlocks.SAKURA_SAPLING, 0.05F).setRandomAddAmount(3));
        // Silverfish
        KatanaDropManager.addDrop(EntitySilverfish.class, new KatanaDropItem(Items.PAPER, 0.5F).setRandomAddAmount(4));
        // Blaze
        KatanaDropManager.addDrop(EntityBlaze.class, new KatanaDropItem(Items.FIRE_CHARGE, 0.1F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityGhast.class, new KatanaDropItem(Items.GLOWSTONE_DUST, 0.2F).setRandomAddAmount(8));
        // Pig
        KatanaDropManager.addDrop(EntityPig.class, new KatanaDropItem(Items.LEATHER, 0.8F).setRandomAddAmount(3));
        // Sheep
        KatanaDropManager.addDrop(EntitySheep.class, new KatanaDropItem(Items.STRING, 0.5F).setRandomAddAmount(4));
        // Bat
        KatanaDropManager.addDrop(EntityBat.class, new KatanaDropItem(Items.APPLE, 0.5F).setRandomAddAmount(2));
        KatanaDropManager.addDrop(EntityBat.class, new KatanaDropItem(Items.GOLDEN_APPLE, 0.01F));
        // Witch
        KatanaDropManager.addDrop(EntityWitch.class, new KatanaDropItem(Blocks.WATERLILY, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityWitch.class, new KatanaDropItem(Items.GLASS_BOTTLE, 0.5F).setRandomAddAmount(4));
        // Chicken
        KatanaDropManager.addDrop(EntityChicken.class, new KatanaDropItem(Items.EGG, 0.25F).setRandomAddAmount(2));
        // Wolf
        KatanaDropManager.addDrop(EntityWolf.class, new KatanaDropItem(Items.BONE, 0.5F).setRandomAddAmount(2));
        KatanaDropManager.addDrop(EntityWolf.class, new KatanaDropItem(Items.BEEF, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityWolf.class, new KatanaDropItem(Items.PORKCHOP, 0.5F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityWolf.class, new KatanaDropItem(Items.CHICKEN, 0.5F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityWolf.class, new KatanaDropItem(Items.RABBIT, 0.5F).setRandomAddAmount(3));
        KatanaDropManager.addDrop(EntityWolf.class, new KatanaDropItem(Items.FEATHER, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityWolf.class, new KatanaDropItem(Items.MUTTON, 0.5F).setRandomAddAmount(4));
        KatanaDropManager.addDrop(EntityWolf.class, new KatanaDropItem(Items.LEATHER, 0.3F).setRandomAddAmount(3));
        // Ocelot
        KatanaDropManager.addDrop(EntityOcelot.class, new KatanaDropItem(Items.FISH, 0.75F).setRandomAddAmount(2));
        KatanaDropManager.addDrop(EntityOcelot.class, new KatanaDropItem(Items.CHICKEN, 0.5F).setRandomAddAmount(2));
    }
}
