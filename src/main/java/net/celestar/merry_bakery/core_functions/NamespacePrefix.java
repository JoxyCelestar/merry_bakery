package net.celestar.merry_bakery.core_functions;

import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("deprecated")
public class NamespacePrefix extends ResourceLocation {
    public NamespacePrefix(String PATH){
        super("merry_bakery", PATH);
    }

    public static String TO_STRING(String Final_PATH) { return "merry_bakery:" + Final_PATH; }
}
