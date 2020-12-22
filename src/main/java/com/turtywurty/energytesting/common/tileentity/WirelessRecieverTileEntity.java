package com.turtywurty.energytesting.common.tileentity;

import javax.annotation.Nullable;

import com.turtywurty.energytesting.common.blocks.WirelessBlock;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.energy.CapabilityEnergy;

public class WirelessRecieverTileEntity extends TileEntity implements ITickableTileEntity {

	private boolean valid = false;
	private int transmitAmount = 0;

	public WirelessRecieverTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public WirelessRecieverTileEntity() {
		this(TileEntityTypeInit.WIRELESS_RECIEVER.get());
	}

	@Override
	public void tick() {
		if (!this.world.isRemote) {
			if (this.getCurrentTransferAmount() > 0) {
				TileEntity tile = this.world
						.getTileEntity(this.pos.offset(this.getBlockState().get(WirelessBlock.FACING).getOpposite()));
				if (tile instanceof IEnergyConsumer) {
					((IEnergyConsumer) tile).getEnergyStorage().receiveEnergy(this.getCurrentTransferAmount(), false);
				} else if (tile != null) {
					if (tile.getCapability(CapabilityEnergy.ENERGY) != null) {
						tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
							storage.receiveEnergy(this.getCurrentTransferAmount(), false);
						});
					}
				} else {
					return;
				}
				this.setTransferAmount(0);
			}
		}
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.transmitAmount = compound.getInt("TransmitAmount");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("TransmitAmount", this.transmitAmount);
		return compound;
	}

	public boolean hasValidExitPoint() {
		@Nullable
		TileEntity tile = this.world
				.getTileEntity(this.pos.offset(this.getBlockState().get(WirelessBlock.FACING).getOpposite()));
		if (tile instanceof IEnergyConsumer) {
			this.valid = true;
		} else if (tile.getCapability(CapabilityEnergy.ENERGY) != null) {
			tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
				if (energy.canReceive()) {
					this.valid = true;
				} else {
					this.valid = false;
				}
			});
		}
		return this.valid;
	}

	public void setTransferAmount(int amount) {
		this.transmitAmount = amount;
	}

	public int getCurrentTransferAmount() {
		return this.transmitAmount;
	}
}
