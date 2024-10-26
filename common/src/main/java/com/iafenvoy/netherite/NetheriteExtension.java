package com.iafenvoy.netherite;

import com.iafenvoy.netherite.network.UpdateNetheriteBeaconC2SPacket;
import com.iafenvoy.netherite.registry.*;
import com.iafenvoy.netherite.screen.NetheriteBeaconScreenHandler;
import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import org.slf4j.Logger;

public class NetheriteExtension {
    public static final String MOD_ID = "netherite_ext";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        NetheriteBlocks.BLOCK_REGISTRY.register();
        NetheriteBlocks.BLOCK_ENTITY_REGISTRY.register();
        NetheriteItemGroups.REGISTRY.register();
        NetheriteItems.REGISTRY.register();
        NetheriteRecipeSerializers.REGISTRY.register();
        NetheriteScreenHandlers.REGISTRY.register();
        NetheriteStatusEffects.REGISTRY.register();
    }

    public static void process() {
        NetheriteCriteria.init();
        NetheriteItems.init();
        NetheriteStats.init();
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, UpdateNetheriteBeaconC2SPacket.ID, (buf, ctx) -> {
            UpdateNetheriteBeaconC2SPacket packet = new UpdateNetheriteBeaconC2SPacket(buf);
            ctx.queue(() -> {
                if (ctx.getPlayer().currentScreenHandler instanceof NetheriteBeaconScreenHandler screenHandler)
                    screenHandler.setEffects(packet.getPrimaryEffectId(), packet.getSecondaryEffectId(), packet.getTertiaryEffect());
            });
        });
    }
}
