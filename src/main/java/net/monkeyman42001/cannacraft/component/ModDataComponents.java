package net.monkeyman42001.cannacraft.component;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.core.component.DataComponentType;
import net.monkeyman42001.cannacraft.CannaCraft;

import java.util.function.UnaryOperator;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import java.util.function.Supplier;
import net.monkeyman42001.cannacraft.component.Strain;
import net.neoforged.neoforge.registries.DeferredRegister.DataComponents;
import net.minecraft.core.component.DataComponentType.Builder;


public class ModDataComponents {

	public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(CannaCraft.MOD_ID);
	// add components here
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRAIN =
        DATA_COMPONENT_TYPES.register(
                "strain",
                () -> DataComponentType.<String>builder()
                        .persistent(Codec.STRING)
                        .build()
        );
	//public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRAIN = DATA_COMPONENT_TYPES.register("strain", builder -> builder.persistant(Codec.STRING));

	

	public static void register(IEventBus eventBus) {
		DATA_COMPONENT_TYPES.register(eventBus);
	}
}