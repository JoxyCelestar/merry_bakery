package net.celestar.merry_bakery.registry.item;


import net.celestar.merry_bakery.Merry_Bakery;
import net.celestar.merry_bakery.registry.food.Food_registry;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MB_ItemRegistry {
    public static final DeferredRegister<Item> MOD_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Merry_Bakery.MOD_ID);


// ------------------------------------------------------------------------------------------ //
// Adding in the actual items down here

    public static final RegistryObject<Item> FRUITCAKE_SLICE = MOD_ITEMS.register("fruitcake_slice", () -> new Item(new Item.Properties().food(Food_registry.FRUITCAKE_SLICE)));




    // ------------------------------------------------------------------------------------------ //
// Void Listener
    public static void register(IEventBus ITEM_EventBus) {
        MOD_ITEMS.register(ITEM_EventBus);
    }


}