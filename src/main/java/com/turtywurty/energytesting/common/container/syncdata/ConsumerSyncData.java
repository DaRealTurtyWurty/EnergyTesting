package com.turtywurty.energytesting.common.container.syncdata;

import com.turtywurty.energytesting.common.tileentity.ConsumerTileEntity;

import net.minecraft.util.IIntArray;

public class ConsumerSyncData implements IIntArray {

	private final ConsumerTileEntity te;

	public ConsumerSyncData(ConsumerTileEntity te) {
		this.te = te;
	}

	@Override
	public int get(int index) {
		switch (index) {
		case 0:
			return this.te.getEnergyStorage().getEnergyStored();
		default:
			return 0;
		}
	}

	@Override
	public void set(int index, int value) {
		switch (index) {
		case 0:
			this.te.getEnergyStorage().setEnergy(value);
			break;
		default:
			break;
		}
	}

	@Override
	public int size() {
		return 1;
	}
}
