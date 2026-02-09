package net.monkeyman42001.cannacraft.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.monkeyman42001.cannacraft.item.BowlItem;
import net.monkeyman42001.cannacraft.villager.CannacraftModTrades;
import net.monkeyman42001.cannacraft.villager.CannacraftModVillagers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

public class CannacraftEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == CannacraftModVillagers.DEALER.value()) {
            CannacraftModTrades.addTrades(event);
        }
    }

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		Player player = event.getEntity();
		Level level = event.getLevel();
		InteractionHand hand = event.getHand();
		InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		boolean packed = BowlItem.tryPack(level, player, hand, otherHand)
				|| BowlItem.tryPack(level, player, otherHand, hand);
		if (packed) {
			event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
			event.setCanceled(true);
		}
	}
}
