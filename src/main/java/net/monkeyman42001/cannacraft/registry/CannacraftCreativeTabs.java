package net.monkeyman42001.cannacraft.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.block.CannacraftBlocks;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;
import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CannacraftCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CannaCraft.MOD_ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN =
			CREATIVE_TABS.register("cannacraft", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.cannacraft"))
					.icon(() -> new ItemStack(CannacraftItems.NUG.get()))
					.displayItems((parameters, output) -> {
					Strain defaultStrain = Strain.STARTER_STRAINS[0];
						output.accept(withStrain(CannacraftItems.CANNABIS_SEED.get(), defaultStrain));
						output.accept(withStrain(CannacraftItems.NUG.get(), defaultStrain));
						output.accept(withStrain(CannacraftItems.GROUND_CANNABIS.get(), defaultStrain));
						output.accept(withStrain(CannacraftItems.EXTRACT.get(), defaultStrain));
						output.accept(withStrain(CannacraftItems.JOINT.get(), defaultStrain));
						output.accept(CannacraftItems.GRINDER.get());
						output.accept(CannacraftItems.BOWL.get());
						output.accept(CannacraftItems.LIGHTER.get());
						output.accept(CannacraftItems.MATCHBOX.get());
						output.accept(CannacraftBlocks.GROW_TENT.get());
						output.accept(CannacraftBlocks.EXTRACTOR.get());
					})
					.build());

	public static void register(IEventBus eventBus) {
		CREATIVE_TABS.register(eventBus);
	}

	private static ItemStack withStrain(net.minecraft.world.item.Item item, Strain strain) {
		ItemStack stack = new ItemStack(item);
		stack.set(CannacraftDataComponents.STRAIN.get(), strain);
		return stack;
	}
}
