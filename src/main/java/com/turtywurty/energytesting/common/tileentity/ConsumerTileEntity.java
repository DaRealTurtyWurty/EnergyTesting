package com.turtywurty.energytesting.common.tileentity;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class ConsumerTileEntity extends InventoryTile implements IEnergyConsumer {

	private static final int ENERGY_PER_TICK = 50;
	private static final int MAX_ENERGY = 5000;
	private static final int MAX_INSERT = 10;
	private static final int MAX_EXTRACT = 10;

	protected LazyOptional<CustomEnergyStorage> energyHandler = LazyOptional
			.of(() -> this.createEnergyStorage(MAX_ENERGY, MAX_INSERT, MAX_EXTRACT, 0));

	public ConsumerTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 1);
	}

	public ConsumerTileEntity() {
		this(TileEntityTypeInit.CONSUMER.get());
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.world.isRemote) {
			if (!this.getItemInSlot(0).isEmpty()) {
				if (this.getItemInSlot(0).getCapability(CapabilityEnergy.ENERGY).isPresent()) {
					int energy = this.getItemInSlot(0).getCapability(CapabilityEnergy.ENERGY)
							.orElse(new EnergyStorage(0)).getEnergyStored();
					int leftEnergy = this.getEnergyStorage().receiveEnergy(energy, false);
					this.getItemInSlot(0).getCapability(CapabilityEnergy.ENERGY).orElse(new EnergyStorage(0))
							.extractEnergy(energy - leftEnergy, false);
				}
			}

			if (this.getEnergyStorage().getEnergyStored() >= ENERGY_PER_TICK) {
				this.getEnergyStorage().extractEnergy(ENERGY_PER_TICK, false);
				// Do Something here
				((ServerWorld) this.world).addLightningBolt(
						new LightningBoltEntity(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), false));
			}
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityEnergy.ENERGY) {
			return energyHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public LazyOptional<CustomEnergyStorage> getEnergyHandler() {
		return this.energyHandler;
	}

	@Override
	public CustomEnergyStorage createEnergyStorage(int capacity, int maxReceive, int maxExtract, int defaultEnergy) {
		return new CustomEnergyStorage(this, capacity, maxReceive, maxExtract, defaultEnergy);
	}

	@Override
	public CustomEnergyStorage getEnergyStorage() {
		return energyHandler.orElse(this.createEnergyStorage(MAX_ENERGY, MAX_INSERT, MAX_EXTRACT, 0));
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + EnergyTesting.MOD_ID + ".consumer");
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
}