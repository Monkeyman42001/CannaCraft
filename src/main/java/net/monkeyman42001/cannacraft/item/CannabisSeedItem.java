package net.monkeyman42001.cannacraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;

import net.monkeyman42001.cannacraft.component.ModDataComponents;
import net.monkeyman42001.cannacraft.procedures.PlantSeedProcedure;

public class CannabisSeedItem extends Item {

    public CannabisSeedItem(Properties properties) {
        super(properties);
    }

    /* =======================
       STRAIN DATA COMPONENT
       ======================= */

    public static void setStrain(ItemStack stack, String strain) {
        stack.set(ModDataComponents.STRAIN, strain);
    }

    public static String getStrain(ItemStack stack) {
        return stack.get(ModDataComponents.STRAIN);
    }

    /* =======================
       TOOLTIP (MCreator quirk)
       ======================= */

    public void appendHoverText(
            ItemStack stack,
            net.minecraft.world.level.Level level,
            java.util.List<net.minecraft.network.chat.Component> tooltip,
            TooltipFlag flag
    ) {
        String strain = getStrain(stack);
        if (strain != null) {
            tooltip.add(net.minecraft.network.chat.Component.literal("Strain: " + strain));
        }
    }

    /* =======================
       RIGHT CLICK
       ======================= */

    @Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		PlantSeedProcedure.execute(context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), context.getPlayer(), context.getItemInHand());
		return InteractionResult.SUCCESS;
	}

}