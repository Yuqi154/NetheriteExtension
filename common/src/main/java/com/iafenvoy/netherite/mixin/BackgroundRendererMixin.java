package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.registry.NetheriteStatusEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), method = "applyFog")
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local(name = "fogData", ordinal = 0) BackgroundRenderer.FogData fogData) {
        if (camera.getSubmersionType() == CameraSubmersionType.LAVA) {
            RegistryEntry<StatusEffect> entry = Registries.STATUS_EFFECT.getEntry(NetheriteStatusEffects.LAVA_VISION.get());
            if (camera.getFocusedEntity() instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(entry))
                fogData.fogEnd = (float) (3.0F + NetheriteExtensionConfig.INSTANCE.graphics.lava_vision_distance * livingEntity.getStatusEffect(entry).getAmplifier());
        }
    }
}
