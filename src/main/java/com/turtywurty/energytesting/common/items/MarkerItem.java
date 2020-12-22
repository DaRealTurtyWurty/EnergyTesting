package com.turtywurty.energytesting.common.items;

import com.turtywurty.energytesting.common.tileentity.WirelessRecieverTileEntity;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MarkerItem extends Item {

	public MarkerItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		TileEntity tile = context.getWorld().getTileEntity(context.getPos());
		if (tile instanceof WirelessRecieverTileEntity && context.getPlayer().isSneaking()) {
			context.getItem().getOrCreateTag().put("RecieverPos", NBTUtil.writeBlockPos(context.getPos()));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getOrCreateTag().contains("RecieverPos")) {
			CompoundNBT nbt = stack.getOrCreateTag().getCompound("RecieverPos");
			int x = nbt.getInt("X");
			int y = nbt.getInt("Y");
			int z = nbt.getInt("Z");
			BlockPos pos = new BlockPos(x, y, z);
			if (!(worldIn.getTileEntity(pos) instanceof WirelessRecieverTileEntity)) {
				stack.getOrCreateTag().remove("RecieverPos");
			}
		}
	}
}
