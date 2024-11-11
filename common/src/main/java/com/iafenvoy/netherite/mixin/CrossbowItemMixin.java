package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.registry.NetheriteItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @ModifyReturnValue(method = "getSpeed", at = @At("RETURN"))
    private static float addCustomSpeed(float origin, @Local(argsOnly = true) ChargedProjectilesComponent stack) {
        if (stack.contains(NetheriteItems.NETHERITE_CROSSBOW.get()))
            return (float) (origin * NetheriteExtensionConfig.INSTANCE.damage.crossbow_damage_multiplier + NetheriteExtensionConfig.INSTANCE.damage.crossbow_damage_addition);
        return origin;
    }
}
