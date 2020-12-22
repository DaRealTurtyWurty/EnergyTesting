package com.turtywurty.energytesting.core.init;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.common.blocks.CableBlock;
import com.turtywurty.energytesting.common.blocks.EnergyConsumerBlock;
import com.turtywurty.energytesting.common.blocks.EnergyStorageBlock;
import com.turtywurty.energytesting.common.blocks.GeneratorBlock;
import com.turtywurty.energytesting.common.blocks.WirelessBlock;
import com.turtywurty.energytesting.common.blocks.WirelessBlock.WirelessType;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			EnergyTesting.MOD_ID);

	public static final RegistryObject<GeneratorBlock> GENERATOR = BLOCKS.register("generator",
			() -> new GeneratorBlock(Block.Properties.from(Blocks.FURNACE)));

	public static final RegistryObject<EnergyConsumerBlock> CONSUMER = BLOCKS.register("consumer",
			() -> new EnergyConsumerBlock(Block.Properties.from(Blocks.FURNACE)));

	public static final RegistryObject<EnergyStorageBlock> ENERGY_STORAGE = BLOCKS.register("energy_storage",
			() -> new EnergyStorageBlock(Block.Properties.from(Blocks.FURNACE)));

	public static final RegistryObject<WirelessBlock> WIRELESS_TRANSMITTER = BLOCKS.register("wireless_transmitter",
			() -> new WirelessBlock(Block.Properties.from(Blocks.FURNACE).notSolid(), WirelessType.TRANSMITTER));

	public static final RegistryObject<WirelessBlock> WIRELESS_RECIEVER = BLOCKS.register("wireless_reciever",
			() -> new WirelessBlock(Block.Properties.from(Blocks.FURNACE).notSolid(), WirelessType.RECIEVER));

	public static final RegistryObject<CableBlock> CABLE = BLOCKS.register("cable",
			() -> new CableBlock(Block.Properties.from(Blocks.CHORUS_PLANT)));
}
