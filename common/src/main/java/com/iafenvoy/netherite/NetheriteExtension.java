package com.iafenvoy.netherite;

import com.iafenvoy.netherite.network.UpdateNetheriteBeaconC2SPacket;
import com.iafenvoy.netherite.registry.*;
import com.iafenvoy.netherite.screen.NetheriteBeaconScreenHandler;
import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.registry.entry.RegistryEntry;
import org.slf4j.Logger;

public class NetheriteExtension {
    public static final String MOD_ID = "netherite_ext";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        NetheriteBlocks.BLOCK_REGISTRY.register();
        NetheriteBlocks.BLOCK_ENTITY_REGISTRY.register();
        NetheriteCriteria.REGISTRY.register();
        NetheriteItemGroups.REGISTRY.register();
        NetheriteItems.REGISTRY.register();
        NetheriteRecipeSerializers.REGISTRY.register();
        NetheriteScreenHandlers.REGISTRY.register();
        NetheriteStatusEffects.REGISTRY.register();
        NetheriteStats.STATS_REGISTRY.register();
        NetheriteStats.TYPE_REGISTRY.register();
    }

    public static void process() {
        NetheriteItems.init();
        NetheriteStats.init();
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, UpdateNetheriteBeaconC2SPacket.ID, UpdateNetheriteBeaconC2SPacket.CODEC, (packet, ctx) -> ctx.queue(() -> {
            if (ctx.getPlayer().currentScreenHandler instanceof NetheriteBeaconScreenHandler screenHandler) {
                screenHandler.setEffects(packet.primary().map(RegistryEntry::value), packet.secondary().map(RegistryEntry::value), packet.tertiaryEffect().map(RegistryEntry::value));
                ctx.getPlayer().closeHandledScreen();
            }
        }));
    }
}
