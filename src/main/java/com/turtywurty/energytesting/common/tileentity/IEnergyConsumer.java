package com.turtywurty.energytesting.common.tileentity;

import net.minecraftforge.common.util.LazyOptional;

public interface IEnergyConsumer {
	public CustomEnergyStorage getEnergyStorage();
	
	public CustomEnergyStorage createEnergyStorage(int capacity, int maxReceive, int maxExtract, int defaultEnergy);
	
	public LazyOptional<CustomEnergyStorage> getEnergyHandler();
}
