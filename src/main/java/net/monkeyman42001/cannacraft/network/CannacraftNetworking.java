package net.monkeyman42001.cannacraft.network;

import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.block.entity.GrowTentBlockEntity;
import net.monkeyman42001.cannacraft.menu.GrowTentMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class CannacraftNetworking {
	private CannacraftNetworking() {
	}

	public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(
				GrowTentNamePayload.TYPE,
				GrowTentNamePayload.STREAM_CODEC,
				CannacraftNetworking::handleGrowTentName
		);
	}

	private static void handleGrowTentName(GrowTentNamePayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			var player = context.player();
			if (player == null) {
				return;
			}
			if (!(player.containerMenu instanceof GrowTentMenu menu)) {
				return;
			}
			if (!payload.pos().equals(menu.getBlockPos())) {
				return;
			}
			BlockEntity blockEntity = player.level().getBlockEntity(payload.pos());
			if (blockEntity instanceof GrowTentBlockEntity growTent) {
				growTent.setBreedName(payload.name());
			}
		});
	}
}
