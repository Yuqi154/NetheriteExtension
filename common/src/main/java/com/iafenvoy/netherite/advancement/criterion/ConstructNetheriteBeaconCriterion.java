package com.iafenvoy.netherite.advancement.criterion;

import com.iafenvoy.netherite.block.entity.NetheriteBeaconBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class ConstructNetheriteBeaconCriterion extends AbstractCriterion<ConstructNetheriteBeaconCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, NetheriteBeaconBlockEntity beacon) {
        this.trigger(player, (conditions) -> conditions.matches(beacon));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return RecordCodecBuilder.create(i -> i.group(
                LootContextPredicate.CODEC.optionalFieldOf("player", null).forGetter(Conditions::getPlayer),
                NumberRange.IntRange.CODEC.fieldOf("level").forGetter(Conditions::getLevel)
        ).apply(i, Conditions::new));
    }

    public static class Conditions implements AbstractCriterion.Conditions {
        private final LootContextPredicate player;
        private final NumberRange.IntRange level;

        public Conditions(LootContextPredicate player, NumberRange.IntRange level) {
            this.player = player;
            this.level = level;
        }

        public NumberRange.IntRange getLevel() {
            return level;
        }

        public LootContextPredicate getPlayer() {
            return player;
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return this.level.test(beacon.getBeaconLevel());
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.ofNullable(this.player);
        }
    }
}
