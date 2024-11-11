package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.recipe.NetheriteShieldDecorationRecipe;
import com.iafenvoy.netherite.recipe.NetheriteShulkerBoxColoringRecipe;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class NetheriteRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.RECIPE_SERIALIZER);

    public static final RegistrySupplier<SpecialRecipeSerializer<NetheriteShulkerBoxColoringRecipe>> NETHERITE_SHULKER_BOX = register("crafting_special_netherite_shulker_box_coloring", () -> new SpecialRecipeSerializer<>(NetheriteShulkerBoxColoringRecipe::new));
    public static final RegistrySupplier<SpecialRecipeSerializer<NetheriteShieldDecorationRecipe>> NETHERITE_SHIELD = register("crafting_special_netheriteshielddecoration", () -> new SpecialRecipeSerializer<>(NetheriteShieldDecorationRecipe::new));

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistrySupplier<S> register(String id, Supplier<S> serializer) {
        return REGISTRY.register(id, serializer);
    }
}
