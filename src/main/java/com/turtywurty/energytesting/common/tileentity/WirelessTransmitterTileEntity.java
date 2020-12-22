package com.turtywurty.energytesting.common.tileentity;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.common.blocks.WirelessBlock;
import com.turtywurty.energytesting.common.items.MarkerItem;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.CapabilityEnergy;

public class WirelessTransmitterTileEntity extends InventoryTile {

	private BlockPos recieverPos;
	private int transmitAmount;

	public WirelessTransmitterTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 1);
	}

	public WirelessTransmitterTileEntity() {
		this(TileEntityTypeInit.WIRELESS_TRANSMITTER.get());
	}

	@Override
	public void tick() {
		super.tick();
		this.updateTransmitAmount();
		
		if (!this.world.isRemote) {
			if (this.getItemInSlot(0).getItem() instanceof MarkerItem) {
				if (this.getItemInSlot(0).getOrCreateTag().contains("RecieverPos")) {
					CompoundNBT nbt = this.getItemInSlot(0).getOrCreateTag().getCompound("RecieverPos");
					int x = nbt.getInt("X");
					int y = nbt.getInt("Y");
					int z = nbt.getInt("Z");
					BlockPos position = new BlockPos(x, y, z);
					if (this.world.getTileEntity(position) instanceof WirelessRecieverTileEntity) {
						WirelessRecieverTileEntity reciever = (WirelessRecieverTileEntity) this.world
								.getTileEntity(position);
						if (reciever.hasValidExitPoint()) {
							reciever.setTransferAmount(this.transmitAmount);
							this.transmitAmount = 0;
						}
					}
				}
			}
		}
	}

	public void updateTransmitAmount() {
		TileEntity tile = this.world
				.getTileEntity(this.pos.offset(this.getBlockState().get(WirelessBlock.FACING).getOpposite()));
		if (tile != null) {
			tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
				if (energy.canExtract()) {
					this.transmitAmount = energy.extractEnergy(energy.getEnergyStored(), false);
				}
			});
		}
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + EnergyTesting.MOD_ID + ".wireless_transmitter");
	}
}
