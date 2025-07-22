package net.celestar.merry_bakery.registry.tag;

import net.celestar.merry_bakery.core_functions.NamespacePrefix;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class tags_registry {
    public static final TagKey<Item> JAMS       = TagKey.create(Registries.ITEM, new NamespacePrefix("Jams"));
    public static final TagKey<Item> FILLINGS   = TagKey.create(Registries.ITEM, new NamespacePrefix("Fillings"));
    public static final TagKey<Item> TOPPINGS   = TagKey.create(Registries.ITEM, new NamespacePrefix("Toppings"));
    public static final TagKey<Item> INTERMEDIATES   = TagKey.create(Registries.ITEM, new NamespacePrefix("Cake Ingredients"));
}
