package com.turtywurty.energytesting.common.blocks;

import com.turtywurty.energytesting.common.tileentity.CableTileEntity;
import com.turtywurty.energytesting.core.init.BlockInit;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class CableBlock extends SixWayBlock {

	public CableBlock(Properties properties) {
		super(0.3125F, properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.valueOf(false))
				.with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false))
				.with(WEST, Boolean.valueOf(false)).with(UP, Boolean.valueOf(false))
				.with(DOWN, Boolean.valueOf(false)));
	}

	public BlockState makeConnections(IBlockReader blockReader, BlockPos pos) {
		Block block = blockReader.getBlockState(pos.down()).getBlock();
		Block block1 = blockReader.getBlockState(pos.up()).getBlock();
		Block block2 = blockReader.getBlockState(pos.north()).getBlock();
		Block block3 = blockReader.getBlockState(pos.east()).getBlock();
		Block block4 = blockReader.getBlockState(pos.south()).getBlock();
		Block block5 = blockReader.getBlockState(pos.west()).getBlock();
		return this.getDefaultState().with(DOWN, Boolean.valueOf(block == this || block == BlockInit.CABLE.get()))
				.with(UP, Boolean.valueOf(block1 == this || block1 == BlockInit.CABLE.get()))
				.with(NORTH, Boolean.valueOf(block2 == this || block2 == BlockInit.CABLE.get()))
				.with(EAST, Boolean.valueOf(block3 == this || block3 == BlockInit.CABLE.get()))
				.with(SOUTH, Boolean.valueOf(block4 == this || block4 == BlockInit.CABLE.get()))
				.with(WEST, Boolean.valueOf(block5 == this || block5 == BlockInit.CABLE.get()));
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (!stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		} else {
			Block block = facingState.getBlock();
			boolean flag = block == this || block == BlockInit.CABLE.get() || facing == Direction.DOWN;
			return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), Boolean.valueOf(flag));
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.CABLE.get().create();
	}

	@Override
	public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, world, pos, neighbor);
		TileEntity thisTile = world.getTileEntity(pos);
		if (thisTile instanceof CableTileEntity) {
			CableTileEntity thisCable = (CableTileEntity) thisTile;
			TileEntity neighbourTile = world.getTileEntity(neighbor);
			if (neighbourTile instanceof CableTileEntity) {
				CableTileEntity neighbourCable = (CableTileEntity) neighbourTile;
				if (neighbourCable.getEnergyStored() < thisCable.getEnergyStored()) {
					neighbourCable.setEnergyStored(thisCable.getEnergyStored());
				} else {
					thisCable.setEnergyStored(neighbourCable.getEnergyStored());
				}
			}
		}
	}
}
