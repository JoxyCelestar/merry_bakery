package net.celestar.merry_bakery.registry.block;

import net.celestar.merry_bakery.Merry_Bakery;
import net.celestar.merry_bakery.registry.item.MB_ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.bakery.core.block.cake.CakeBlock;

import java.util.function.Supplier;

public class Mod_Blocks {
    public static final DeferredRegister<Block> MOD_BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Merry_Bakery.MOD_ID);
// ------------------------------------------------------------------------------------------ //
// Block creation
    public static final RegistryObject<Block> FRUITCAKE = RegisterBlockItem("fruitcake", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), MB_ItemRegistry.FRUITCAKE_SLICE));







// ------------------------------------------------------------------------------------------ //

// Registers Block and Block item for this method
    private static <T extends Block> RegistryObject<T> RegisterBlockItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = MOD_BLOCKS.register(name, block);
        RegisterBlockItem(name, toReturn);
        return toReturn;
    }


    // Auto assign item to block
    private static <T extends Block> RegistryObject<Item> RegisterBlockItem(String name, RegistryObject<T> block) {
        return MB_ItemRegistry.MOD_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

// ------------------------------------------------------- //
    public static void register(IEventBus eventBus) {
        MOD_BLOCKS.register(eventBus);
    }
}
