package net.celestar.merry_bakery.block_functions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.bakery.core.registry.SoundEventRegistry;
import net.satisfy.bakery.core.registry.TagsRegistry;
import net.satisfy.farm_and_charm.core.block.FacingBlock;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"deprecated"})
public class sixCutPieblock extends FacingBlock {
    //-----------------------------------------------------------------------------------------------//
    public static final IntegerProperty CUTS = IntegerProperty.create("cuts", 0, 6);
    private static final Supplier<VoxelShape> VOXEL_SHAPE_SUPPLIER = () -> {
        VoxelShape SHAPE = Shapes.empty();
        SHAPE = Shapes.joinUnoptimized(SHAPE, Shapes.box(0,0,0.0625,1,1,1), BooleanOp.OR);
        return SHAPE;
    };

    public static final Map<Direction, VoxelShape> SHAPE_DIR = Util.make(new HashMap<>(), hMap -> {
        for (Direction DIRECTION : Direction.Plane.HORIZONTAL.stream().toList()) {
            hMap.put(DIRECTION, GeneralUtil.rotateShape(Direction.NORTH, DIRECTION, VOXEL_SHAPE_SUPPLIER.get()));
        }
    });
    public final Supplier<Item> SLICES;

    // Block Functionality, basically: if it has slices left, give those, else give air
    public sixCutPieblock(Properties BLK_Properties, Supplier<Item> SLICED_ITEM) {
        super(BLK_Properties);
        this.SLICES = SLICED_ITEM != null ? SLICED_ITEM : () -> Items.AIR;
    }

