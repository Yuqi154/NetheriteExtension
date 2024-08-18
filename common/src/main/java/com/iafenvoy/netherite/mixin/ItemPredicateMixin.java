package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.registry.NetheriteExtItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(ItemPredicate.class)
public class ItemPredicateMixin {
    @Shadow
    @Final
    @Nullable
    private Set<Item> items;

    @ModifyVariable(method = "test", at = @At("HEAD"), argsOnly = true)
    public ItemStack letNetheriteShearsCountAsShears(ItemStack stack) {
        if (this.items != null && this.items.contains(Items.SHEARS) && stack.isOf(NetheriteExtItems.NETHERITE_SHEARS)) {
            ItemStack itemStack = new ItemStack(Items.SHEARS);
            itemStack.setCount(stack.getCount());
            itemStack.setNbt(stack.getOrCreateNbt());
            return itemStack;
        }
        return stack;
    }
}
