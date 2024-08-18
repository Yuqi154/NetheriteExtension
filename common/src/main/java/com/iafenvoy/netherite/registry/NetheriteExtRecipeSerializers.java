package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.recipe.NetheriteShieldDecorationRecipe;
import com.iafenvoy.netherite.recipe.NetheriteShulkerBoxColoringRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class NetheriteExtRecipeSerializers {
    public static SpecialRecipeSerializer<NetheriteShulkerBoxColoringRecipe> NETHERITE_SHULKER_BOX = register("crafting_special_netheriteshulkerboxcoloring", new SpecialRecipeSerializer<>(NetheriteShulkerBoxColoringRecipe::new));
    public static SpecialRecipeSerializer<NetheriteShieldDecorationRecipe> NETHERITE_SHIELD = register("crafting_special_netheriteshielddecoration", new SpecialRecipeSerializer<>(NetheriteShieldDecorationRecipe::new));

    public static void init() {
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(NetheriteExtension.MOD_ID, id), serializer);
    }
}
