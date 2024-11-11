package com.iafenvoy.netherite.fabric.mixin;

import com.github.crimsondawn45.fabricshieldlib.initializers.FabricShieldLibClient;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FabricShieldLibClient.class, remap = false)
public class FabricShieldLibClientMixin {
    @Redirect(method = "onInitializeClient", at = @At(value = "INVOKE", target = "Lnet/fabricmc/loader/api/FabricLoader;isDevelopmentEnvironment()Z"))
    private boolean disableDevelopmentStuff(FabricLoader instance) {
        return false;
    }
}
