package com.turtywurty.energytesting.core.event;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.client.screen.ConsumerScreen;
import com.turtywurty.energytesting.client.screen.GeneratorScreen;
import com.turtywurty.energytesting.client.screen.WirelessTramsitterScreen;
import com.turtywurty.energytesting.core.init.BlockInit;
import com.turtywurty.energytesting.core.init.ContainerTypeInit;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EnergyTesting.MOD_ID, bus = Bus.MOD)
public class ModEvents {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ContainerTypeInit.GENERATOR.get(), GeneratorScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.CONSUMER.get(), ConsumerScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.WIRELESS_TRANSMITTER.get(), WirelessTramsitterScreen::new);
	}

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			event.getRegistry().register(new BlockItem(block, new Item.Properties().group(ItemGroup.MISC))
					.setRegistryName(block.getRegistryName()));
		});
	}
}
