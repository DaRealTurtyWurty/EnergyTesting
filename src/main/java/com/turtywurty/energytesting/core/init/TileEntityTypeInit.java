package com.turtywurty.energytesting.core.init;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.common.tileentity.CableTileEntity;
import com.turtywurty.energytesting.common.tileentity.ConsumerTileEntity;
import com.turtywurty.energytesting.common.tileentity.EnergyStorageTileEntity;
import com.turtywurty.energytesting.common.tileentity.GeneratorTileEntity;
import com.turtywurty.energytesting.common.tileentity.WirelessRecieverTileEntity;
import com.turtywurty.energytesting.common.tileentity.WirelessTransmitterTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeInit {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, EnergyTesting.MOD_ID);

	public static final RegistryObject<TileEntityType<GeneratorTileEntity>> GENERATOR = TILE_ENTITY_TYPES.register(
			"generator",
			() -> TileEntityType.Builder.create(GeneratorTileEntity::new, BlockInit.GENERATOR.get()).build(null));

	public static final RegistryObject<TileEntityType<ConsumerTileEntity>> CONSUMER = TILE_ENTITY_TYPES.register(
			"consumer",
			() -> TileEntityType.Builder.create(ConsumerTileEntity::new, BlockInit.CONSUMER.get()).build(null));

	public static final RegistryObject<TileEntityType<EnergyStorageTileEntity>> ENERGY_STORAGE = TILE_ENTITY_TYPES
			.register("energy_storage", () -> TileEntityType.Builder
					.create(EnergyStorageTileEntity::new, BlockInit.ENERGY_STORAGE.get()).build(null));

	public static final RegistryObject<TileEntityType<WirelessTransmitterTileEntity>> WIRELESS_TRANSMITTER = TILE_ENTITY_TYPES
			.register("wireless_transmitter", () -> TileEntityType.Builder
					.create(WirelessTransmitterTileEntity::new, BlockInit.WIRELESS_TRANSMITTER.get()).build(null));

	public static final RegistryObject<TileEntityType<WirelessRecieverTileEntity>> WIRELESS_RECIEVER = TILE_ENTITY_TYPES
			.register("wireless_reciever", () -> TileEntityType.Builder
					.create(WirelessRecieverTileEntity::new, BlockInit.WIRELESS_RECIEVER.get()).build(null));

	public static final RegistryObject<TileEntityType<CableTileEntity>> CABLE = TILE_ENTITY_TYPES.register("cable",
			() -> TileEntityType.Builder.create(CableTileEntity::new, BlockInit.CABLE.get()).build(null));
}
