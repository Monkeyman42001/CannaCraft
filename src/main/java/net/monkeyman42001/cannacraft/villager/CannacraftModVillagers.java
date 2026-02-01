package net.monkeyman42001.cannacraft.villager;

import com.google.common.collect.ImmutableSet;
import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.block.CannacraftBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class CannacraftModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, CannaCraft.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, CannaCraft.MOD_ID);

    public static final Holder<PoiType> DEALER_POI = POI_TYPES.register("dealer_poi",
            () -> new PoiType(ImmutableSet.copyOf(CannacraftBlocks.GROW_TENT.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final Holder<VillagerProfession> DEALER = VILLAGER_PROFESSIONS.register("dealer",
            () -> new VillagerProfession("dealer", holder -> holder.value() == DEALER_POI.value(),
                    poiTypeHolder -> poiTypeHolder.value() == DEALER_POI.value(), ImmutableSet.of(), ImmutableSet.of(),
                    BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.piston.extend"))));


    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}