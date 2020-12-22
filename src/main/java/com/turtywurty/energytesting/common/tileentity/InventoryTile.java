package com.turtywurty.energytesting.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryTile extends TileEntity implements ITickableTileEntity {

	public final int size;
	public int timer;
	public boolean requiresUpdate = true;

	protected LazyOptional<IItemHandlerModifiable> handler = LazyOptional.of(this::createHandler);

	public InventoryTile(TileEntityType<?> tileEntityTypeIn, int size) {
		super(tileEntityTypeIn);
		this.size = size;
	}

	@Override
	public void tick() {
		timer++;
		if (world != null) {
			if (requiresUpdate) {
				updateTile();
				requiresUpdate = false;
			}
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}

	public LazyOptional<IItemHandlerModifiable> getHandler() {
		return handler;
	}

	public IItemHandlerModifiable getInventory() {
		return handler.orElse(createHandler());
	}

	public IItemHandlerModifiable createHandler() {
		return new ItemStackHandler(size) {
			@Override
			protected void onContentsChanged(int slot) {
				markDirty();
			}
		};
	}

	public ItemStack getItemInSlot(int slot) {
		return handler.map(inventory -> inventory.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
	}

	public ItemStack insertItem(int slot, ItemStack stack) {
		ItemStack itemIn = stack.copy();
		stack.shrink(itemIn.getCount());
		requiresUpdate = true;
		return handler.map(inventory -> inventory.insertItem(slot, itemIn, false)).orElse(ItemStack.EMPTY);
	}

	public ItemStack extractItem(int slot) {
		int count = getItemInSlot(slot).getCount();
		requiresUpdate = true;
		return handler.map(inventory -> inventory.extractItem(slot, count, false)).orElse(ItemStack.EMPTY);
	}

	public int getSize() {
		return size;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		ListNBT list = compound.getList("Items", 10);
		for (int x = 0; x < list.size(); ++x) {
			CompoundNBT nbt = list.getCompound(x);
			int r = nbt.getByte("Slot") & 255;
			handler.ifPresent(inventory -> {
				int invslots = inventory.getSlots();
				if (r >= 0 && r < invslots) {
					inventory.setStackInSlot(r, ItemStack.read(nbt));
				}
			});
		}
		requiresUpdate = true;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		ListNBT list = new ListNBT();
		handler.ifPresent(inventory -> {
			int slots = inventory.getSlots();
			for (int x = 0; x < slots; ++x) {
				ItemStack stack = inventory.getStackInSlot(x);
				if (!stack.isEmpty()) {
					CompoundNBT nbt = new CompoundNBT();
					nbt.putByte("Slot", (byte) x);
					stack.write(nbt);
					list.add(nbt);
				}
			}
		});
		if (!list.isEmpty()) {
			compound.put("Items", list);
		}
		return compound;
	}

	public void updateTile() {
		requestModelDataUpdate();
		this.markDirty();
		if (this.getWorld() != null) {
			this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	@Nonnull
	public CompoundNBT getUpdateTag() {
		return this.serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.deserializeNBT(tag);
	}
}
