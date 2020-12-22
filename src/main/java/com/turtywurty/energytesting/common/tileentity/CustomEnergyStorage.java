package com.turtywurty.energytesting.common.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

	private TileEntity tile;

	public CustomEnergyStorage(TileEntity tile, int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
		this.tile = tile;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		tile.markDirty();
		return super.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		tile.markDirty();
		return super.receiveEnergy(maxReceive, simulate);
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}
}