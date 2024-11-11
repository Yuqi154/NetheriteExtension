package com.iafenvoy.netherite.recipe;

import com.iafenvoy.netherite.block.NetheriteShulkerBoxBlock;
import net.minecraft.block.Block;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class NetheriteShulkerBoxColoringRecipe extends SpecialCraftingRecipe {
    public NetheriteShulkerBoxColoringRecipe(CraftingRecipeCategory category) {
        super(category);
    }


    @Override
    public ItemStack craft(RecipeInputInventory craftingInventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = ItemStack.EMPTY;
        DyeItem dyeItem = (DyeItem) Items.WHITE_DYE;
        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack2 = craftingInventory.getStack(i);
            if (!itemStack2.isEmpty()) {
                Item item = itemStack2.getItem();
                if (Block.getBlockFromItem(item) instanceof NetheriteShulkerBoxBlock)
                    itemStack = itemStack2;
                else if (item instanceof DyeItem dye)
                    dyeItem = dye;
            }
        }
        ItemStack itemStack3 = NetheriteShulkerBoxBlock.getItemStack(dyeItem.getColor());
        return itemStack.copyComponentsToNewStack(NetheriteShulkerBoxBlock.get(dyeItem.getColor()), 1);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHULKER_BOX;
    }

    @Override
    public boolean matches(RecipeInputInventory craftingInventory, World world) {
        int i = 0;
        int j = 0;
        for (int k = 0; k < craftingInventory.size(); ++k) {
            ItemStack itemStack = craftingInventory.getStack(k);
            if (!itemStack.isEmpty()) {
                if (Block.getBlockFromItem(itemStack.getItem()) instanceof NetheriteShulkerBoxBlock)
                    ++i;
                else {
                    if (!(itemStack.getItem() instanceof DyeItem))
                        return false;
                    ++j;
                }
                if (j > 1 || i > 1)
                    return false;
            }
        }
        return i == 1 && j == 1;
    }
}
