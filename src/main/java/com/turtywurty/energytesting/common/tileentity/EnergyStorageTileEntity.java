package com.turtywurty.energytesting.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class EnergyStorageTileEntity extends TileEntity implements ITickableTileEntity {

	public EnergyStorageTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public EnergyStorageTileEntity() {
		this(TileEntityTypeInit.ENERGY_STORAGE.get());
	}

	public int timer;
	public boolean requiresUpdate = true;

	protected LazyOptional<CustomEnergyStorage> energyHandler = LazyOptional
			.of(() -> this.createEnergyStorage(10000, 500, 500, 0));

	@Override
	public void tick() {
		timer++;
		if (world != null) {
			if (requiresUpdate) {
				updateTile();
				requiresUpdate = false;
			}
		}

		if (this.getEnergyStorage().getEnergyStored() > 0) {
			outputEnergy();
		}
	}

	public void outputEnergy() {
		for (Direction dir : Direction.values()) {
			@Nullable
			TileEntity tile = this.world.getTileEntity(this.pos.offset(dir));

			if (tile instanceof IEnergyConsumer) {
				IEnergyConsumer storage = (IEnergyConsumer) tile;
				if (this.getEnergyStorage().canExtract() && storage.getEnergyStorage().canReceive()) {
					storage.getEnergyStorage().receiveEnergy(this.getEnergyStorage().extractEnergy(500, false), false);
				}
			}
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityEnergy.ENERGY) {
			return energyHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	public LazyOptional<CustomEnergyStorage> getEnergyHandler() {
		return this.energyHandler;
	}

	public CustomEnergyStorage getEnergyStorage() {
		return energyHandler.orElse(this.createEnergyStorage(10000, 500, 500, 0));
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + EnergyTesting.MOD_ID + ".energy_storage");
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.getEnergyStorage().setEnergy(compound.getInt("EnergyStored"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("EnergyStored", this.getEnergyStorage().getEnergyStored());
		return compound;
	}

	public CustomEnergyStorage createEnergyStorage(int capacity, int maxReceive, int maxExtract, int defaultEnergy) {
		return new CustomEnergyStorage(this, capacity, maxReceive, maxExtract, defaultEnergy);
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

	@Override
	public void markDirty() {
		this.requestModelDataUpdate();
		super.markDirty();
		if (this.getWorld() != null) {
			this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
		}
	}
}
