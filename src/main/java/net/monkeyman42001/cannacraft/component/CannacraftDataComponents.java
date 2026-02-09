package net.monkeyman42001.cannacraft.component;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.monkeyman42001.cannacraft.CannaCraft;
import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;

public class CannacraftDataComponents {

	public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES =
		DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CannaCraft.MOD_ID);
	private static final StreamCodec<RegistryFriendlyByteBuf, List<Strain>> STRAIN_LIST_STREAM_CODEC = new StreamCodec<>() {
		@Override
		public List<Strain> decode(RegistryFriendlyByteBuf buf) {
			int size = buf.readVarInt();
			List<Strain> list = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				list.add(Strain.STREAM_CODEC.decode(buf));
			}
			return list;
		}

		@Override
		public void encode(RegistryFriendlyByteBuf buf, List<Strain> value) {
			buf.writeVarInt(value.size());
			for (Strain strain : value) {
				Strain.STREAM_CODEC.encode(buf, strain);
			}
		}
	};
	// add components here
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Strain>> STRAIN =
        DATA_COMPONENT_TYPES.register(
                "strain",
                () -> DataComponentType.<Strain>builder()
                        .persistent(Strain.CODEC)
						.networkSynchronized(Strain.STREAM_CODEC)
                        .build()
        );
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<java.util.List<Strain>>> HODGEPODGE =
		DATA_COMPONENT_TYPES.register(
			"hodgepodge",
			() -> DataComponentType.<java.util.List<Strain>>builder()
				.persistent(Strain.CODEC.listOf())
				.networkSynchronized(STRAIN_LIST_STREAM_CODEC)
				.build()
		);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<LineageNode>> LINEAGE =
		DATA_COMPONENT_TYPES.register(
			"lineage",
			() -> DataComponentType.<LineageNode>builder()
				.persistent(LineageNode.CODEC)
				.networkSynchronized(LineageNode.STREAM_CODEC)
				.build()
		);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LIT =
		DATA_COMPONENT_TYPES.register(
			"lit",
			() -> DataComponentType.<Boolean>builder()
				.persistent(Codec.BOOL)
				.networkSynchronized(ByteBufCodecs.BOOL)
				.build()
		);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Strain>> BOWL_STRAIN =
		DATA_COMPONENT_TYPES.register(
			"bowl_strain",
			() -> DataComponentType.<Strain>builder()
				.persistent(Strain.CODEC)
				.networkSynchronized(Strain.STREAM_CODEC)
				.build()
		);
	//public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRAIN = DATA_COMPONENT_TYPES.register("strain", builder -> builder.persistant(Codec.STRING));

	

	public static void register(IEventBus eventBus) {
		DATA_COMPONENT_TYPES.register(eventBus);
	}
}
