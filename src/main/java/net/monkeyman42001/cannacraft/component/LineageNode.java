package net.monkeyman42001.cannacraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record LineageNode(String name, List<LineageNode> parents) {
	public LineageNode {
		if (parents == null) {
			parents = List.of();
		} else {
			parents = List.copyOf(parents);
		}
	}

	public static final Codec<LineageNode> CODEC = Codec.recursive("LineageNode",
			codec -> RecordCodecBuilder.create(instance -> instance.group(
					Codec.STRING.fieldOf("name").forGetter(LineageNode::name),
					codec.listOf().optionalFieldOf("parents", List.of()).forGetter(LineageNode::parents)
			).apply(instance, LineageNode::new))
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, LineageNode> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public LineageNode decode(RegistryFriendlyByteBuf buf) {
			String name = ByteBufCodecs.STRING_UTF8.decode(buf);
			int size = buf.readVarInt();
			if (size <= 0) {
				return new LineageNode(name, List.of());
			}
			List<LineageNode> list = new java.util.ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				list.add(decode(buf));
			}
			return new LineageNode(name, list);
		}

		@Override
		public void encode(RegistryFriendlyByteBuf buf, LineageNode value) {
			ByteBufCodecs.STRING_UTF8.encode(buf, value.name());
			buf.writeVarInt(value.parents().size());
			for (LineageNode parent : value.parents()) {
				encode(buf, parent);
			}
		}
	};

	public int depth() {
		if (parents.isEmpty()) {
			return 1;
		}
		int max = 0;
		for (LineageNode parent : parents) {
			max = Math.max(max, parent.depth());
		}
		return max + 1;
	}
}
