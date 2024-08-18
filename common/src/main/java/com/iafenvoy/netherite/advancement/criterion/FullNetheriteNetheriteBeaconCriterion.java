package com.iafenvoy.netherite.advancement.criterion;

import com.google.gson.JsonObject;
import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.block.entity.NetheriteBeaconBlockEntity;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FullNetheriteNetheriteBeaconCriterion extends AbstractCriterion<FullNetheriteNetheriteBeaconCriterion.Conditions> {
    public static final Identifier id = new Identifier(NetheriteExtension.MOD_ID, "full_netherite_netherite_beacon");

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        NumberRange.IntRange intRange = NumberRange.IntRange.fromJson(jsonObject.get("netherite_level"));
        return new Conditions(extended, intRange);
    }

    public void trigger(ServerPlayerEntity player, NetheriteBeaconBlockEntity beacon) {
        this.trigger(player, (conditions) -> conditions.matches(beacon));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final NumberRange.IntRange netheriteLevel;

        public Conditions(LootContextPredicate player, NumberRange.IntRange netheriteLevel) {
            super(id, player);
            this.netheriteLevel = netheriteLevel;
        }

        public static Conditions level(NumberRange.IntRange netheriteLevel) {
            return new Conditions(LootContextPredicate.EMPTY, netheriteLevel);
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return this.netheriteLevel.test(beacon.getNetheriteLevel());
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("netherite_level", this.netheriteLevel.toJson());
            return jsonObject;
        }
    }
}
