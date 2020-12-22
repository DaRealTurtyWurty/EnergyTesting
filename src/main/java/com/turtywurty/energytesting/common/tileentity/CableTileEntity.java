package com.turtywurty.energytesting.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class CableTileEntity extends TileEntity implements IEnergyConsumer {

	private CustomEnergyStorage storage = new CustomEnergyStorage(this, Integer.MAX_VALUE, Integer.MAX_VALUE,
			Integer.MAX_VALUE, 0);
	private LazyOptional<CustomEnergyStorage> lazy = LazyOptional.of(() -> this.storage);

	public CableTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public CableTileEntity() {
		this(TileEntityTypeInit.CABLE.get());
	}

	public int getEnergyStored() {
		return this.storage.getEnergyStored();
	}

	public void setEnergyStored(int energyStored) {
		if (this.storage.getEnergyStored() != energyStored) {
			this.storage.setEnergy(energyStored);
			this.markDirty();
		}
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.storage.setEnergy(compound.getInt("EnergyStored"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("EnergyStored", this.storage.getEnergyStored());
		return compound;
	}

	@Override
	public CustomEnergyStorage getEnergyStorage() {
		return this.storage;
	}

	@Override
	public CustomEnergyStorage createEnergyStorage(int capacity, int maxReceive, int maxExtract, int defaultEnergy) {
		return new CustomEnergyStorage(this, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
	}

	@Override
	public LazyOptional<CustomEnergyStorage> getEnergyHandler() {
		return this.lazy;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityEnergy.ENERGY) {
			return this.lazy.cast();
		}
		return super.getCapability(cap, side);
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
