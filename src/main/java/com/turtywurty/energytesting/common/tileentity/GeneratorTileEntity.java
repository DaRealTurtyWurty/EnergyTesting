package com.turtywurty.energytesting.common.tileentity;

import javax.annotation.Nullable;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class GeneratorTileEntity extends InventoryTile {

	protected LazyOptional<CustomEnergyStorage> energyHandler = LazyOptional
			.of(() -> this.createEnergyStorage(1000, 100000, 50, 0));

	private int currentEnergy, stackEnergy;

	public GeneratorTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 1);
	}

	public GeneratorTileEntity() {
		this(TileEntityTypeInit.GENERATOR.get());
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.world.isRemote) {
			if (!this.getItemInSlot(0).isEmpty()) {
				this.stackEnergy = this.getEnergyForStack(this.getItemInSlot(0));
				if (this.currentEnergy < this.stackEnergy) {
					this.currentEnergy += this.getEnergyStorage().receiveEnergy(this.stackEnergy, false);
				} else if (this.currentEnergy >= this.stackEnergy) {
					this.currentEnergy = 0;
					this.stackEnergy = 0;
					this.getItemInSlot(0).shrink(1);
				}
			}
		}

		if (this.getEnergyStorage().getEnergyStored() >= 50 && this.getEnergyStorage().canExtract()) {
			this.outputEnergy();
		}
	}

	public int getEnergyForStack(ItemStack stack) {
		return ForgeHooks.getBurnTime(stack);
	}

	public void outputEnergy() {
		for (Direction dir : Direction.values()) {
			@Nullable
			TileEntity tile = this.world.getTileEntity(this.pos.offset(dir));

			if(tile != null) {
				tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
					if (storage.canReceive() && storage.getEnergyStored() < storage.getMaxEnergyStored()) {
						storage.receiveEnergy(this.getEnergyStorage().extractEnergy(50, false), false);
					}
				});
			}
		}
	}

	// irrit but also pog
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityEnergy.ENERGY) {
			return energyHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	public LazyOptional<CustomEnergyStorage> getEnergyHandler() {
		return this.energyHandler;
	}

	public CustomEnergyStorage createEnergyStorage(int capacity, int maxReceive, int maxExtract, int defaultEnergy) {
		return new CustomEnergyStorage(this, capacity, maxReceive, maxExtract, defaultEnergy);
	}

	public CustomEnergyStorage getEnergyStorage() {
		return energyHandler.orElse(this.createEnergyStorage(1000, 10, 10, 0));
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + EnergyTesting.MOD_ID + ".generator");
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
