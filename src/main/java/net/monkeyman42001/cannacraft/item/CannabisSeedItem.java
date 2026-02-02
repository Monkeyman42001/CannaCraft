package net.monkeyman42001.cannacraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.TooltipFlag;

import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.List;

public class CannabisSeedItem extends Item {

    public CannabisSeedItem(Properties properties) {
        super(properties);
    }

    /* =======================
       STRAIN DATA COMPONENT
       ======================= */

    public static void setStrain(ItemStack stack, Strain strain) {
        stack.set(CannacraftDataComponents.STRAIN.get(), strain);
    }

    public static Strain getStrain(ItemStack stack) {
        return stack.get(CannacraftDataComponents.STRAIN.get());
    }

    /* =======================
       TOOLTIP (MCreator quirk)
       ======================= */
    //@Override

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Strain strain = stack.get(CannacraftDataComponents.STRAIN.get());
        if (strain != null && !strain.name().isBlank()) {
            tooltipComponents.add(Component.literal("Strain: " + strain.name()));
            tooltipComponents.add(Component.literal("THC %: " + strain.thcPercentage()));
            tooltipComponents.add(Component.literal("Terpene %: " + strain.terpenePercentage()));
        } else {
            tooltipComponents.add(Component.literal("Strain: Unknown"));
        }
    }

    /*
    public void appendHoverText(
            ItemStack stack,
            @org.jetbrains.annotations.Nullable net.minecraft.world.level.Level level,
            java.util.List<net.minecraft.network.chat.Component> tooltip,
            TooltipFlag flag
    ) {
        //String strain = getStrain(stack);
        //if (strain != null) {
        //    tooltip.add(Component.literal("Strain: " + strain));
        //}
        //tooltip.add(Component.literal("Strain: " + stack.has(CannacraftDataComponents.STRAIN)));
        tooltip.add(Component.literal("Strain: "));

        //tooltip.add(net.minecraft.network.chat.Component.literal("Strain: " + strain));
    }
     */
    /* =======================
       RIGHT CLICK
       ======================= */

    @Override
	public InteractionResult useOn(UseOnContext context) {
		//super.useOn(context);
        Player player = net.minecraft.client.Minecraft.getInstance().player;
        player.sendSystemMessage(Component.literal("EXT TEST: tooltip called!"));
        System.out.println("right clicked");
		//PlantSeedProcedure.execute(context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), context.getPlayer(), context.getItemInHand());
		return InteractionResult.SUCCESS;
	}

}
