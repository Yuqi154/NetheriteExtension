package com.iafenvoy.netherite.advancement.criterion;

import com.google.gson.JsonObject;
import com.iafenvoy.netherite.NetheriteExtension;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RiptideNetheriteTridentCriterion extends AbstractCriterion<RiptideNetheriteTridentCriterion.Conditions> {
    public static final Identifier id = new Identifier(NetheriteExtension.MOD_ID, "riptide_netherite_trident");

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new Conditions(extended);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> conditions.matches(player));
    }

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(LootContextPredicate player) {
            super(id, player);
        }

        public boolean matches(ServerPlayerEntity player) {
            return true;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            return super.toJson(predicateSerializer);
        }
    }
}
