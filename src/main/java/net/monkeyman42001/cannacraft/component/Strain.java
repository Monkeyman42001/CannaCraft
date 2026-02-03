package net.monkeyman42001.cannacraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

public record Strain(String name, float thcPercentage, float terpenePercentage, int colorRgb) {
	public static final Codec<Strain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("name").forGetter(Strain::name),
			Codec.FLOAT.fieldOf("thcPercentage").forGetter(Strain::thcPercentage),
			Codec.FLOAT.fieldOf("terpenePercentage").forGetter(Strain::terpenePercentage),
			Codec.INT.optionalFieldOf("colorRgb", 0xFFFFFF).forGetter(Strain::colorRgb)
	).apply(instance, Strain::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Strain> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, Strain::name,
			ByteBufCodecs.FLOAT, Strain::thcPercentage,
			ByteBufCodecs.FLOAT, Strain::terpenePercentage,
			ByteBufCodecs.INT, Strain::colorRgb,
			Strain::new
	);

	public static final Strain[] DEFAULT_STRAINS = new Strain[] {
			new Strain("Sour Diesel", 19.0f, 1.6f, 0xE6E04B),
			new Strain("Blue Dream", 19.2f, 1.9f, 0x4AA3FF),
			new Strain("OG Kush", 19.1f, 1.35f, 0x1B7A3A),
			new Strain("Girl Scout Cookies", 19.1f, 1.4f, 0xD4A846),
			new Strain("Gelato", 18.7f, 1.0f, 0xC9A0FF),
			new Strain("Gorilla Glue", 21.3f, 1.53f, 0x7A7A7A),
			new Strain("Granddaddy Purple", 20.0f, 1.07f, 0x6B2A9B)
			//new Strain("Cinderella 99")
			//lemon skunk
			//Skywalker OG
	};

	public Component coloredName() {
		return Component.literal(name).withStyle(style -> style.withColor(TextColor.fromRgb(colorRgb)));
	}
}
