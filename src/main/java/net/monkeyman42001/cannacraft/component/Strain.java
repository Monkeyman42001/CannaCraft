package net.monkeyman42001.cannacraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class Strain {
	public static final Codec<Strain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("name").forGetter(Strain::getName),
			Codec.FLOAT.fieldOf("thcPercentage").forGetter(Strain::getThcPercentage),
			Codec.FLOAT.fieldOf("terpenePercentage").forGetter(Strain::getTerpenePercentage)
	).apply(instance, Strain::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Strain> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, Strain::getName,
			ByteBufCodecs.FLOAT, Strain::getThcPercentage,
			ByteBufCodecs.FLOAT, Strain::getTerpenePercentage,
			Strain::new
	);

	private final String name;
	private final float thcPercentage;
	private final float terpenePercentage;

	public Strain(String name, float thcPercentage, float terpenePercentage) {
		this.name = name;
		this.thcPercentage = thcPercentage;
		this.terpenePercentage = terpenePercentage;
	}

	public String getName() {
		return name;
	}

	public float getThcPercentage() {
		return thcPercentage;
	}

	public float getTerpenePercentage() {
		return terpenePercentage;
	}
}
