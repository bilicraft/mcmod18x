package ruby.bamboo.enchant;

import static ruby.bamboo.enchant.EnchantConstants.*;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.BambooCore;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.core.client.KeyBindFactory.IItemUtilKeylistener;
import ruby.bamboo.gui.GuiHandler;
import ruby.bamboo.packet.MessageBambooUtil;
import ruby.bamboo.packet.MessageBambooUtil.IMessagelistener;
import ruby.bamboo.util.ItemStackHelper;

public interface IBambooEnchantable extends IItemUtilKeylistener, IMessagelistener {

    static final NBTTagCompound empty = new NBTTagCompound();

    // Expとレベルアップ
    default void addExpChance(ItemStack stack, EntityLivingBase target) {
        addExpChance(stack, target, 1);
    }

    default void addExpChance(ItemStack stack, EntityLivingBase target, float rate) {
        this.addExp(stack, (int) getEntityAtkExp(stack, target), rate);
    }

    default float getEntityAtkExp(ItemStack stack, EntityLivingBase target) {
        float exp = 1;
        if (target instanceof EntityMob) {
            if (target.getHealth() <= 0) {
                exp = (float) Math.ceil(target.getMaxHealth() * 0.1F);
            }
        }
        return exp;
    }

    default void addExpChance(ItemStack stack, World worldIn, Block blockIn, BlockPos pos) {
        addExpChance(stack, worldIn, blockIn, pos, 1);
    }

    default void addExpChance(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, float rate) {
        this.addExp(stack, (int) getBlockBreakExp(stack, worldIn, blockIn, pos), rate);
    }

    default float getBlockBreakExp(ItemStack stack, World worldIn, Block blockIn, BlockPos pos) {
        float exp = 1;
        Item dropItem = blockIn.getItemDropped(worldIn.getBlockState(pos), worldIn.rand, 0);
        //        float hardness = blockIn.getBlockHardness(worldIn, pos);
        float hardness = blockIn.getDefaultState().getBlockHardness(worldIn, pos);
        if (dropItem != null && dropItem != Item.getItemFromBlock(blockIn)) {
            //ドロップアイテムが存在し、itemBlockとドロップアイテムが違うパッタン
            exp = exp * hardness * 2F;
            exp = Math.round(exp);
        } else {
            //ドロップがnullもしくは同じタイプ。
            if (hardness > 1) {
                hardness = 1;
            }
            if (worldIn.rand.nextFloat() > hardness) {
                exp = 0;
            }
        }
        return exp;
    }

    default void addExp(ItemStack stack, int num, float rate) {
        int expNum = (int) Math.ceil(num * rate);
        int nuwExp = stack.getItemDamage();
        if (expNum + nuwExp > getNextExp(stack)) {
            nuwExp = getNextExp(stack);
        } else {
            nuwExp += expNum;
        }
        if (nuwExp < 0) {
            nuwExp = 0;
        }
        stack.setItemDamage(nuwExp);
    }

    static boolean canLevelUp(ItemStack stack) {
        return true || getNextExp(stack) <= stack.getItemDamage();
    }

    default void levelUp(ItemStack stack) {
        short[] t = new short[] { 100, 2000, 8000, 20000, 32000 };
        float[] f = new float[] { 1, 1.5F, 2 };
        int next = 100;
        stack.setItemDamage(0);
        int lv = getLV(stack) + 1;
        setLV(stack, lv);
        next = (int) (next * lv + (t[lv / 3 < t.length ? lv / 3 : 4] * f[lv % 3]));
        if (next > 32000) {
            next = 32000;
        }
        setNextExp(stack, next);
    }

    // 表示系
    // 表示用強度
    static String getDisplayEnchantStrength(int str) {
        int cnt = (int) Math.ceil(str / 50D);
        StringBuilder stb = new StringBuilder("[");
        for (; 6 <= cnt; cnt -= 6) {
            stb.append("+");
        }
        for (; 1 <= cnt; cnt--) {
            stb.append("*");
        }
        stb.append("]");
        return stb.toString();
    }

