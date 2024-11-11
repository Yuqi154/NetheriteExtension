package com.iafenvoy.netherite.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class RiptideNetheriteTridentCriterion extends AbstractCriterion<RiptideNetheriteTridentCriterion.Conditions> {
    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> conditions.matches(player));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return RecordCodecBuilder.create(i -> i.group(LootContextPredicate.CODEC.optionalFieldOf("player", LootContextPredicate.create()).forGetter(Conditions::getPlayer)).apply(i, Conditions::new));
    }

    public static class Conditions implements AbstractCriterion.Conditions {
        private final LootContextPredicate player;

        public Conditions(LootContextPredicate player) {
            this.player = player;
        }

        public boolean matches(ServerPlayerEntity player) {
            return true;
        }

        public LootContextPredicate getPlayer() {
            return player;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.ofNullable(this.player);
        }
    }
}
