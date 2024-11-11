package com.iafenvoy.netherite.network;

import com.iafenvoy.netherite.NetheriteExtension;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TridentSpawnPacket(int id) implements CustomPayload {
    public static final Id<TridentSpawnPacket> ID = new Id<>(Identifier.of(NetheriteExtension.MOD_ID, "trident_spawn"));
    public static final PacketCodec<RegistryByteBuf, TridentSpawnPacket> CODEC = PacketCodec.tuple(PacketCodecs.codec(Codec.INT), TridentSpawnPacket::id, TridentSpawnPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
