package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.advancement.criterion.ConstructNetheriteBeaconCriterion;
import com.iafenvoy.netherite.advancement.criterion.FullNetheriteNetheriteBeaconCriterion;
import com.iafenvoy.netherite.advancement.criterion.RiptideNetheriteTridentCriterion;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class NetheriteCriteria {
    public static final DeferredRegister<Criterion<?>> REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.CRITERION);

    public static final RegistrySupplier<FullNetheriteNetheriteBeaconCriterion> FULL_NETHERITE_NETHERITE_BEACON = register("full_netherite_netherite_beacon", FullNetheriteNetheriteBeaconCriterion::new);
    public static final RegistrySupplier<ConstructNetheriteBeaconCriterion> CONSTRUCT_NETHERITE_BEACON = register("construct_netherite_beacon", ConstructNetheriteBeaconCriterion::new);
    public static final RegistrySupplier<RiptideNetheriteTridentCriterion> RIPTIDE_NETHERITE_TRIDENT = register("riptide_netherite_trident", RiptideNetheriteTridentCriterion::new);

    private static <T extends Criterion<?>> RegistrySupplier<T> register(String id, Supplier<T> object) {
        return REGISTRY.register(id, object);
    }

    public static void init() {
    }
}
