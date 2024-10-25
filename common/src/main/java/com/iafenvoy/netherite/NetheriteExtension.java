package com.iafenvoy.netherite;

import com.iafenvoy.netherite.network.UpdateNetheriteBeaconC2SPacket;
import com.iafenvoy.netherite.registry.*;
import com.iafenvoy.netherite.screen.NetheriteBeaconScreenHandler;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collection;

public class NetheriteExtension {
    public static final String MOD_ID = "netherite_ext";
    public static final Collection<ServerPlayerEntity> CONNECTED_CLIENTS = new ArrayList<>();

    public static void init() {
        NetheriteExtBlocks.BLOCK_REGISTRY.register();
        NetheriteExtBlocks.BLOCK_ENTITY_REGISTRY.register();
        NetheriteExtItemGroups.REGISTRY.register();
        NetheriteExtItems.REGISTRY.register();
        NetheriteExtRecipeSerializers.REGISTRY.register();
        NetheriteExtScreenHandlers.REGISTRY.register();
        NetheriteExtStatusEffects.REGISTRY.register();
    }

    public static void process(){
        NetheriteExtCriteria.init();
        NetheriteExtItems.init();
        NetheriteExtStats.init();

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, UpdateNetheriteBeaconC2SPacket.ID, (buf,ctx) -> {
            UpdateNetheriteBeaconC2SPacket packet = new UpdateNetheriteBeaconC2SPacket(buf);
            ctx.queue(() -> {
                if (ctx.getPlayer().currentScreenHandler instanceof NetheriteBeaconScreenHandler screenHandler)
                    screenHandler.setEffects(packet.getPrimaryEffectId(), packet.getSecondaryEffectId(), packet.getTertiaryEffect());
            });
        });
    }
}
