package ruby.bamboo.item;

import static ruby.bamboo.enchant.EnchantConstants.*;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.enchant.Curse;
import ruby.bamboo.enchant.EnchantBase;
import ruby.bamboo.enchant.IBambooEnchantable;
import ruby.bamboo.enchant.SpecialEnchant;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class BambooPickaxe extends ItemPickaxe implements IBambooEnchantable {

    public BambooPickaxe() {
        super(ToolMaterial.EMERALD);

    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!attacker.getEntityWorld().isRemote) {
            this.addExpChance(stack, target);
        }
        SpecialEnchant.onAttackEntity(stack, target, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
        if (!worldIn.isRemote) {
            this.addExpChance(stack, worldIn, blockIn, pos);
        }
        SpecialEnchant.onBreakBlock(stack, worldIn, blockIn, pos, playerIn, getMovingObjectPositionFromPlayer(worldIn, (EntityPlayer) playerIn, false));
        return true;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return super.getToolClasses(stack);
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
        return true;
    }

    @Override
    public float getDigSpeed(ItemStack stack, IBlockState state) {
        float base = 1.5F;
        // ダイヤで8、こちらのほうがgetStrより優先？
        return state.getBlock().getMaterial() == Material.rock ? base * 2 : base;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        // ダイヤで8
        return 8;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 32767;
    }

    // マウス合わせた時の情報
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        String name = stack.getDisplayName();
        tooltip.set(0, tooltip.get(0).replaceFirst(Pattern.quote(name), this.getPrefix(stack) + name + this.getSuffix(stack)));
        tooltip.addAll(IBambooEnchantable.getSpenchList(stack).stream().map(e -> {
            EnchantBase base = SpecialEnchant.getEnchantmentById(e.getShort(ENCHANT_ID));
            StringBuilder stb = new StringBuilder();
            // 種類増えることはないと思うけども。
            boolean isCurse = base instanceof Curse;
            stb.append("§").append(isCurse ? "4" : "b").append("§l");
            stb.append(base.getName(stack, EnchantBase.getSubID(e.getShort(ENCHANT_ID))));
            stb.append(" ").append(!isCurse ? "§w" : "");
            stb.append(base.getMaxLevel() > 1 ? IBambooEnchantable.getDisplayEnchantStrength(e.getShort(ENCHANT_LV)) : "");
            return stb.toString();
        }).collect(Collectors.toList()));
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }

    // 手持ち切替時のハイライト表示
    @Override
    public String getHighlightTip(ItemStack stack, String displayName) {
        return this.getPrefix(stack) + super.getHighlightTip(stack, displayName) + this.getSuffix(stack);
    }

    // NBT操作系
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        IBambooEnchantable.createNBT(stack);
        return null;
    }

    // 設定
    // 本によるエンチャント
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    // ゲージは0だと満タン、1だと空っぽになる
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - IBambooEnchantable.getExpP(stack);
    }

    //つねにぬめぬめ表示
    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    //常にバーを表示
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    // 壊れないため修理は不可
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    // なんでも採取は可能
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return 3;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.areItemsEqual(oldStack, newStack);
    }

}
