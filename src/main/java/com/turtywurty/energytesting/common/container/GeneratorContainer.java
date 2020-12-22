package com.turtywurty.energytesting.common.container;

import com.turtywurty.energytesting.common.container.syncdata.GeneratorSyncData;
import com.turtywurty.energytesting.common.tileentity.GeneratorTileEntity;
import com.turtywurty.energytesting.core.init.BlockInit;
import com.turtywurty.energytesting.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GeneratorContainer extends Container {

	private final IWorldPosCallable callable;
	public static GeneratorTileEntity tile;
	public final IIntArray data;

	public GeneratorContainer(int id, final PlayerInventory playerInventory, IItemHandler slots, BlockPos pos,
			IIntArray data) {
		super(ContainerTypeInit.GENERATOR.get(), id);
		this.callable = IWorldPosCallable.of(playerInventory.player.getEntityWorld(), pos);
		this.data = data;
		int startX = 8;
		int startY = 84;
		int slotSizePlus2 = 18;

		this.addSlot(new SlotItemHandler(slots, 0, 80, 35));

		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * slotSizePlus2),
						startY + (row * slotSizePlus2)));
			}
		}

		int hotbarY = 142;

		for (int column = 0; column < 9; column++) {
			this.addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
		}

		this.trackIntArray(data);
	}

	public static IContainerProvider getServerContainerProvider(GeneratorTileEntity te, BlockPos activationPos) {
		tile = te;
		return (id, playerInventory, serverPlayer) -> new GeneratorContainer(id, playerInventory, te.getInventory(),
				activationPos, new GeneratorSyncData(te));
	}

	public static GeneratorContainer getClientContainer(int id, PlayerInventory playerInventory) {
		return new GeneratorContainer(id, playerInventory, new ItemStackHandler(1), BlockPos.ZERO, new IntArray(1));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.callable, playerIn, BlockInit.GENERATOR.get());
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack returnStack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			returnStack = slotStack.copy();

			final int containerSlots = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(slotStack, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}
			if (slotStack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (slotStack.getCount() == returnStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, slotStack);
		}
		return returnStack;
	}

	@OnlyIn(Dist.CLIENT)
	public int getStoredEnergy() {
		return tile.getEnergyStorage().getEnergyStored() != 0 && tile.getEnergyStorage().getMaxEnergyStored() != 0
				? tile.getEnergyStorage().getEnergyStored() * 24 / tile.getEnergyStorage().getMaxEnergyStored()
				: 0;
	}
}
