package com.turtywurty.energytesting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.turtywurty.energytesting.core.init.BlockInit;
import com.turtywurty.energytesting.core.init.ContainerTypeInit;
import com.turtywurty.energytesting.core.init.ItemInit;
import com.turtywurty.energytesting.core.init.TileEntityTypeInit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("energytesting")
public class EnergyTesting {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "energytesting";
	public static EnergyTesting instance;

	public EnergyTesting() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ItemInit.ITEMS.register(modEventBus);
		BlockInit.BLOCKS.register(modEventBus);
		TileEntityTypeInit.TILE_ENTITY_TYPES.register(modEventBus);
		ContainerTypeInit.CONTAINERS.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(this);

		instance = this;
	}
}
