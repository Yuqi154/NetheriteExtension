package com.iafenvoy.netherite.advancement.criterion;

import com.iafenvoy.netherite.block.entity.NetheriteBeaconBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class FullNetheriteNetheriteBeaconCriterion extends AbstractCriterion<FullNetheriteNetheriteBeaconCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, NetheriteBeaconBlockEntity beacon) {
        this.trigger(player, (conditions) -> conditions.matches(beacon));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return RecordCodecBuilder.create(i -> i.group(
                LootContextPredicate.CODEC.optionalFieldOf("player", LootContextPredicate.create()).forGetter(Conditions::getPlayer),
                NumberRange.IntRange.CODEC.fieldOf("netherite_level").forGetter(Conditions::getNetheriteLevel)
        ).apply(i, Conditions::new));
    }

    public static class Conditions implements AbstractCriterion.Conditions {
        private final LootContextPredicate player;
        private final NumberRange.IntRange netheriteLevel;

        public Conditions(LootContextPredicate player, NumberRange.IntRange netheriteLevel) {
            this.player = player;
            this.netheriteLevel = netheriteLevel;
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return this.netheriteLevel.test(beacon.getNetheriteLevel());
        }

        public LootContextPredicate getPlayer() {
            return player;
        }

        public NumberRange.IntRange getNetheriteLevel() {
            return netheriteLevel;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.ofNullable(this.player);
        }
    }
}
