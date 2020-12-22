package com.turtywurty.energytesting.core.caps;

import com.turtywurty.energytesting.common.items.EnergyItem;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyItemCap<NBT extends INBT> implements ICapabilityProvider, INBTSerializable<NBT> {

	private int energy;
	private EnergyItem item;
	private final LazyOptional<IEnergyStorage> lazy;

	public ForgeEnergyItemCap(EnergyItem itemIn) {
		this.item = itemIn;
		this.lazy = LazyOptional.of(() -> new EnergyStorage(this.item.getMaxEnergyStored(), this.item.getMaxReceive(),
				this.item.getMaxExtract(), this.item.getDefaultEnergy()));
	}

	@Override
	public NBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("Energy", this.energy);
		return (NBT) nbt;
	}

	@Override
	public void deserializeNBT(NBT nbt) {
		if (nbt instanceof CompoundNBT) {
			this.energy = ((CompoundNBT) nbt).getInt("Energy");
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return CapabilityEnergy.ENERGY.orEmpty(cap, this.lazy);
	}
}
