package net.celestar.merry_bakery.core_functions;

import net.celestar.merry_bakery.registry.tag.tags_registry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.level.BlockEvent;
import net.satisfy.farm_and_charm.core.block.FacingBlock;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"deprecated"})
public class IntermediatePieBlock extends FacingBlock {


    //-----------------------------------------------------------------------------------------------//
    //-----------------------------------------------------------------------------------------------//

        //Defining the Shape area
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

    //-----------------------------------------------------------------------------------------------//
    //-----------------------------------------------------------------------------------------------//


    public static final IntegerProperty STAGES = IntegerProperty.create("cake_stage", 0, 4);
    public int get_MaxStages() { return 4; }

    public IntermediatePieBlock(Properties BLK_PROPERTIES, Supplier<Item> FILLING, Supplier<Item> TOP_LAYER, Supplier<Item> GLAZE, Supplier<Item> TOPPINGS) {
        super(BLK_PROPERTIES);
    }

    //-----------------------------------------------------------------------------------------------//
    //-----------------------------------------------------------------------------------------------//

    protected void create_BLOCKSTATE_DEFINITION(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGES);
    }

    public @NotNull InteractionResult WHEN_USED(BlockState BLK_STATE, Level LEVEL, BlockPos POSITION, Player PLAYER, InteractionHand ON_HAND, BlockEvent EVENT, ItemStack HELD_ITEM) {
        HELD_ITEM = PLAYER.getItemInHand(ON_HAND);
        int CURRENT_STAGE = BLK_STATE.getValue(STAGES);

        if (!PLAYER.isShiftKeyDown() && HELD_ITEM.is(tags_registry.INTERMEDIATES) && CURRENT_STAGE == 0) {
            LEVEL.setBlock(POSITION, BLK_STATE.setValue(STAGES, CURRENT_STAGE + 1), 4);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    //-----------------------------------------------------------------------------------------------//
    protected InteractionResult ADD_CAKE_STAGE(Level LEVEL, BlockPos POSITION, BlockState BLK_STATE, Player PLAYER, InteractionHand ON_HAND, ItemStack HELD_ITEM) {
        int CURRENT_STAGE = BLK_STATE.getValue(STAGES);

        // This could probs be donw with a switch, but I'm not that smart
        if (CURRENT_STAGE < get_MaxStages() - 3) { LEVEL.setBlock(POSITION, BLK_STATE.setValue(STAGES, CURRENT_STAGE + 1), 4); LEVEL.playSound(null, POSITION, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.PLAYERS, 0.5f, 0.5f);}
        if (CURRENT_STAGE < get_MaxStages() - 2) { LEVEL.setBlock(POSITION, BLK_STATE.setValue(STAGES, CURRENT_STAGE + 1), 4); LEVEL.playSound(null, POSITION, SoundEvents.WOOL_PLACE,        SoundSource.PLAYERS, 0.5f, 0.5f);}
        if (CURRENT_STAGE < get_MaxStages() - 1) { LEVEL.setBlock(POSITION, BLK_STATE.setValue(STAGES, CURRENT_STAGE + 1), 4); LEVEL.playSound(null, POSITION, SoundEvents.SLIME_DEATH_SMALL, SoundSource.PLAYERS, 0.5f, 0.5f);}
        if (CURRENT_STAGE < get_MaxStages())     { LEVEL.setBlock(POSITION, BLK_STATE.setValue(STAGES, CURRENT_STAGE + 1), 4); LEVEL.playSound(null, POSITION, SoundEvents.BEEHIVE_DRIP,      SoundSource.PLAYERS, 0.5f, 0.5f);}
        return InteractionResult.SUCCESS;
    }
    //-----------------------------------------------------------------------------------------------//
}
