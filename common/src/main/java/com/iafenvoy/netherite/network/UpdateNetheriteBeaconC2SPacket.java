package com.iafenvoy.netherite.network;

import com.iafenvoy.netherite.NetheriteExtension;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class UpdateNetheriteBeaconC2SPacket extends UpdateBeaconC2SPacket {
    public static final Identifier ID = new Identifier(NetheriteExtension.MOD_ID, "netherite_beacon_update_packet");

    private final Optional<StatusEffect> tertiaryEffect;

    public UpdateNetheriteBeaconC2SPacket(Optional<StatusEffect> primaryEffect, Optional<StatusEffect> secondaryEffect, Optional<StatusEffect> tertiaryEffect) {
        super(primaryEffect, secondaryEffect);
        this.tertiaryEffect = tertiaryEffect;
    }

    public UpdateNetheriteBeaconC2SPacket(PacketByteBuf packetByteBuf) {
        super(packetByteBuf);
        this.tertiaryEffect = packetByteBuf.readOptional(byteBuf -> byteBuf.readRegistryValue(Registries.STATUS_EFFECT));
    }

    @Override
    public void write(PacketByteBuf buf) {
        super.write(buf);
        buf.writeOptional(this.tertiaryEffect, (packetByteBuf, statusEffect) -> packetByteBuf.readRegistryValue(Registries.STATUS_EFFECT));
    }

    public Optional<StatusEffect> getTertiaryEffect() {
        return this.tertiaryEffect;
    }
}
