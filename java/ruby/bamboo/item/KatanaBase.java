package ruby.bamboo.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruby.bamboo.api.katana.KatanaDropManager;

public abstract class KatanaBase extends ItemSword {

    private static final List<KatanaBase> katanaList = Lists.newArrayList();

    public KatanaBase() {
        super(ToolMaterial.IRON);
        this.setMaxDamage(150);
        this.setNoRepair();
        katanaList.add(this);
    }

    @Override
    public float getDamageVsEntity() {
        return 0;
    }

    public abstract float getDamageVsEntity(ItemStack stack, Entity entity);

    public float getDropRate() {
        return 0;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), getDamageVsEntity(stack, entity));
        if (!entity.worldObj.isRemote) {
            if (KatanaDropManager.isDropableEntity(entity.getClass())) {
                if (entity.hurtResistantTime == ((EntityLivingBase) entity).maxHurtResistantTime) {
                    if (((EntityLivingBase) entity).getHealth() <= 0) {
                        //                        ItemStack drop = KatanaDropManager.getRandomDrop(EntityList.getClassFromID(EntityList.getEntityID(entity)), player.getEntityWorld().rand);
                        ItemStack drop = KatanaDropManager.getRandomDropItem(entity.getClass(), itemRand, getDropRate());
                        if (drop != null) {
                            entity.entityDropItem(drop, 0.0F);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        for(KatanaBase e:katanaList){
            playerIn.getCooldownTracker().setCooldown(e, 60);
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }
}
