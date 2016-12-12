package ruby.bamboo.item.arrow;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.api.Constants;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.entity.arrow.BaseArrow;
import ruby.bamboo.entity.arrow.EntityAntiArrow;
import ruby.bamboo.item.itemblock.IEnumTex;
import ruby.bamboo.item.itemblock.ISubTexture;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class AntiArrow extends ArrowBase implements ISubTexture {

    public AntiArrow() {
        this.setHasSubtypes(true);
    }

    public enum AntiType {
        // 不明
        UNKNOWN(0),
        // 不死っぽいの
        UNDEAD(1, EntityZombie.class, EntitySkeleton.class),
        // 変異してそうなの
        MUTANT(2, EntityCreeper.class, EntityPigZombie.class),
        // エンダーは本来矢に当たらないため、専用
        ENDERMAN(3, EntityEnderman.class, EntityEndermite.class),
        // 燃えてそうなの、isFlame状態も含む
        FLAME(4, EntityBlaze.class, EntityMagmaCube.class),
        // 浮遊とか飛行系、対象が多いため、倍率低め
        AIR(5, EntityGhast.class, EntityBlaze.class, EntityBat.class, EntityDragon.class, EntityWither.class),
        // 多脚生物
        MULTILEG(6, EntitySpider.class, EntityGhast.class, EntitySquid.class),
        // 人(？)型生物、不死以外だけどゾンビ村人は含みそう
        HUMAN(7, EntityVillager.class, EntityWitch.class, EntityPlayer.class),
        // 脚がなさそうなもの、こちらも対象が多いため倍率低め
        NONLEG(8, EntitySlime.class, EntityBlaze.class, EntityWither.class, EntitySnowman.class, EntityGuardian.class),
        // 硬そうなの
        HARD(9, EntityIronGolem.class, EntityBlaze.class),
        // 柔らかそうなの
        SOFT(10, EntityGhast.class, EntitySlime.class),
        // 水棲
        WATER(11, EntityGuardian.class, EntitySquid.class),;

        private byte id;
        private Class<? extends Entity>[] entities;

        private AntiType(int id, Class<? extends Entity>... entities) {
            this.id = (byte) id;
            this.entities = entities;
        }

        public byte getID() {
            return id;
        }

        public Class<? extends Entity>[] getEntity() {
            return entities;
        }

        public static AntiType getType(int id) {
            for (AntiType type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    @Override
    public BaseArrow createArrowIn(World world, ItemStack bow, ItemStack arrow, float power, int chargeFrame, EntityPlayer player) {
        EntityAntiArrow entityArrow = new EntityAntiArrow(world, player, power * 2.0f);
        entityArrow.setArrowType(AntiType.getType(arrow.getItemDamage()));

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);

        if (j > 0) {
            entityArrow.setDamage(entityArrow.getDamage() + j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);

        if (k > 0) {
            entityArrow.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0) {
            entityArrow.setFire(100);
        }

        return entityArrow;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (AntiType type : AntiType.values()) {
            if (type != AntiType.UNKNOWN) {
                subItems.add(new ItemStack(this, 1, type.getID()));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + AntiType.getType(stack.getItemDamage()).name().toLowerCase();
    }

    @Override
    public float getBowModel(int useRemaining) {
        return useRemaining >= 18 ? 1 : useRemaining > 13 ? 0.7F : 0;
    }

    @Override
    public Class<? extends BaseArrow> getArrowClass() {
        return EntityAntiArrow.class;
    }

    @Override
    public IEnumTex[] getName() {
        IEnumTex[] tex = new IEnumTex[AntiType.values().length];
        List<IEnumTex> list = new ArrayList<>();
        for (AntiType type : AntiType.values()) {
            list.add(new IEnumTex() {

                @Override
                public String getJsonName() {
                    return Constants.RESOURCED_DOMAIN + "antiarrow";
                }

                @Override
                public int getId() {
                    return type.getID();
                }
            });
        }
        return list.toArray(new IEnumTex[0]);
    }

}
