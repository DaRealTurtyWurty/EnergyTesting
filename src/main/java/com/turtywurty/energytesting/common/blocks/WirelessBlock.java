package com.turtywurty.energytesting.common.blocks;

import com.turtywurty.energytesting.common.container.WirelessTransmitterContainer;
import com.turtywurty.energytesting.common.tileentity.WirelessTransmitterTileEntity;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class WirelessBlock extends Block {

	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
	public static final EnumProperty<WirelessType> TYPE = EnumProperty.create("type", WirelessType.class);

	public WirelessBlock(Properties properties, WirelessType typeIn) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(TYPE, typeIn));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, TYPE);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		if (state.get(TYPE).equals(WirelessType.TRANSMITTER))
			return TileEntityTypeInit.WIRELESS_TRANSMITTER.get().create();
		else if (state.get(TYPE).equals(WirelessType.RECIEVER))
			return TileEntityTypeInit.WIRELESS_RECIEVER.get().create();
		else
			return super.createTileEntity(state, world);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			if (worldIn.getTileEntity(pos) instanceof WirelessTransmitterTileEntity) {
				WirelessTransmitterTileEntity tile = (WirelessTransmitterTileEntity) worldIn.getTileEntity(pos);
				for (int index = 0; index < tile.getInventory().getSlots(); index++) {
					worldIn.addEntity(
							new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), tile.getItemInSlot(index)));
				}
			}
			worldIn.removeTileEntity(pos);
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (state.get(TYPE).equals(WirelessType.TRANSMITTER) && !worldIn.isRemote
				&& worldIn.getTileEntity(pos) instanceof WirelessTransmitterTileEntity) {
			WirelessTransmitterTileEntity tile = (WirelessTransmitterTileEntity) worldIn.getTileEntity(pos);
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			IContainerProvider provider = WirelessTransmitterContainer.getServerContainerProvider(tile, pos);
			INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider, tile.getDisplayName());
			NetworkHooks.openGui(serverPlayer, namedProvider, pos);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace();
		BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(direction.getOpposite()));
		return blockstate.getBlock() == this && blockstate.get(FACING) == direction
				? this.getDefaultState().with(FACING, direction.getOpposite())
				: this.getDefaultState().with(FACING, direction);
	}

	public enum WirelessType implements IStringSerializable {
		TRANSMITTER("transmitter"), RECIEVER("reciever");

		private String name;

		private WirelessType(String nameIn) {
			this.name = nameIn;
		}

		@Override
		public String getName() {
			return this.name;
		}
	}
}
