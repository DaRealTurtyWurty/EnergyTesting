package com.turtywurty.energytesting.core.init;

import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.common.container.ConsumerContainer;
import com.turtywurty.energytesting.common.container.GeneratorContainer;
import com.turtywurty.energytesting.common.container.WirelessTransmitterContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeInit {

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, EnergyTesting.MOD_ID);

	public static final RegistryObject<ContainerType<GeneratorContainer>> GENERATOR = CONTAINERS.register("generator",
			() -> new ContainerType<GeneratorContainer>(GeneratorContainer::getClientContainer));

	public static final RegistryObject<ContainerType<ConsumerContainer>> CONSUMER = CONTAINERS.register("consumer",
			() -> new ContainerType<ConsumerContainer>(ConsumerContainer::getClientContainer));

	public static final RegistryObject<ContainerType<WirelessTransmitterContainer>> WIRELESS_TRANSMITTER = CONTAINERS
			.register("wireless_transmitter", () -> new ContainerType<WirelessTransmitterContainer>(
					WirelessTransmitterContainer::getClientContainer));
}
