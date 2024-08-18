package com.iafenvoy.netherite.mixin.render;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.client.NetheritePlusTextures;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(SpriteAtlasManager.class)
public class TexturedRenderLayersMixin {
    @ModifyVariable(at = @At("HEAD"), method = "<init>", argsOnly = true)
    private static Map<Identifier, Identifier> onAddDefaultTextures(Map<Identifier, Identifier> atlasIds) {
        atlasIds = new HashMap<>(atlasIds);
        atlasIds.put(NetheritePlusTextures.NETHERITE_SHULKER_BOXES_ATLAS_TEXTURE, new Identifier(NetheriteExtension.MOD_ID, "netherite_shulker_boxes"));
        atlasIds.put(NetheritePlusTextures.NETHERITE_SHIELD_PATTERNS_ATLAS_TEXTURE, new Identifier(NetheriteExtension.MOD_ID, "netherite_shield_patterns"));
        return atlasIds;
    }
}