    // 接頭文字列
    default String getPrefix(ItemStack stack) {
        return "";
    }

    // 接尾文字列
    default String getSuffix(ItemStack stack) {
        int pow = (int) Math.ceil(getEnchntNBT(stack, SpecialEnchant.getEnchantmentByClass(Power.class), EnchantBase.SUB_WILD).getShort(ENCHANT_LV) / 50F);
        return " [LV:" + getLV(stack) + " Exp:" + (int) (getExpP(stack) * 100) + "%]" + (pow > 0 ? " +" + pow : "");
    }

    // 経験値割合
    static float getExpP(ItemStack stack) {
        float f = stack.getItem().getDamage(stack) / (float) getNextExp(stack);
        return f > 1 ? 1 : f;
    }

    // NBT操作
    static int getLV(ItemStack stack) {
        return getStatus(stack).getInteger(ENCHANT_LV);
    }

    static void setLV(ItemStack stack, int num) {
        getStatus(stack).setInteger(ENCHANT_LV, num);
    }

    static int getNextExp(ItemStack stack) {
        return getStatus(stack).getInteger(NEXT_EXP);
    }

    static void setNextExp(ItemStack stack, int num) {
        getStatus(stack).setInteger(NEXT_EXP, num);
    }

    static NBTTagCompound getStatus(ItemStack stack) {
        if (getNBT(stack).hasKey(TAG_STATUS)) {
            return getNBT(stack).getCompoundTag(TAG_STATUS);
        } else {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger(ENCHANT_LV, 0);
            nbt.setInteger(NEXT_EXP, 16);
            getNBT(stack).setTag(TAG_STATUS, nbt);
            return nbt;
        }
    }

    static NBTTagCompound getNBT(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        return nbt == null ? createNBT(stack) : nbt;
    }

    static NBTTagCompound createNBT(ItemStack stack) {
        NBTTagCompound nbttag = new NBTTagCompound();
        stack.setTagCompound(nbttag);
        // 39が上限
        nbttag.setInteger("RepairCost", 40);
        setLV(stack, 0);
        setNextExp(stack, 1);

        return nbttag;
    }

