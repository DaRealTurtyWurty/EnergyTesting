package com.turtywurty.energytesting.core.init;

import java.util.function.Supplier;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.common.items.MarkerItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			EnergyTesting.MOD_ID);

	/*
	 * public static final RegistryObject<EnergyItem> ENERGY_CAPSULE = registerItem(
	 * () -> new EnergyItem(new
	 * Item.Properties().maxStackSize(1).group(ItemGroup.MISC), 1000, 1000, 1000,
	 * 1000), "energy_capsule");
	 */

	public static final RegistryObject<MarkerItem> MARKER = registerItem(
			() -> new MarkerItem(new Item.Properties().maxStackSize(16).group(ItemGroup.MISC)), "marker");

	private static <T extends Item> RegistryObject<T> registerItem(Supplier<? extends T> item, String name) {
		return ITEMS.register(name, item);
	}
}
