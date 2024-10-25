package com.iafenvoy.netherite.config;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.network.LavaVisionUpdatePacket;
import com.iafenvoy.netherite.network.PacketBufferUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public final class NetheriteExtensionConfig {
    private static NetheriteExtensionConfig INSTANCE = null;
    public AnvilConfigs anvil = new AnvilConfigs();
    public DamageConfigs damage = new DamageConfigs();
    public DurabilityConfigs durability = new DurabilityConfigs();
    public GraphicsConfigs graphics = new GraphicsConfigs();

    public static NetheriteExtensionConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = ConfigLoader.load(NetheriteExtensionConfig.class, "./config/netherite_ext/config.json", new NetheriteExtensionConfig());
            PacketByteBuf buf = PacketBufferUtils.create();
            buf.writeDouble(INSTANCE.graphics.lava_vision_distance);
            for (ServerPlayerEntity player : NetheriteExtension.CONNECTED_CLIENTS)
                NetworkManager.sendToPlayer(player, LavaVisionUpdatePacket.ID, buf);
        }
        return INSTANCE;
    }

    public static class AnvilConfigs {
        public double xp_reduction = 0.5;
    }

    public static class DamageConfigs {
        public double bow_damage_addition = 0.0;
        public double bow_damage_multiplier = 1.0;
        public double crossbow_damage_addition = 0.0;
        public double crossbow_damage_multiplier = 1.0;
        public double trident_damage_addition = 0.0;
        public double trident_damage_multiplier = 1.0;
        public int elytra_armor_points = 4;
    }

    public static class DurabilityConfigs {
        public int bow = 768;
        public int crossbow = 562;
        public int elytra = 864;
        public int fishing_rod = 128;
        public int trident = 500;
        public int shears = 476;
        public int shield = 672;
    }

    public static class GraphicsConfigs {
        public double lava_vision_distance = 0.25;
    }
}
