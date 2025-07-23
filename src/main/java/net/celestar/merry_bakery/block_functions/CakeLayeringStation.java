package net.celestar.merry_bakery.block_functions;

import net.celestar.merry_bakery.registry.block.Mod_Blocks;
import net.celestar.merry_bakery.registry.item.MB_ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.farm_and_charm.core.block.FacingBlock;
import org.jetbrains.annotations.NotNull;

public class CakeLayeringStation extends FacingBlock {
    public CakeLayeringStation(BlockBehaviour.Properties BLK_PROPERTIES) { super(BLK_PROPERTIES); }

    public boolean SURVIVES(BlockState BLK_STATE, LevelReader LEVEL, BlockPos POSITION) { return LEVEL.isEmptyBlock(POSITION.above()); }

// ------------------------------------------------------------------------------------------------- //

    public @NotNull InteractionResult ON_USE(BlockState BLK_STATE, Level LEVEL, BlockPos POSITION, Player PLAYER, InteractionHand ON_HAND, BlockHitResult ON_HIT) {
        if (!LEVEL.isClientSide && ON_HAND == InteractionHand.MAIN_HAND) {
            ItemStack ITEM_ON_HAND = PLAYER.getItemInHand(ON_HAND);

            if (!ITEM_ON_HAND.isEmpty()) {
                PLAYER.displayClientMessage(Component.translatable("hovertext.merry_bakery.cakelayer_start").withStyle(ChatFormatting.ITALIC), true);
            }
            BlockPos ABOVE = POSITION.above();
// ----------------------------- Now for the Layers --------------------------------- //


            if ( LEVEL.isEmptyBlock(ABOVE) && ITEM_ON_HAND.is((Item) MB_ItemRegistry.FRUITCAKE_SLICE.get()) ) {
                 LEVEL.setBlock(ABOVE, ((Block) Mod_Blocks.FRUITCAKE.get()).defaultBlockState(), 1);
                 LEVEL.playSound( null, POSITION, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.5f, 0.5f);
                 LEVEL.levelEvent(77001, ABOVE, Block.getId( (Mod_Blocks.FRUITCAKE.get()).defaultBlockState() ));
                 if (!PLAYER.isCreative()) { ITEM_ON_HAND.shrink(1); }
                 return InteractionResult.SUCCESS;
            }

        }
// ---------------------------------------------------------------------------------- //
        return InteractionResult.PASS;
    }

// ------------------------------------------------------------------------------------------------- //
}
