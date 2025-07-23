package net.celestar.merry_bakery.block_functions.cake_types;

import net.celestar.merry_bakery.block_functions.stages_cake.CakeBase_empty;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class BaseCakeToFilling extends CakeBase_empty {
    public BaseCakeToFilling(Properties BLK_PROPERTIES, Supplier<Block> FROM, Supplier<Block> TURNS_TO) {
        super(BLK_PROPERTIES, FROM, TURNS_TO);
    }

    private static final Supplier<VoxelShape> SHAPE_SUPPLIER = () -> Shapes.box(0.0625, 0, 0.0625, 0.875, 0.1875, 0.875);
// ------------------------------------------------------------------------------------- //
}
