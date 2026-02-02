package net.monkeyman42001.cannacraft.component;

import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.core.component.DataComponentType;
import net.monkeyman42001.cannacraft.CannaCraft;

import com.mojang.serialization.Codec;


public class CannacraftDataComponents {

	public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(CannaCraft.MOD_ID);
	// add components here
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRAIN =
        DATA_COMPONENT_TYPES.register(
                "strain",
                () -> DataComponentType.<String>builder()
                        .persistent(Codec.STRING)
						.networkSynchronized(ByteBufCodecs.STRING_UTF8)
                        .build()
        );
	//public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRAIN = DATA_COMPONENT_TYPES.register("strain", builder -> builder.persistant(Codec.STRING));

	

	public static void register(IEventBus eventBus) {
		DATA_COMPONENT_TYPES.register(eventBus);
	}
}