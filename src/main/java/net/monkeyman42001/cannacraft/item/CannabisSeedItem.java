package net.monkeyman42001.cannacraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.monkeyman42001.cannacraft.block.CannabisPlantBlock;
import net.monkeyman42001.cannacraft.block.CannacraftBlocks;
import net.monkeyman42001.cannacraft.block.entity.CannabisPlantBlockEntity;
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
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!level.getBlockState(pos).is(Blocks.FARMLAND)) {
            return InteractionResult.PASS;
        }

        BlockPos plantPos = pos.above();
        if (!level.getBlockState(plantPos).isAir()) {
            return InteractionResult.PASS;
        }

        level.setBlock(plantPos, CannacraftBlocks.CANNABIS_PLANT.get().defaultBlockState()
                .setValue(CannabisPlantBlock.AGE, 0), 3);

        BlockEntity blockEntity = level.getBlockEntity(plantPos);
        if (blockEntity instanceof CannabisPlantBlockEntity plantEntity) {
            Strain strain = getStrain(stack);
            if (strain != null) {
                plantEntity.setStrain(strain);
            }
        }

        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

		return InteractionResult.SUCCESS;
	}

}
