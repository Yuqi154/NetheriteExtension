package com.iafenvoy.netherite.mixin.render;

import com.iafenvoy.netherite.NetheriteExtensionClient;
import com.iafenvoy.netherite.registry.NetheriteExtStatusEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), method = "applyFog")
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local(name = "fogData", ordinal = 0) BackgroundRenderer.FogData fogData) {
        if (camera.getSubmersionType() == CameraSubmersionType.LAVA)
            if (camera.getFocusedEntity() instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(NetheriteExtStatusEffects.LAVA_VISION.get()))
                fogData.fogEnd = (float) (3.0F + NetheriteExtensionClient.LAVA_VISION_DISTANCE * livingEntity.getStatusEffect(NetheriteExtStatusEffects.LAVA_VISION.get()).getAmplifier());
    }
}
