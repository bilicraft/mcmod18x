package ruby.bamboo.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruby.bamboo.block.Campfire;
import ruby.bamboo.crafting.CookingManager;
import ruby.bamboo.crafting.CookingManager.RecipeEntry;

public class TileCampfire extends TileEntity implements ITickable, ISidedInventory {

    public enum BakeType {
        NONE,
        ATHER,
        MEAT,
        FISH;
    }

    private static final byte SLOT_FUEL = 9;
    private static final byte SLOT_RESULT = 10;
    private static final int[] slotsTop = new int[] { 0 };
    private static final int[] slotsBottom = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, SLOT_FUEL, SLOT_RESULT };
    private static final int[] slotsSides = new int[] { SLOT_FUEL };
    //0～8:クラフトスロット,9:燃料スロット,10:結果スロット
    private ItemStack[] slots = new ItemStack[11];
    private ItemStack[] copyMatrix = new ItemStack[9];
    private static final int MAX_FUEL = 102400;
    private int fuel;
    private int maxCookTime = 200;
    private int cookTime = 200;
    private boolean isBurn = false;
    private RecipeEntry entry;
    public ItemStack nowCookingResult;
    //クライアント側
    public int fuelRatio;
    public int cookRatio = 100;
    private int meatroll;
    private BakeType nowBakeType = BakeType.NONE;

    @Override
    public void update() {
        updateFuel();
        updateCooking();
        updateRender();
    }

    private void updateFuel() {

        if (!this.getWorld().isRemote) {
            if (TileEntityFurnace.isItemFuel(slots[SLOT_FUEL])) {
                int slotFuel = TileEntityFurnace.getItemBurnTime(slots[SLOT_FUEL]);
                if (fuel + slotFuel <= MAX_FUEL) {
                    fuel += slotFuel;
                    if (--slots[SLOT_FUEL].stackSize == 0) {
                        slots[SLOT_FUEL] = slots[SLOT_FUEL].getItem().getContainerItem(slots[SLOT_FUEL]);
                    }

                }
            }
        }
    }

    private void updateCooking() {
        if (!this.getWorld().isRemote) {
            if (!isBurn && !isEmpty()) {
                if (slots[SLOT_RESULT] != null && slots[SLOT_RESULT].stackSize == slots[SLOT_RESULT].getMaxStackSize()) {
                    return;
                }
                if (this.canCooking() && entry.getFuelCost() <= fuel) {
                    if (nowCookingResult != null) {
                        if (slots[SLOT_RESULT] == null) {
                            isBurn = true;
                            maxCookTime = cookTime = getCookTime();
                            this.setMatrix();
                        } else if (nowCookingResult.isItemEqual(slots[SLOT_RESULT]) && slots[SLOT_RESULT].stackSize + nowCookingResult.stackSize <= slots[SLOT_RESULT].getMaxStackSize()) {
                            isBurn = true;
                            maxCookTime = cookTime = getCookTime();
                            this.setMatrix();
                        }
                    }
                }
            } else {
                updateBurn();
            }
        }

    }

    private void updateBurn() {
        if (!this.getWorld().isRemote) {
            if (isBurn) {
                if (--cookTime <= 0) {
                    if (nowCookingResult != null) {
                        RecipeEntry entry = CookingManager.getInstance().findMatchingRecipe(slots, this.getWorld());
                        ItemStack nowMatrix = entry.getItemStack();
                        if (nowMatrix != null && nowMatrix.isItemEqual(nowCookingResult)) {
                            if (slots[SLOT_RESULT] == null) {
                                materialConsumption(entry);
                                slots[SLOT_RESULT] = nowCookingResult.copy();
                            } else if (nowCookingResult.isItemEqual(slots[SLOT_RESULT]) && slots[SLOT_RESULT].stackSize + nowCookingResult.stackSize <= slots[SLOT_RESULT].getMaxStackSize()) {
                                materialConsumption(entry);
                                slots[SLOT_RESULT].stackSize += nowCookingResult.stackSize;
                            }
                            this.markDirty();
                        }
                    }
                    nowCookingResult = null;
                    isBurn = false;
                    maxCookTime = cookTime = getCookTime();
                }
                if ((cookTime & 1) == 0) {
                    if (!this.chkMtrix()) {
                        nowCookingResult = null;
                        isBurn = false;
                        maxCookTime = cookTime = 200;
                    }

                }
                if (cookTime % 100 == 0) {
                    this.markDirty();
                    this.getWorld().notifyBlockUpdate(pos, this.getWorld().getBlockState(this.getPos()), this.getWorld().getBlockState(this.getPos()), 7);
                }
            }
        }
    }

    void updateRender() {
        meatroll = meatroll < 360 ? ++meatroll : 0;
        if (!worldObj.isRemote) {
            BakeType type = BakeType.NONE;
            if (getStackInSlot(SLOT_RESULT) != null) {
                if (getStackInSlot(SLOT_RESULT).getItem() == Items.COOKED_FISH) {
                    type = BakeType.FISH;
                } else if (getStackInSlot(SLOT_RESULT).getItem() == Items.COOKED_PORKCHOP || getStackInSlot(SLOT_RESULT).getItem() == Items.COOKED_BEEF) {
                    type = BakeType.MEAT;
                } else {
                    type = BakeType.ATHER;
                }
            }

            if (nowBakeType != type) {
                nowBakeType = type;
                this.getWorld().notifyBlockUpdate(pos, this.getWorld().getBlockState(this.getPos()), this.getWorld().getBlockState(this.getPos()), 3);
            }
            //                this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, (meta << 2) | (getBlockMetadata() & 3), 3);
        }

    }

    private int getCookTime() {
        return entry.getTotalCookTime();
    }

    private boolean isEmpty() {
        for (int i = 0; i < 9; i++) {
            if (slots[i] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean canCooking() {
        this.entry = CookingManager.getInstance().findMatchingRecipe(slots, this.getWorld());
        return entry != null && (nowCookingResult = entry.getItemStack()) != null;
    }

    private void setMatrix() {
        for (int i = 0; i < 9; i++) {
            copyMatrix[i] = getStackInSlot(i);
        }
    }

    private boolean chkMtrix() {
        for (int i = 0; i < 9; i++) {
            if (copyMatrix[i] != slots[i]) {
                return false;
            }
        }
        return true;
    }

    private void materialConsumption(RecipeEntry entry) {
        for (int i = 0; i < 9; i++) {
            if (slots[i] != null) {
                if (--slots[i].stackSize == 0) {
                    slots[i] = slots[i].getItem().getContainerItem(slots[i]);
                }
            }
        }
        fuel -= entry.getFuelCost();
    }

    public BakeType getBakeType() {
        return nowBakeType;
    }

    public float getRotate() {
        switch (this.getWorld().getBlockState(this.getPos()).getValue(Campfire.FACING)) {
            case NORTH:
                return 0;
            case EAST:
                return 270;
            case SOUTH:
                return 180;
            case WEST:
                return 90;
            default:
                return 0;
        }
    }

    public int getMeatroll() {
        return meatroll;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        fuel = nbt.getInteger("fuel");
        cookTime = nbt.getInteger("cookTime");
        maxCookTime = nbt.getInteger("maxCookTime");
        if (nbt.hasKey("nowItem")) {
            nowCookingResult = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt.getTag("nowItem"));
        }
        NBTTagList nbtList = new NBTTagList();
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        for (int i = 0; i < slots.length && i < list.tagCount(); i++) {
            NBTTagCompound nbtCompound = list.getCompoundTagAt(i);
            if (nbtCompound.hasKey("itemNBT")) {
                slots[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbtCompound.getTag("itemNBT"));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("fuel", fuel);
        nbt.setInteger("cookTime", cookTime);
        nbt.setInteger("maxCookTime", maxCookTime);
        if (nowCookingResult != null) {
            nbt.setTag("nowItem", nowCookingResult.writeToNBT(new NBTTagCompound()));
        }
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        NBTTagCompound nbtCompound;
        for (int i = 0; i < slots.length; i++) {
            nbtCompound = new NBTTagCompound();
            if (slots[i] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                slots[i].writeToNBT(itemCompound);
                nbtCompound.setTag("itemNBT", itemCompound);
            }
            list.appendTag(nbtCompound);
        }
        return nbt;
    }

    public ItemStack[] getInventorySlots() {
        return slots;
    }

    // ISidedInventory

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return fuel;
            case 1:
                return maxCookTime;
            case 2:
                return cookTime;
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {

        switch (id) {
            case 0:
                fuel = value;
                break;
            case 1:
                maxCookTime = value;
                break;
            case 2:
                cookTime = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getSizeInventory() {
        return this.slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.slots[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        ItemStack itemstack = null;
        if (this.slots[var1] != null) {
            if (this.slots[var1].stackSize <= var2) {
                itemstack = this.slots[var1];
                this.slots[var1] = null;
            } else {
                itemstack = this.slots[var1].splitStack(var2);

                if (this.slots[var1].stackSize == 0) {
                    this.slots[var1] = null;
                }
            }
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int var1) {
        if (this.slots[var1] != null) {
            ItemStack itemstack = this.slots[var1];
            this.slots[var1] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.slots[var1] = var2;
        if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
            var2.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getName() {
        return "bamboo.container.campfire";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return this.worldObj.getTileEntity(this.getPos()) != this ? false : var1.getDistanceSq(this.getPos()) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return var1 == 2 ? false : (var1 == 9 ? TileEntityFurnace.isItemFuel(var2) : true);

    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return stack.getItem() == Items.BUCKET || index == 10;
    }

    public int getCookAmount() {
        return this.getRatio(cookTime, maxCookTime, 100);
    }

    public int getFuelAmount() {
        return this.getRatio(fuel, MAX_FUEL, 100);
    }

    private int getRatio(float par0, float par1, int par3) {
        return Math.round(par0 / par1 * par3);
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
        tag.setString("bakeType", nowBakeType.name());
        tag.setInteger("time", cookTime);
        tag.setInteger("maxtime", maxCookTime);
        return tag;
    }

    private void readData(NBTTagCompound tag) {
        String str = tag.getString("bakeType");
        if (!str.isEmpty()) {
            nowBakeType = BakeType.valueOf(str);
        }
        cookTime = tag.getInteger("time");
        maxCookTime = tag.getInteger("maxTIme");
    }

}
