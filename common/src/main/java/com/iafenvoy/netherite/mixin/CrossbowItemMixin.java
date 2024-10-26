package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.registry.NetheriteItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @ModifyReturnValue(method = "getSpeed", at = @At("RETURN"))
    private static float addCustomSpeed(float origin, @Local(name = "stack") ItemStack stack) {
        if (stack.isOf(NetheriteItems.NETHERITE_CROSSBOW.get()))
            return (float) (origin * NetheriteExtensionConfig.INSTANCE.damage.crossbow_damage_multiplier + NetheriteExtensionConfig.INSTANCE.damage.crossbow_damage_addition);
        return origin;
    }
}
