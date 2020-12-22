package com.turtywurty.energytesting.common.blocks;

import com.turtywurty.energytesting.common.tileentity.EnergyStorageTileEntity;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class EnergyStorageBlock extends Block {

	public EnergyStorageBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.ENERGY_STORAGE.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.getTileEntity(pos) instanceof EnergyStorageTileEntity) {
			EnergyStorageTileEntity tile = (EnergyStorageTileEntity) worldIn.getTileEntity(pos);
			player.sendStatusMessage(new StringTextComponent(String.valueOf(tile.getEnergyStorage().getEnergyStored())),
					true);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.SUCCESS;
	}
}