    // @Override
    protected void create_BLOCKSTATE_DEFINITION(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CUTS);
    }

    // @Override
    // Redstone signal??? idk what this does
    public int get_AnalogOutputSignal(BlockState BLOCK_STATE, Level LEVEL, BlockPos POSITION) {
        return get_MaxCuts() - BLOCK_STATE.getValue(CUTS);
    }

    // *Level refers to where this is executred, Client or Server
    public @NotNull InteractionResult WHEN_USED(BlockState BLK_STATE, Level LEVEL, BlockPos POSITION, Player PLAYER, InteractionHand ON_HAND, BlockHitResult ON_HIT) {
        ItemStack HELD_ITEM = PLAYER.getItemInHand(ON_HAND);

    // If player R-clicks with nothin in hand, no shift
        if (!LEVEL.isClientSide && !PLAYER.isShiftKeyDown() && BLK_STATE.getValue(CUTS) == 0 && HELD_ITEM.isEmpty()) {
            //Get player Facing direction
            Direction DIRECTION = PLAYER.getDirection().getOpposite();
            double X_DIR = DIRECTION.getStepX() * 0.1;
            double Y_DIR = 0.35;
            double Z_DIR = DIRECTION.getStepZ() * 0.1;
            //Spawn the Item
            GeneralUtil.spawnSlice(LEVEL, new ItemStack(this),
                    POSITION.getX(), POSITION.getY(), POSITION.getZ(),
                    X_DIR, Y_DIR, Z_DIR);
            // Do not delete the block on use
            LEVEL.removeBlock(POSITION, false);
            return InteractionResult.SUCCESS;
        }

    // If player has a Knife in hand and is sneaking
        if (PLAYER.isShiftKeyDown() && (HELD_ITEM.isEmpty() || HELD_ITEM.is(TagsRegistry.KNIVES))) {
            return this.CONSUME_SLICE(LEVEL, POSITION, BLK_STATE, PLAYER);
        }

    // If Player has a Knife and isn't sneaking
        if (!PLAYER.isShiftKeyDown() &&  (HELD_ITEM.isEmpty() || HELD_ITEM.is(TagsRegistry.KNIVES))) {
            return CUT_SLICE(LEVEL, POSITION, BLK_STATE, PLAYER);
        }

        return InteractionResult.PASS;
    }



    //-----------------------------------------------------------------------------------------------//

    public int get_MaxCuts() {return 6;}

    public boolean has_AnalogOuputSignal(BlockState BLK_STATE) { return true; }

    public boolean CAN_SURVIVE(BlockState BLK_STATE, LevelReader LVL_READER, BlockPos BLK_POSITION) {
        return GeneralUtil.isFullAndSolid(LVL_READER, BLK_POSITION);
    }

    // give player (this SLICES) as specified in the registry
    public ItemStack get_PieSlicedItem() { return new ItemStack(this.SLICES != null ? this.SLICES.get() : Items.AIR); }

    // CONSUME
    protected InteractionResult CONSUME_SLICE(Level LEVEL, BlockPos POSITION, BlockState BK_STATE, Player PLAYER) {
        if (!PLAYER.canEat(false)) { return InteractionResult.PASS; }
        else {
            ItemStack SLICED_ITEM = this.get_PieSlicedItem();
            FoodProperties SLICED_FOOD = SLICED_ITEM.getItem().getFoodProperties();

            PLAYER.getFoodData().eat(SLICED_ITEM.getItem(), SLICED_ITEM);
                if (this.get_PieSlicedItem().getItem().isEdible() && SLICED_FOOD != null)
                {
                    for (Pair<MobEffectInstance, Float> PAIR : SLICED_FOOD.getEffects())
                    {
                        if (!LEVEL.isClientSide && PAIR.getFirst() != null && LEVEL.random.nextFloat() < PAIR.getSecond()) {
                            PLAYER.addEffect(new MobEffectInstance(PAIR.getFirst()));
                        }
                    }
                }
                int cuts_left = BK_STATE.getValue(CUTS);
                // if cuts left are less than the max, add 1 until it matches, then destroy the block
                if (cuts_left < get_MaxCuts() - 1)
                { LEVEL.setBlock(POSITION, BK_STATE.setValue(CUTS, cuts_left + 1), 5); }
                else
                { LEVEL.destroyBlock(POSITION, false); }
                LEVEL.playSound(null, POSITION, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.7f, 0.7f);
                return  InteractionResult.SUCCESS;
            }
    }

    // Thingy was CUT instead
    protected InteractionResult CUT_SLICE(Level LEVEL, BlockPos BLK_POSITION, BlockState BLK_STATE, Player PLAYER) {
        // same as Consume, but in this case an item is given to the player
        int cuts_left = BLK_STATE.getValue(CUTS);
        if (cuts_left < get_MaxCuts() - 1)
        { LEVEL.setBlock(BLK_POSITION, BLK_STATE.setValue(CUTS, cuts_left + 1), 5); }
        else
        { LEVEL.removeBlock(BLK_POSITION, false); }

        Direction DIRECTION = PLAYER.getDirection().getOpposite();
        double xDir = DIRECTION.getStepX() * 0.1;
        double yDir = 0.35;
        double zDir = DIRECTION.getStepZ() * 0.1;

        GeneralUtil.spawnSlice(LEVEL, this.get_PieSlicedItem(),
                BLK_POSITION.getX() + 0.5, BLK_POSITION.getY() + 0.5, BLK_POSITION.getZ() + 0.5,
                xDir, yDir, zDir);
        LEVEL.playSound(null, BLK_POSITION, SoundEventRegistry.CAKE_CUT.get(), SoundSource.PLAYERS, 0.75f,0.75f);
        return InteractionResult.SUCCESS;
    }

    //-----------------------------------------------------------------------------------------------//
                // Text Descriptions
    // @Override
    public void HOVER_TEXT(ItemStack ITEM, BlockGetter WORLD, List<Component> TOOLTIP, TooltipFlag CONTEXT)
    {
        TOOLTIP.add(Component.translatable("tooltip.merry_bakery.can-be-placed").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        TOOLTIP.add(Component.empty());
        TOOLTIP.add(Component.translatable("tooltip.merry_bakery.cake-pickup").withStyle(ChatFormatting.WHITE));
        TOOLTIP.add(Component.translatable("tooltip.merry_bakery.cake-with-knife").withStyle(ChatFormatting.WHITE));
        TOOLTIP.add(Component.translatable("tooltip.merry_bakery.cake-eat").withStyle(ChatFormatting.WHITE));
        TOOLTIP.add(Component.empty());
        TOOLTIP.add(Component.translatable("tooltip.merry_bakery.credit").withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    //-----------------------------------------------------------------------------------------------//
}
