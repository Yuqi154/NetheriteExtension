package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class NetheriteStatusEffects {
    public static final DeferredRegister<StatusEffect> REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.STATUS_EFFECT);

    public static final RegistrySupplier<StatusEffect> LAVA_VISION = register("lava_vision", () -> new StatusEffect(StatusEffectCategory.BENEFICIAL, 16744207));

    private static RegistrySupplier<StatusEffect> register(String name, Supplier<StatusEffect> entry) {
        return REGISTRY.register(name, entry);
    }
}
