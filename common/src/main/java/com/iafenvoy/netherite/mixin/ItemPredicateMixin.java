package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.registry.NetheriteItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntryList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(ItemPredicate.class)
public class ItemPredicateMixin {
    @Shadow
    @Final
    private Optional<RegistryEntryList<Item>> items;

    @ModifyVariable(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), argsOnly = true)
    public ItemStack letNetheriteShearsCountAsShears(ItemStack stack) {
        if (this.items.isPresent() && this.items.get().contains(Items.SHEARS.getRegistryEntry()) && stack.isOf(NetheriteItems.NETHERITE_SHEARS.get()))
            return stack.copyComponentsToNewStack(Items.SHEARS, stack.getCount());
        return stack;
    }
}
