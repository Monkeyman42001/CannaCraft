package net.monkeyman42001.cannacraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Strain(String name, float thcPercentage, float terpenePercentage) {
	public static final Codec<Strain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("name").forGetter(Strain::name),
			Codec.FLOAT.fieldOf("thcPercentage").forGetter(Strain::thcPercentage),
			Codec.FLOAT.fieldOf("terpenePercentage").forGetter(Strain::terpenePercentage)
	).apply(instance, Strain::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Strain> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, Strain::name,
			ByteBufCodecs.FLOAT, Strain::thcPercentage,
			ByteBufCodecs.FLOAT, Strain::terpenePercentage,
			Strain::new
	);
}
