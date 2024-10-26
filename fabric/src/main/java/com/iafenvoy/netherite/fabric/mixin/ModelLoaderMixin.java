package com.iafenvoy.netherite.fabric.mixin;

import com.iafenvoy.netherite.registry.NetheriteRenderers;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract void addModel(ModelIdentifier modelId);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
    public void addNetheriteTrident(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> modelResources, Map<Identifier, List<ModelLoader.SourceTrackedData>> blockStateResources, CallbackInfo ci) {
        NetheriteRenderers.registerModel(this::addModel);
    }
}
