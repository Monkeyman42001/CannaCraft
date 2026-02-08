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
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	public static final Strain[] STARTER_STRAINS = new Strain[] {
			new Strain("Afghan Kush", 17.5f, 1.20f, 0x6A8B3C,
					trade("minecraft", "emerald", 1, 16, 2, 0.05F, 1),
					List.of(effect("minecraft", "resistance", 600, 0))),
			new Strain("Durban Poison", 18.2f, 1.10f, 0xE3C84A,
					trade("minecraft", "emerald", 1, 16, 2, 0.05F, 1),
					List.of(effect("minecraft", "speed", 600, 1))),
			new Strain("Haze", 17.0f, 1.30f, 0x9FD86D,
					trade("minecraft", "wheat", 1, 16, 2, 0.05F, 1),
					List.of(effect("minecraft", "jump_boost", 600, 1))),
			new Strain("Skunk #1", 18.0f, 1.40f, 0x7FA85A,
					trade("minecraft", "emerald", 1, 16, 2, 0.05F, 1),
					List.of(effect("minecraft", "haste", 600, 0))),
			new Strain("Chemdawg", 20.5f, 2.00f, 0x4F6B54,
					trade("minecraft", "emerald", 2, 12, 5, 0.08F, 2),
					List.of(effect("minecraft", "strength", 600, 1))),
			new Strain("Blueberry", 16.5f, 1.80f, 0x4A5AA8,
					trade("minecraft", "emerald", 2, 12, 5, 0.08F, 2),
					List.of(effect("minecraft", "regeneration", 400, 0)))
	};

	public static final Strain[] ALL_STRAINS = new Strain[] {
			STARTER_STRAINS[0],
			STARTER_STRAINS[1],
			STARTER_STRAINS[2],
			STARTER_STRAINS[3],
			STARTER_STRAINS[4],
			STARTER_STRAINS[5],
			new Strain("OG Kush", 21.0f, 1.60f, 0x1B7A3A,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "resistance", 600, 1))),
			new Strain("Super Silver Haze", 20.0f, 1.70f, 0xB6C98A,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "speed", 600, 1), effect("minecraft", "jump_boost", 400, 0))),
			new Strain("Blue Dream", 19.5f, 1.90f, 0x4AA3FF,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "jump_boost", 600, 1), effect("minecraft", "night_vision", 400, 0))),
			new Strain("Sour Diesel", 20.8f, 2.10f, 0xE6E04B,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "speed", 600, 1), effect("minecraft", "haste", 600, 0))),
			new Strain("Purple Kush", 19.8f, 1.50f, 0x6B2A9B,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "absorption", 600, 0))),
			new Strain("Girl Scout Cookies", 22.0f, 2.20f, 0xD4A846,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "regeneration", 400, 1))),
			new Strain("Garlic Cookies", 23.0f, 2.40f, 0xB4A88B,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "strength", 600, 1))),
			new Strain("Dream OG", 21.5f, 2.05f, 0x4D8EBF,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "night_vision", 400, 0), effect("minecraft", "luck", 600, 0))),
			new Strain("Fire OG", 22.2f, 2.10f, 0xC65A2D,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "fire_resistance", 600, 0), effect("minecraft", "strength", 400, 0))),
			new Strain("Animal Cookies", 24.0f, 2.60f, 0x8B7D6B,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "absorption", 600, 1), effect("minecraft", "regeneration", 400, 0))),
			new Strain("Sunset Sherbet", 22.8f, 2.30f, 0xF2A65A,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "speed", 400, 0), effect("minecraft", "regeneration", 400, 0))),
			new Strain("Gelato", 23.5f, 2.50f, 0xC9A0FF,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "absorption", 600, 1))),
			new Strain("Runtz", 24.2f, 2.70f, 0xF0B7D1,
					DEFAULT_TRADE,
					List.of(effect("minecraft", "luck", 600, 1), effect("minecraft", "speed", 400, 0)))
	};

	private static final java.util.Map<String, Strain> STRAINS_BY_NAME = buildStrainLookup();
	private static final java.util.Map<String, Strain> BREEDING_TABLE = buildBreedingTable();

	private static EffectInfo effect(String namespace, String path, int durationTicks, int amplifier) {
		return new EffectInfo(ResourceLocation.fromNamespaceAndPath(namespace, path), durationTicks, amplifier, false, true, true);
	}

	private static TradeInfo trade(String namespace, String path, int costCount, int maxUses, int villagerXp, float priceMultiplier, int level) {
		return new TradeInfo(ResourceLocation.fromNamespaceAndPath(namespace, path), costCount, maxUses, villagerXp, priceMultiplier, level);
	}

	private static java.util.Map<String, Strain> buildStrainLookup() {
		java.util.Map<String, Strain> map = new java.util.HashMap<>();
		for (Strain strain : ALL_STRAINS) {
			map.put(normalizeName(strain.name()), strain);
		}
		return java.util.Map.copyOf(map);
	}

	private static java.util.Map<String, Strain> buildBreedingTable() {
		java.util.Map<String, Strain> map = new java.util.HashMap<>();
		putBreeding(map, "Afghan Kush", "Durban Poison", "OG Kush");
		putBreeding(map, "Skunk #1", "Haze", "Super Silver Haze");
		putBreeding(map, "Blueberry", "Haze", "Blue Dream");
		putBreeding(map, "Chemdawg", "Skunk #1", "Sour Diesel");
		putBreeding(map, "Afghan Kush", "Blueberry", "Purple Kush");

		putBreeding(map, "OG Kush", "Durban Poison", "Girl Scout Cookies");
		putBreeding(map, "Girl Scout Cookies", "Chemdawg", "Garlic Cookies");
		putBreeding(map, "OG Kush", "Chemdawg", "Fire OG");
		putBreeding(map, "Blue Dream", "OG Kush", "Dream OG");

		putBreeding(map, "Girl Scout Cookies", "Fire OG", "Animal Cookies");
		putBreeding(map, "Girl Scout Cookies", "Sour Diesel", "Sunset Sherbet");
		putBreeding(map, "Sunset Sherbet", "Girl Scout Cookies", "Gelato");
		putBreeding(map, "Gelato", "Blueberry", "Runtz");
		return java.util.Map.copyOf(map);
	}

	private static void putBreeding(java.util.Map<String, Strain> map, String first, String second, String result) {
		Strain resultStrain = getByName(result);
		if (resultStrain == null) {
			return;
		}
		map.put(pairKey(first, second), resultStrain);
	}

	private static String pairKey(String first, String second) {
		String left = normalizeName(first);
		String right = normalizeName(second);
		return left.compareTo(right) <= 0 ? left + "|" + right : right + "|" + left;
	}

	private static String normalizeName(String name) {
		return name == null ? "" : name.trim().toLowerCase(java.util.Locale.ROOT);
	}

	public static Strain getByName(String name) {
		return STRAINS_BY_NAME.get(normalizeName(name));
	}

	public static Strain getBreedingResult(Strain left, Strain right) {
		if (left == null || right == null) {
			return null;
		}
		return BREEDING_TABLE.get(pairKey(left.name(), right.name()));
	}

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
		for (Strain strain : ALL_STRAINS) {
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
