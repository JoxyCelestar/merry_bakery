package net.celestar.merry_bakery.core_functions;

import net.celestar.merry_bakery.Merry_Bakery;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CLSTR_RegistryFunctions {


    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Merry_Bakery.MOD_ID);
    public static final DeferredRegister<Item> ITEM_REGISTRY   = DeferredRegister.create(ForgeRegistries.ITEMS,  Merry_Bakery.MOD_ID);

    //Block no item
    public static RegistryObject<Block> CLS_RegisterBlock(String BLK_NAME, Supplier<Block> BLK_DATA)
    {
        RegistryObject<Block> SendToMain = BLOCK_REGISTRY.register(BLK_NAME, BLK_DATA);
        CLS_RegisterBlock(BLK_NAME, SendToMain);
        return SendToMain;
    }

    //Block with item
    public static RegistryObject<Item> CLS_RegisterBlock_WithItem(String BLK_NAME, RegistryObject<Block> BLOCK)
    {
        return ITEM_REGISTRY.register(BLK_NAME, () -> new BlockItem(BLOCK.get(), new Item.Properties()));
    }


}
