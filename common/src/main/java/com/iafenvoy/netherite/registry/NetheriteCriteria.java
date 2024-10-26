package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.advancement.criterion.ConstructNetheriteBeaconCriterion;
import com.iafenvoy.netherite.advancement.criterion.FullNetheriteNetheriteBeaconCriterion;
import com.iafenvoy.netherite.advancement.criterion.RiptideNetheriteTridentCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;

public final class NetheriteCriteria {
    public static final FullNetheriteNetheriteBeaconCriterion FULL_NETHERITE_NETHERITE_BEACON = register(new FullNetheriteNetheriteBeaconCriterion());
    public static final ConstructNetheriteBeaconCriterion CONSTRUCT_NETHERITE_BEACON = register(new ConstructNetheriteBeaconCriterion());
    public static final RiptideNetheriteTridentCriterion RIPTIDE_NETHERITE_TRIDENT = register(new RiptideNetheriteTridentCriterion());

    private static <T extends Criterion<?>> T register(T object) {
        return Criteria.register(object);
    }

    public static void init() {
    }
}
