package com.iafenvoy.netherite.network;

import com.iafenvoy.netherite.NetheriteExtension;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public record UpdateNetheriteBeaconC2SPacket(Optional<RegistryEntry<StatusEffect>> primary,
                                             Optional<RegistryEntry<StatusEffect>> secondary,
                                             Optional<RegistryEntry<StatusEffect>> tertiaryEffect) implements CustomPayload {
    public static final Id<UpdateNetheriteBeaconC2SPacket> ID = new Id<>(Identifier.of(NetheriteExtension.MOD_ID, "netherite_beacon_update_packet"));
    public static final PacketCodec<RegistryByteBuf, UpdateNetheriteBeaconC2SPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.registryEntry(RegistryKeys.STATUS_EFFECT).collect(PacketCodecs::optional), UpdateNetheriteBeaconC2SPacket::primary,
            PacketCodecs.registryEntry(RegistryKeys.STATUS_EFFECT).collect(PacketCodecs::optional), UpdateNetheriteBeaconC2SPacket::secondary,
            PacketCodecs.registryEntry(RegistryKeys.STATUS_EFFECT).collect(PacketCodecs::optional), UpdateNetheriteBeaconC2SPacket::tertiaryEffect,
            UpdateNetheriteBeaconC2SPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
