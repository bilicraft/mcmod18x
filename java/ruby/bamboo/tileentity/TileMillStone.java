package ruby.bamboo.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ruby.bamboo.api.crafting.grind.IGrindRecipe;
import ruby.bamboo.block.MillStone;
import ruby.bamboo.crafting.GrindManager;

public class TileMillStone extends TileEntity implements ISidedInventory, ITickable {

    private static final int[] slots_top = new int[] { 0 };
    private static final int[] slots_bottom = new int[] { 2, 1 };
    private static final int[] slots_sides = new int[] { 0 };
    private ResourceLocation nowGrindItemName = Block.REGISTRY.getNameForObject(Blocks.AIR);
    private int nowGrindItemDmg;
    private float roll;
    private ItemStack[] slot = new ItemStack[3];
    private final static int MAX_PROGRESS = 3;
    public int grindTime;
    public int progress;
    public int grindMotion;
    public int isGrind;
    public static final int MAX_GRINDTIME = 400;

    public int getRoll() {
        return (int) roll;
    }

    public int isGrind() {
        return grindTime > 0 ? 1 : 0;
    }

    public int getProgress() {
        return getRatio(grindTime, MAX_GRINDTIME, MAX_PROGRESS);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void update() {
        byte magnification = 1;

        boolean flag1 = false;

        if (!worldObj.isRemote) {
            if (grindTime == 0 && canGrind()) {
                decrementSlot0();
                grindTime += magnification;
                updateMeta(magnification);
            } else if (grindTime > 0) {
                flag1 = true;
                grindTime += magnification;

                if (grindTime > MAX_GRINDTIME) {
                    grindTime = 0;
                    this.grindItem();
                }
                updateMeta(magnification);
            } else {
                updateMeta(0);
            }

            if (grindTime > 0) {
                grindMotion = grindTime % 40 / 10;
            } else {
                grindMotion = 0;
            }
        } else {
            if (getBlockMetadata() != 0) {
                this.roll = this.roll + getBlockMetadata() < 360 ? this.roll + getBlockMetadata() : 0;
                Item nowgi = Item.REGISTRY.getObject(this.nowGrindItemName);

                if (nowgi != null) {
                    itemCrackParticle(nowgi);
                }
            } else {
                roll = 0;
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private void updateMeta(int meta) {
        if (getBlockMetadata() != meta) {
            this.getWorld().setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(MillStone.META, meta), 3);
            this.getWorld().notifyBlockUpdate(pos, this.getWorld().getBlockState(this.getPos()), this.getWorld().getBlockState(this.getPos()), 3);
        }
    }

    private void itemCrackParticle(Item nowgi) {
        float pitch = 0;
        float yaw = this.worldObj.rand.nextFloat() * 20 - 1;

        for (int j = 0; j < 1; ++j) {
            Vec3d vec3d = new Vec3d(((double) this.getWorld().rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.rotatePitch(-pitch * (float) Math.PI / 180.0F);
            vec3d = vec3d.rotateYaw(-yaw * (float) Math.PI);
            double d0 = (double) (-this.getWorld().rand.nextFloat()) * 0.6D - 0.3D;
            Vec3d vec3d1 = new Vec3d(((double) this.getWorld().rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vec3d1 = vec3d1.rotatePitch(-pitch * (float) Math.PI / 180.0F);
            vec3d1 = vec3d1.rotateYaw(-yaw * (float) Math.PI);
            vec3d1 = vec3d1.addVector(this.getPos().getX() + 0.5F, this.getPos().getY() + 1F, this.getPos().getZ() + 0.5F);

            if (nowgi.getHasSubtypes()) {
                this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord - 0.15D, vec3d.zCoord, new int[] { Item.getIdFromItem(nowgi), nowGrindItemDmg });
            } else {
                this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord - 0.15D, vec3d.zCoord, new int[] { Item.getIdFromItem(nowgi) });
            }
        }

    }

    private int getRatio(float par0, float par1, int par3) {
        return Math.round(par0 / par1 * par3);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        this.slot = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.slot.length) {
                this.slot[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        if (nbt.hasKey("grindTime")) {
            grindTime = nbt.getInteger("grindTime");
        }
        if (nbt.hasKey("grindItemDmg")) {
            nowGrindItemDmg = nbt.getInteger("grindItemDmg");
        }
        if (nbt.hasKey("grindItemName")) {
            nowGrindItemName = new ResourceLocation(nbt.getString("grindItemName"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("grindTime", grindTime);
        nbt.setString("grindItemName", (nowGrindItemName == null ? Block.REGISTRY.getNameForObject(Blocks.AIR) : nowGrindItemName).toString());
        nbt.setInteger("grindItemDmg", nowGrindItemDmg);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.slot.length; ++i) {
            if (this.slot[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.slot[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);

        return nbt;
    }

    //    @Override
    //    public Packet getDescriptionPacket() {
    //        NBTTagCompound var1 = new NBTTagCompound();
    //        writeToNBT(var1);
    //        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    //    }
    //
    //    @Override
    //    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    //        this.readFromNBT(pkt.func_148857_g());
    //    }

    /**
     * grind実行可能か(出力欄に格納可能か)
     *
     * @return
     */
    private boolean canGrind() {
        if (this.slot[0] == null) {
            return false;
        } else {
            IGrindRecipe gr = GrindManager.getOutput(slot[0]);

            if (gr == null) {
                return false;
            }

            ItemStack output = gr.getOutput();
            ItemStack bonus = gr.getBonus();

            if (output == null) {
                return false;
            }

            // 出力先両方共存在しない
            if (this.slot[1] == null && this.slot[2] == null) {
                return true;
            }
            //slot2にボーナスが存在するが、ボーナスは存在しない
            if (this.slot[2] != null && bonus == null) {
                return false;
            }

            // 出力先いずれかが同一のものではない
            if ((this.slot[1] != null && !this.slot[1].isItemEqual(output)) || (this.slot[2] != null && !this.slot[2].isItemEqual(bonus))) {
                return false;
            }

            boolean isInRangeStackSize;
            int outResult = slot[1] != null ? slot[1].stackSize + output.stackSize : output.stackSize;
            isInRangeStackSize = outResult <= getInventoryStackLimit() && outResult <= output.getMaxStackSize();

            if (isInRangeStackSize && bonus != null) {
                int bonusResult = slot[2] != null ? slot[2].stackSize + bonus.stackSize : bonus.stackSize;
                isInRangeStackSize = bonusResult <= getInventoryStackLimit() && bonusResult <= bonus.getMaxStackSize();
            }

            return isInRangeStackSize;
        }
    }

    private void decrementSlot0() {
        nowGrindItemName = Item.REGISTRY.getNameForObject(slot[0].getItem());
        if (nowGrindItemName == null) {
            nowGrindItemName = Block.REGISTRY.getNameForObject(Blocks.AIR);
        }
        nowGrindItemDmg = slot[0].getItemDamage();
        this.slot[0].stackSize -= GrindManager.getOutput(slot[0]).getInput().getStackSize();

        if (this.slot[0].stackSize <= 0) {
            this.slot[0] = null;
        }
    }

    /**
     * 完了後、slotへ格納する
     */
    private void grindItem() {
        if (Item.REGISTRY.getObject(nowGrindItemName) != Item.getItemFromBlock(Blocks.AIR)) {
            IGrindRecipe gr = GrindManager.getOutput(new ItemStack((Item) Item.REGISTRY.getObject(nowGrindItemName), 64, nowGrindItemDmg));
            ItemStack output = gr.getOutput();
            ItemStack bonus = worldObj.rand.nextFloat() <= gr.getBonusWeight() ? gr.getBonus() : null;

            if (this.slot[1] == null) {
                this.slot[1] = output.copy();
            } else if (this.slot[1].isItemEqual(output)) {
                slot[1].stackSize += output.stackSize;
            }

            if (bonus != null) {
                if (this.slot[2] == null) {
                    this.slot[2] = bonus.copy();
                } else if (this.slot[2].isItemEqual(bonus)) {
                    slot[2].stackSize += bonus.stackSize;
                }
            }

            nowGrindItemName = Block.REGISTRY.getNameForObject(Blocks.AIR);
        }
    }

    @Override
    public int getSizeInventory() {
        return this.slot.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.slot[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.slot[index] != null) {
            ItemStack itemstack;

            if (this.slot[index].stackSize <= count) {
                itemstack = this.slot[index];
                this.slot[index] = null;
                return itemstack;
            } else {
                itemstack = this.slot[index].splitStack(count);

                if (this.slot[index].stackSize == 0) {
                    this.slot[index] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.slot[index] != null) {
            ItemStack itemstack = this.slot[index];
            this.slot[index] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.slot[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.getPos()) != this ? false : player.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;

    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 1 || index == 2 ? false : true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return "tile.MillStone";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? slots_bottom : (side == EnumFacing.UP ? slots_top : slots_sides);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return direction != EnumFacing.DOWN || index != 0;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        writeData(var1);
        return new SPacketUpdateTileEntity(this.getPos(), 5, var1);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readData(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        writeData(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        readData(tag);
    }

    private NBTTagCompound writeData(NBTTagCompound tag) {
        tag.setString("grindItemName", (nowGrindItemName == null ? Block.REGISTRY.getNameForObject(Blocks.AIR) : nowGrindItemName).toString());
        tag.setInteger("grindItemDmg", nowGrindItemDmg);
        return tag;
    }

    private void readData(NBTTagCompound tag) {
        if (tag.hasKey("grindItemDmg")) {
            nowGrindItemDmg = tag.getInteger("grindItemDmg");
        }
        if (tag.hasKey("grindItemName")) {
            nowGrindItemName = new ResourceLocation(tag.getString("grindItemName"));
        }
    }

}
