package net.monkeyman42001.cannacraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record Strain(
		String name,
		float thcPercentage,
		float terpenePercentage,
		int colorRgb,
		TradeInfo trade,
		List<EffectInfo> effects
) {
	public Strain {
		if (trade == null) {
			trade = DEFAULT_TRADE;
		}
		if (effects == null) {
			effects = List.of();
		}
	}

	public static final TradeInfo DEFAULT_TRADE = new TradeInfo(
			ResourceLocation.fromNamespaceAndPath("minecraft", "air"),
			0,
			0,
			0,
			0.0F,
			0
	);

	public static final Codec<TradeInfo> TRADE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("costItemId").forGetter(TradeInfo::costItemId),
			Codec.INT.fieldOf("costCount").forGetter(TradeInfo::costCount),
			Codec.INT.fieldOf("maxUses").forGetter(TradeInfo::maxUses),
			Codec.INT.fieldOf("villagerXp").forGetter(TradeInfo::villagerXp),
			Codec.FLOAT.fieldOf("priceMultiplier").forGetter(TradeInfo::priceMultiplier),
			Codec.INT.fieldOf("level").forGetter(TradeInfo::level)
	).apply(instance, TradeInfo::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, TradeInfo> TRADE_STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, TradeInfo::costItemId,
			ByteBufCodecs.VAR_INT, TradeInfo::costCount,
			ByteBufCodecs.VAR_INT, TradeInfo::maxUses,
			ByteBufCodecs.VAR_INT, TradeInfo::villagerXp,
			ByteBufCodecs.FLOAT, TradeInfo::priceMultiplier,
			ByteBufCodecs.VAR_INT, TradeInfo::level,
			TradeInfo::new
	);

	public static final Codec<EffectInfo> EFFECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("effectId").forGetter(EffectInfo::effectId),
			Codec.INT.fieldOf("durationTicks").forGetter(EffectInfo::durationTicks),
			Codec.INT.fieldOf("amplifier").forGetter(EffectInfo::amplifier),
			Codec.BOOL.optionalFieldOf("ambient", false).forGetter(EffectInfo::ambient),
			Codec.BOOL.optionalFieldOf("showParticles", true).forGetter(EffectInfo::showParticles),
			Codec.BOOL.optionalFieldOf("showIcon", true).forGetter(EffectInfo::showIcon)
	).apply(instance, EffectInfo::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, EffectInfo> EFFECT_STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, EffectInfo::effectId,
			ByteBufCodecs.VAR_INT, EffectInfo::durationTicks,
			ByteBufCodecs.VAR_INT, EffectInfo::amplifier,
			ByteBufCodecs.BOOL, EffectInfo::ambient,
			ByteBufCodecs.BOOL, EffectInfo::showParticles,
			ByteBufCodecs.BOOL, EffectInfo::showIcon,
			EffectInfo::new
	);

	private static final StreamCodec<RegistryFriendlyByteBuf, List<EffectInfo>> EFFECT_LIST_STREAM_CODEC = new StreamCodec<>() {
		@Override
		public List<EffectInfo> decode(RegistryFriendlyByteBuf buf) {
			int size = buf.readVarInt();
			if (size <= 0) {
				return Collections.emptyList();
			}
			List<EffectInfo> list = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				list.add(EFFECT_STREAM_CODEC.decode(buf));
			}
			return list;
		}

		@Override
		public void encode(RegistryFriendlyByteBuf buf, List<EffectInfo> value) {
			buf.writeVarInt(value.size());
			for (EffectInfo info : value) {
				EFFECT_STREAM_CODEC.encode(buf, info);
			}
		}
	};

	public static final Codec<Strain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("name").forGetter(Strain::name),
			Codec.FLOAT.fieldOf("thcPercentage").forGetter(Strain::thcPercentage),
			Codec.FLOAT.fieldOf("terpenePercentage").forGetter(Strain::terpenePercentage),
			Codec.INT.optionalFieldOf("colorRgb", 0xFFFFFF).forGetter(Strain::colorRgb),
			TRADE_CODEC.optionalFieldOf("trade", DEFAULT_TRADE).forGetter(Strain::trade),
			EFFECT_CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(Strain::effects)
	).apply(instance, Strain::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Strain> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, Strain::name,
			ByteBufCodecs.FLOAT, Strain::thcPercentage,
			ByteBufCodecs.FLOAT, Strain::terpenePercentage,
			ByteBufCodecs.INT, Strain::colorRgb,
			TRADE_STREAM_CODEC, Strain::trade,
			EFFECT_LIST_STREAM_CODEC, Strain::effects,
			Strain::new
	);

	public static final Strain[] DEFAULT_STRAINS = new Strain[] {
			new Strain("Sour Diesel", 19.0f, 1.60f, 0xE6E04B,
					new TradeInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "emerald"), 1, 16, 2, 0.05F, 1),
					List.of(new EffectInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "speed"), 600, 1, false, true, true))),

			new Strain("Blue Dream", 19.2f, 1.90f, 0x4AA3FF,
					new TradeInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "wheat"), 1, 16, 2, 0.05F, 1),
					List.of(new EffectInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "jump_boost"), 600, 1, false, true, true))),

			new Strain("OG Kush", 19.1f, 1.35f, 0x1B7A3A,
					new TradeInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "diamond"), 1, 12, 5, 0.10F, 1),
					List.of(new EffectInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "resistance"), 600, 0, false, true, true))),

			new Strain("Girl Scout Cookies", 19.1f, 1.40f, 0xD4A846,
					new TradeInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "emerald"), 2, 12, 5, 0.08F, 2),
					List.of(new EffectInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "regeneration"), 400, 0, false, true, true))),

			new Strain("Gelato", 18.7f, 1.00f, 0xC9A0FF,
					new TradeInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "emerald"), 3, 12, 5, 0.08F, 2),
					List.of(new EffectInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "absorption"), 600, 1, false, true, true))),

			new Strain("Gorilla Glue", 21.3f, 1.53f, 0x7A7A7A,
					new TradeInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "emerald"), 4, 10, 8, 0.10F, 3),
					List.of(new EffectInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "strength"), 600, 0, false, true, true))),

			new Strain("Granddaddy Purple", 20.0f, 1.07f, 0x6B2A9B,
					new TradeInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "emerald"), 3, 10, 8, 0.10F, 3),
					List.of(new EffectInfo(ResourceLocation.fromNamespaceAndPath("minecraft", "slowness"), 400, 0, false, true, true)))
			//new Strain("Cinderella 99")
			//lemon skunk
			//Skywalker OG
	};

	public record TradeInfo(
			ResourceLocation costItemId,
			int costCount,
			int maxUses,
			int villagerXp,
			float priceMultiplier,
			int level
	) {
		public boolean isValid() {
			return costCount > 0;
		}
	}

	public record EffectInfo(
			ResourceLocation effectId,
			int durationTicks,
			int amplifier,
			boolean ambient,
			boolean showParticles,
			boolean showIcon
	) {}

	public List<MobEffectInstance> createEffectInstances() {
		if (effects.isEmpty()) {
			return Collections.emptyList();
		}
		List<MobEffectInstance> instances = new ArrayList<>(effects.size());
		for (EffectInfo info : effects) {
			var effect = BuiltInRegistries.MOB_EFFECT.getHolder(info.effectId());
			if (effect.isPresent()) {
				instances.add(new MobEffectInstance(
						effect.get(),
						info.durationTicks(),
						info.amplifier(),
						info.ambient(),
						info.showParticles(),
						info.showIcon()
				));
			}
		}
		return instances;
	}

	public static Strain fromLegacy(String name, float thc, float terpene, int colorRgb) {
		for (Strain strain : DEFAULT_STRAINS) {
			if (strain.name().equals(name)) {
				return new Strain(name, thc, terpene, colorRgb, strain.trade(), strain.effects());
			}
		}
		return new Strain(name, thc, terpene, colorRgb, DEFAULT_TRADE, List.of());
	}

	public Component coloredName() {
		return Component.literal(name).withStyle(style -> style.withColor(TextColor.fromRgb(colorRgb)));
	}
}
