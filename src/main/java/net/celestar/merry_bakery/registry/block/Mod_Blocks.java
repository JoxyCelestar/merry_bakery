package net.celestar.merry_bakery.registry.block;

import dev.architectury.registry.registries.RegistrySupplier;
import net.celestar.merry_bakery.Merry_Bakery;
import net.celestar.merry_bakery.block_functions.CakeLayeringStation;
import net.celestar.merry_bakery.core_functions.CLSTR_RegistryFunctions;
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

    private static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Merry_Bakery.MOD_ID);
    private static final DeferredRegister<Item> ITEM_REGISTRY   = DeferredRegister.create(ForgeRegistries.ITEMS,  Merry_Bakery.MOD_ID);


// ------------------------------------------------------------------------------------------ //

    public static final RegistryObject<Block> FRUITCAKE;
    public static final RegistryObject<Block> CAKE_LAYERING_STATION;

    // Block creation
    static
    {
    FRUITCAKE                   = regBLK_ITEM("fruitcake", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), MB_ItemRegistry.FRUITCAKE_SLICE));
    CAKE_LAYERING_STATION       = regBLK_ITEM("advanced_cake_station", () -> new CakeLayeringStation(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    }






// ------------------------------------------------------------------------------------------ //


    // Registers Itemless Block
    private static RegistryObject<Block> regBLK(String BLK_NAME, Supplier<Block> BLK_DATA) {
        RegistryObject<Block> SendToMain = BLOCK_REGISTRY.register(BLK_NAME, BLK_DATA);
        regBLK(BLK_NAME, SendToMain);
        return SendToMain;
    }


    //These two Register Item With block
    private static RegistryObject<Block> regBLK_ITEM(String NAME, Supplier<Block> BLOCK) {
        RegistryObject<Block> ToMain = BLOCK_REGISTRY.register(NAME, BLOCK); regBLK_ITEM(NAME, ToMain);
        return ToMain;
    }
    private static RegistryObject<Item>  regBLK_ITEM(String NAME, RegistryObject<Block> BLOCK) {
        return ITEM_REGISTRY.register(NAME, () -> new BlockItem(BLOCK.get(), new Item.Properties()));
    }


// ------------------------------------------------------- //
    public static void register(IEventBus eventBus) {
        BLOCK_REGISTRY.register(eventBus);
        ITEM_REGISTRY .register(eventBus);
    }
}
