package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NetheriteExtStatusEffects {
    public static final StatusEffect LAVA_VISION = register("lava_vision", new StatusEffect(StatusEffectCategory.BENEFICIAL, 16744207));

    private static StatusEffect register(String name, StatusEffect entry) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(NetheriteExtension.MOD_ID, name), entry);
    }

    public static void init() {
    }
}
