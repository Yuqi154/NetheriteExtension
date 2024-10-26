package com.iafenvoy.netherite.config;

public final class NetheriteExtensionConfig {
    public static final NetheriteExtensionConfig INSTANCE = ConfigLoader.load(NetheriteExtensionConfig.class, "./config/netherite_ext/config.json", new NetheriteExtensionConfig());
    public AnvilConfigs anvil = new AnvilConfigs();
    public DamageConfigs damage = new DamageConfigs();
    public DurabilityConfigs durability = new DurabilityConfigs();
    public GraphicsConfigs graphics = new GraphicsConfigs();

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
