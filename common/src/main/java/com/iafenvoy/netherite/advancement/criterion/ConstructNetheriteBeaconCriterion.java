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

public class ConstructNetheriteBeaconCriterion extends AbstractCriterion<ConstructNetheriteBeaconCriterion.Conditions> {
    private static final Identifier ID = new Identifier(NetheriteExtension.MOD_ID, "construct_netherite_beacon");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        NumberRange.IntRange intRange = NumberRange.IntRange.fromJson(jsonObject.get("level"));
        return new Conditions(extended, intRange);
    }

    public void trigger(ServerPlayerEntity player, NetheriteBeaconBlockEntity beacon) {
        this.trigger(player, (conditions) -> conditions.matches(beacon));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final NumberRange.IntRange level;

        public Conditions(LootContextPredicate player, NumberRange.IntRange level) {
            super(ConstructNetheriteBeaconCriterion.ID, player);
            this.level = level;
        }

        public static Conditions level(NumberRange.IntRange level) {
            return new Conditions(LootContextPredicate.EMPTY, level);
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return this.level.test(beacon.getBeaconLevel());
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("level", this.level.toJson());
            return jsonObject;
        }
    }
}
