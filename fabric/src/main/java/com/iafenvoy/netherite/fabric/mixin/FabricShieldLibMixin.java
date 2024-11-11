package com.iafenvoy.netherite.fabric.mixin;

import com.github.crimsondawn45.fabricshieldlib.initializers.FabricShieldLib;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FabricShieldLib.class, remap = false)
public class FabricShieldLibMixin {
    @Redirect(method = "onInitialize", at = @At(value = "INVOKE", target = "Lnet/fabricmc/loader/api/FabricLoader;isDevelopmentEnvironment()Z"))
    private boolean disableDevelopmentStuff(FabricLoader instance) {
        return false;
    }
}
