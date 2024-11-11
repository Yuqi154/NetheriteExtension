package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class NetheriteStats {
    public static final DeferredRegister<Identifier> STATS_REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.CUSTOM_STAT);
    public static final DeferredRegister<StatType<?>> TYPE_REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.STAT_TYPE);
    public static final Map<Identifier, StatFormatter> REGISTRY = new HashMap<>();

    public static final Identifier FLY_NETHERITE_ELYTRA_ONE_CM = register("netherite_elytra_flight_cm", StatFormatter.DISTANCE);

    private static Identifier register(String name, StatFormatter formatter) {
        Identifier identifier = Identifier.of(NetheriteExtension.MOD_ID, name);
        REGISTRY.put(identifier, formatter);
        STATS_REGISTRY.register(name, () -> identifier);
        TYPE_REGISTRY.register(name, () -> new StatType<>(Registries.CUSTOM_STAT, Text.of(name)));
        return identifier;
    }

    public static void init() {
        for (Map.Entry<Identifier, StatFormatter> entry : REGISTRY.entrySet())
            Stats.CUSTOM.getOrCreateStat(entry.getKey(), entry.getValue());
    }
}
