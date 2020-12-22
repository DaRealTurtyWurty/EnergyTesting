package com.turtywurty.energytesting.common.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyItem extends Item {

	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	private EnergyItem(Properties properties) {
		super(properties);
	}

	public EnergyItem(Properties properties, int capacityIn, int maxRecieveIn, int maxExtractIn, int defaultEnergyIn) {
		this(properties);
		this.capacity = capacityIn;
		this.maxExtract = maxExtractIn;
		this.maxReceive = maxRecieveIn;
		this.energy = defaultEnergyIn;
	}

	public EnergyItem(Properties properties, int capacityIn, int maxRecieveIn, int maxExtractIn) {
		this(properties, capacityIn, maxRecieveIn, maxExtractIn, capacityIn);
	}

	public EnergyItem(Properties properties, int capacityIn, int maxRecieveIn) {
		this(properties, capacityIn, maxRecieveIn, capacityIn, capacityIn);
	}

	public EnergyItem(Properties properties, int capacityIn) {
		this(properties, capacityIn, capacityIn, capacityIn, capacityIn);
	}

	public int getStoredEnergy(ItemStack stack) {
		return stack.getCapability(CapabilityEnergy.ENERGY)
				.orElse(new EnergyStorage(this.capacity, this.maxReceive, this.maxExtract, this.energy))
				.getEnergyStored();
	}	

	public int getMaxEnergyStored() {
		return this.capacity;
	}

	public int getMaxExtract() {
		return this.maxExtract;
	}

	public int getMaxReceive() {
		return this.maxReceive;
	}

	public int getDefaultEnergy() {
		return this.energy;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
			tooltip.add(new StringTextComponent(energy.getEnergyStored() + "/" + energy.getMaxEnergyStored()));
		});
	}
}
