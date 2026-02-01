package net.monkeyman42001.cannacraft.villager;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import net.monkeyman42001.cannacraft.block.CannacraftModBlocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

import java.util.Set;

public class CannacraftModPoiTypes {

    // Deferred register for POIs
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, "cannacraft");

    // Dealer POI linked to GrowTent block
    public static final DeferredHolder<PoiType, PoiType> DEALER_POI =
            POI_TYPES.register("dealer_poi",
                    () -> new PoiType(
                            getAllStates(CannacraftModBlocks.GROW_TENT.get()),
                            1, // max villagers pathing
                            1  // search distance
                    ));

    private static Set<BlockState> getAllStates(net.minecraft.world.level.block.Block block) {
        return Set.copyOf(block.getStateDefinition().getPossibleStates());
    }

    // ✅ Note: DO NOT call register(); MCreator handles it automatically
}
