package net.celestar.merry_bakery.registry.food;


import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;

public class Food_registry {
    public static final FoodProperties FRUITCAKE_SLICE = new FoodProperties.Builder().nutrition(5).alwaysEat().saturationMod(0.7f).effect( () -> new MobEffectInstance(MobEffectRegistry.SWEETS.get(), 700), 1.0f).build();

}
