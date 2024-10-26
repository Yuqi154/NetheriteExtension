package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public final class NetheriteStats {
    public static final Identifier FLY_NETHERITE_ELYTRA_ONE_CM = register("netherite_elytra_flight_cm", StatFormatter.DISTANCE);

    private static Identifier register(String name, StatFormatter formatter) {
        Identifier identifier = new Identifier(NetheriteExtension.MOD_ID, name);
        Registry.register(Registries.CUSTOM_STAT, identifier, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    public static void init() {
    }
}