    // バニラエンチャント
    default void setNomalEnchLevelLevel(ItemStack stack, Enchantment targetEnch, int level) {
        NBTTagList nbttaglist = this.getNBTTagEnchList(stack);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            short enchId = nbttaglist.getCompoundTagAt(i).getShort(ENCHANT_ID);
            Enchantment ench = Enchantment.getEnchantmentByID(enchId);
            if (ench == targetEnch) {
                short enchLv = nbttaglist.getCompoundTagAt(i).getShort(ENCHANT_LV);
                if (level > 0) {
                    // レベル加算
                    nbttaglist.getCompoundTagAt(i).setShort(ENCHANT_LV, (short) (level));
                } else {
                    // 0以下の場合、エンチャント消滅
                    nbttaglist.removeTag(i);
                }
                return;
            }
        }
        // ターゲットが発見出来ない(新規追加)
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort(ENCHANT_ID, (short) Enchantment.getEnchantmentID(targetEnch));
        nbt.setShort(ENCHANT_LV, (short) level);
        nbttaglist.appendTag(nbt);
    }

    // 追加エンチャント
    default void addSpecialEnchLevelLevel(ItemStack stack, EnchantBase targetEnch, int subTypeId, int addLevel) {

        NBTTagList nbttaglist = getNBTTagSpenchList(stack);
        Iterator<NBTTagCompound> ite = ItemStackHelper.<NBTTagCompound> getNBTTagListIte(getNBTTagSpenchList(stack)).iterator();
        while (ite.hasNext()) {
            NBTTagCompound nbt = ite.next();
            short enchId = nbt.getShort(ENCHANT_ID);
            if (enchId == targetEnch.getEffectId(subTypeId)) {
                short enchLv = nbt.getShort(ENCHANT_LV);
                // 上昇値は切りあげ
                if (addLevel > 0) {
                    // レベル加算、すでに存在する場合、増加量は半分。
                    nbt.setShort(ENCHANT_LV, (short) (enchLv + (int) Math.ceil(addLevel / 2D)));
                } else {
                    if ((enchLv + addLevel) > 0) {
                        // レベル減算
                        nbt.setShort(ENCHANT_LV, (short) (enchLv - addLevel));
                    } else {
                        // 0以下の場合、エンチャント消滅
                        ite.remove();
                    }
                }
                return;
            }
        }

        // ターゲットが発見出来ない(新規追加)
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort(ENCHANT_ID, (short) targetEnch.getEffectId(subTypeId));
        nbt.setShort(ENCHANT_LV, (short) addLevel);
        nbt.setBoolean(ENCHANT_ENABLE, true);
        nbttaglist.appendTag(nbt);
    }

    // バニラ互換用エンチャントリスト取得
    default NBTTagList getNBTTagEnchList(ItemStack stack) {
        if (!stack.getTagCompound().hasKey(TAG_ENCH, 9)) {
            stack.getTagCompound().setTag(TAG_ENCH, new NBTTagList());
        }
        return stack.getTagCompound().getTagList(TAG_ENCH, 10);
    }

    //追加特殊エンチャントリスト
    static NBTTagList getNBTTagSpenchList(ItemStack stack) {
        if (!stack.getTagCompound().hasKey(TAG_SPENCH, 9)) {
            stack.getTagCompound().setTag(TAG_SPENCH, new NBTTagList());
        }
        return stack.getTagCompound().getTagList(TAG_SPENCH, 10);
    }

    static List<NBTTagCompound> getSpenchList(ItemStack stack) {
        // NBTTagListとかいう糞、Listって名前ついてるのに、ListどころかCollectionもIteratorすら実装してないの?詐欺でしょ
        return ImmutableList.copyOf(ItemStackHelper.<NBTTagCompound> getNBTTagListIte(getNBTTagSpenchList(stack)));
    }

    static NBTTagCompound getEnchntNBT(ItemStack stack, EnchantBase base, int subTypeId) {
        if (stack.getItem() instanceof IBambooEnchantable) {
            IBambooEnchantable item = (IBambooEnchantable) stack.getItem();
            NBTTagList nbttaglist = getNBTTagSpenchList(stack);
            for (NBTTagCompound tag : ItemStackHelper.<NBTTagCompound> getNBTTagListIte(nbttaglist)) {
                short enchId = tag.getShort(ENCHANT_ID);
                if (EnchantBase.isEqual(base, enchId, subTypeId)) {
                    return tag;
                }
            }
        }
        return empty;
    }

    static short getEnchLevel(ItemStack stack, EnchantBase base, int subTypeId) {
        if (stack == null || base == null) {
            return 0;
        }
        NBTTagCompound nbt = getEnchntNBT(stack, base, subTypeId);
        return nbt != null ? nbt.getShort(ENCHANT_LV) : 0;
    }

    static boolean isEnchantEnable(ItemStack stack, EnchantBase base, int subTypeId) {
        NBTTagCompound nbt = getEnchntNBT(stack, base, subTypeId);
        return nbt != null ? nbt.getBoolean(ENCHANT_ENABLE) : false;
    }

    // GUI実装
    @Override
    @SideOnly(Side.CLIENT)
    default void exec(KeyBinding key) {
        EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        if (key.isPressed()) {
            player.openGui(BambooCore.instance, GuiHandler.GUI_PICKAXE_LV, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            PacketDispatcher.sendToServer(new MessageBambooUtil());
        }
    }

    @Override
    default void call(EntityPlayer player, ItemStack is, byte data) {
        player.openGui(BambooCore.instance, GuiHandler.GUI_PICKAXE_LV, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    static Long getRandSeed(ItemStack stack) {
        return (long) (stack.getDisplayName().hashCode());
    }
}
