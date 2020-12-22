package com.turtywurty.energytesting.common.container.syncdata;

import com.turtywurty.energytesting.common.tileentity.GeneratorTileEntity;

import net.minecraft.util.IIntArray;

public class GeneratorSyncData implements IIntArray {

	private final GeneratorTileEntity te;

	public GeneratorSyncData(GeneratorTileEntity te) {
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
