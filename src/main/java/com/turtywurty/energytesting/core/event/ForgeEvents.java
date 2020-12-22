package com.turtywurty.energytesting.core.event;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.common.items.EnergyItem;
import com.turtywurty.energytesting.core.caps.ForgeEnergyItemCap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = EnergyTesting.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {

	@SubscribeEvent
	public static void attachCapsEvent(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof EnergyItem) {
			event.addCapability(new ResourceLocation(EnergyTesting.MOD_ID, "energy"),
					new ForgeEnergyItemCap((EnergyItem) event.getObject().getItem()));
		}
	}
}