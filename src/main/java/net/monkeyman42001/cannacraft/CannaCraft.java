package net.monkeyman42001.cannacraft;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.monkeyman42001.cannacraft.block.CannacraftBlocks;
import net.monkeyman42001.cannacraft.block.CannacraftModBlockEntities;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.event.CannacraftCraftingEvents;
import net.monkeyman42001.cannacraft.event.CannacraftEvents;
import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.registry.CannacraftCreativeTabs;
import net.monkeyman42001.cannacraft.registry.CannacraftMenus;
import net.monkeyman42001.cannacraft.registry.CannacraftRecipeSerializers;
import net.monkeyman42001.cannacraft.villager.CannacraftModVillagers;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


@Mod(CannaCraft.MOD_ID)
public class CannaCraft {
    public static final String MOD_ID = "cannacraft";

    public static final Logger LOGGER = LogUtils.getLogger();


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CannaCraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        //modEventBus.register(this);
        NeoForge.EVENT_BUS.register(this);


        CannacraftDataComponents.register(modEventBus);
        CannacraftBlocks.register(modEventBus);
        CannacraftModBlockEntities.register(modEventBus);
        CannacraftItems.register(modEventBus);
        CannacraftCreativeTabs.register(modEventBus);
        CannacraftMenus.register(modEventBus);
        CannacraftRecipeSerializers.register(modEventBus);
        CannacraftModVillagers.register(modEventBus);

        modEventBus.addListener(ClientModEvents::onClientSetup);
        modEventBus.addListener(ClientModEvents::onRegisterMenuScreens);
        NeoForge.EVENT_BUS.addListener(CannacraftCraftingEvents::onItemCrafted);
        NeoForge.EVENT_BUS.addListener(CannacraftEvents::addCustomTrades);
        NeoForge.EVENT_BUS.addListener(CannacraftEvents::onRightClickItem);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    static class ClientModEvents {
		@SubscribeEvent
		static void onClientSetup(FMLClientSetupEvent event) {
			event.enqueueWork(() -> ItemProperties.register(
				CannacraftItems.JOINT.get(),
				ResourceLocation.fromNamespaceAndPath(CannaCraft.MOD_ID, "left_hand"),
				(stack, level, entity, seed) -> {
					if (entity == null) {
						return 0.0F;
					}
					ItemStack offhand = entity.getOffhandItem();
					ItemStack mainhand = entity.getMainHandItem();
					boolean offhandItemMatch = ItemStack.isSameItem(offhand, stack);
					boolean mainhandItemMatch = ItemStack.isSameItem(mainhand, stack);
					if (offhandItemMatch && mainhandItemMatch) {
						return (stack == offhand) ? 1.0F : 0.0F;
					}
					return offhandItemMatch ? 1.0F : 0.0F;
				}
			));
			event.enqueueWork(() -> ItemProperties.register(
				CannacraftItems.JOINT.get(),
				ResourceLocation.fromNamespaceAndPath(CannaCraft.MOD_ID, "lit"),
				(stack, level, entity, seed) -> net.monkeyman42001.cannacraft.item.Smokable.isLit(stack) ? 1.0F : 0.0F
			));
			event.enqueueWork(() -> ItemProperties.register(
				CannacraftItems.BOWL.get(),
				ResourceLocation.fromNamespaceAndPath(CannaCraft.MOD_ID, "packed"),
				(stack, level, entity, seed) -> net.monkeyman42001.cannacraft.item.BowlItem.isPacked(stack) ? 1.0F : 0.0F
			));
		}

        @SubscribeEvent
        static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(CannacraftMenus.EXTRACTOR.get(), net.monkeyman42001.cannacraft.client.screen.ExtractorScreen::new);
            event.register(CannacraftMenus.GROW_TENT.get(), net.monkeyman42001.cannacraft.client.screen.GrowTentScreen::new);
        }
    }
}
