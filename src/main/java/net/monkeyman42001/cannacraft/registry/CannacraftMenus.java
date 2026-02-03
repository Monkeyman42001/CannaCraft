package net.monkeyman42001.cannacraft.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.neoforge.network.IContainerFactory;
import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.menu.ExtractorMenu;

public class CannacraftMenus {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, CannaCraft.MOD_ID);

	public static final DeferredHolder<MenuType<?>, MenuType<ExtractorMenu>> EXTRACTOR =
			MENUS.register("extractor", () -> new MenuType<>((IContainerFactory<ExtractorMenu>) ExtractorMenu::new, FeatureFlags.VANILLA_SET));

	public static void register(IEventBus eventBus) {
		MENUS.register(eventBus);
	}
}
