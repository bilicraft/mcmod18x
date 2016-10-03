package ruby.bamboo.core.init;

import net.minecraft.block.material.Material;

public enum EnumMaterial {
    AIR(Material.AIR),
    GRASS(Material.GRASS),
    GROUND(Material.GROUND),
    WOOD(Material.WOOD),
    ROCK(Material.ROCK),
    IRON(Material.IRON),
    ANVIL(Material.ANVIL),
    WATER(Material.WATER),
    LAVA(Material.LAVA),
    LEAVES(Material.LEAVES),
    PLANTS(Material.PLANTS),
    VINE(Material.VINE),
    SPONGE(Material.SPONGE),
    CLOTH(Material.CLOTH),
    FIRE(Material.FIRE),
    SAND(Material.SAND),
    CIRCUITS(Material.CIRCUITS),
    CARPET(Material.CARPET),
    GLASS(Material.GLASS),
    REDSTONELIGHT(Material.REDSTONE_LIGHT),
    TNT(Material.TNT),
    CORAL(Material.CORAL),
    ICE(Material.ICE),
    PACKEDICE(Material.PACKED_ICE),
    SNOW(Material.SNOW),
    CRAFTEDSNOW(Material.CRAFTED_SNOW),
    CACTUS(Material.CACTUS),
    CLAY(Material.CLAY),
    GOURD(Material.GOURD),
    DRAGONEGG(Material.DRAGON_EGG),
    PORTAL(Material.PORTAL),
    CAKE(Material.CAKE),
    WEB(Material.WEB);
    public Material MATERIAL;

    EnumMaterial(Material material) {
        this.MATERIAL = material;
    }

}
