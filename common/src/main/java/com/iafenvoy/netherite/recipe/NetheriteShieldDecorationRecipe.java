package com.iafenvoy.netherite.recipe;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class NetheriteShieldDecorationRecipe extends SpecialCraftingRecipe {
    public NetheriteShieldDecorationRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public ItemStack craft(RecipeInputInventory craftingInventory, RegistryWrapper.WrapperLookup registryManager) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;
        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack craftingStack = craftingInventory.getStack(i);
            if (!craftingStack.isEmpty())
                if (craftingStack.getItem() instanceof BannerItem)
                    itemStack = craftingStack;
        }
        if (itemStack2.isEmpty()) return itemStack2;
        NbtComponent compoundTag = itemStack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
        NbtCompound compoundTag2 = compoundTag == null ? new NbtCompound() : compoundTag.copyNbt();
        compoundTag2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
        itemStack2.set(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(compoundTag2));
        return itemStack2;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHIELD_DECORATION;
    }

    @Override
    public boolean matches(RecipeInputInventory craftingInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;
        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStack(i);
            if (!itemStack3.isEmpty())
                if (itemStack3.getItem() instanceof BannerItem) {
                    if (!itemStack2.isEmpty()) return false;
                    itemStack2 = itemStack3;
                } else {
                    if (!itemStack.isEmpty()) return false;
                    if (itemStack3.contains(DataComponentTypes.BLOCK_ENTITY_DATA)) return false;
                    itemStack = itemStack3;
                }
        }
        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }
}
