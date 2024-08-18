package com.iafenvoy.netherite.config;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.network.LavaVisionUpdatePacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public final class NetheriteExtensionConfig {
    private static NetheriteExtensionConfig INSTANCE = null;
    public final AnvilConfigs anvil = new AnvilConfigs();
    public final DamageConfigs damage = new DamageConfigs();
    public final DurabilityConfigs durability = new DurabilityConfigs();
    public final GraphicsConfigs graphics = new GraphicsConfigs();

    public static NetheriteExtensionConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = ConfigLoader.load(NetheriteExtensionConfig.class, "./config/netherite_ext/config.json", new NetheriteExtensionConfig());
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeDouble(INSTANCE.graphics.lava_vision_distance);
            for (ServerPlayerEntity player : NetheriteExtension.CONNECTED_CLIENTS)
                ServerPlayNetworking.send(player, LavaVisionUpdatePacket.ID, buf);
        }
        return INSTANCE;
    }

    public static class AnvilConfigs {
        public final double xp_reduction = 0.5;
    }

    public static class DamageConfigs {
        public final double bow_damage_addition = 0.0;
        public final double bow_damage_multiplier = 1.0;
        public final double crossbow_damage_addition = 0.0;
        public final double crossbow_damage_multiplier = 1.0;
        public final double trident_damage_addition = 0.0;
        public final double trident_damage_multiplier = 1.0;
        public final int elytra_armor_points = 4;
    }

    public static class DurabilityConfigs {
        public final int bow = 768;
        public final int crossbow = 562;
        public final int elytra = 864;
        public final int fishing_rod = 128;
        public final int trident = 500;
        public final int shears = 476;
    }

    public static class GraphicsConfigs {
        public final double lava_vision_distance = 0.25;
    }
}
