package com.iafenvoy.netherite;

import com.iafenvoy.netherite.network.UpdateNetheriteBeaconC2SPacket;
import com.iafenvoy.netherite.registry.*;
import com.iafenvoy.netherite.screen.NetheriteBeaconScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collection;

public class NetheriteExtension implements ModInitializer {
    public static final String MOD_ID = "netherite_ext";
    public static final Collection<ServerPlayerEntity> CONNECTED_CLIENTS = new ArrayList<>();

    public void onInitialize() {
        NetheriteExtItems.init();
        NetheriteExtScreenHandlers.init();
        NetheriteExtRecipeSerializers.init();
        NetheriteExtStatusEffects.init();
        NetheriteExtCriteria.init();
        NetheriteExtStats.init();

        ServerPlayNetworking.registerGlobalReceiver(UpdateNetheriteBeaconC2SPacket.ID, (server, player, handler, buf, responseSender) -> {
            UpdateNetheriteBeaconC2SPacket packet = new UpdateNetheriteBeaconC2SPacket(buf);
            server.execute(() -> {
                if (player.currentScreenHandler instanceof NetheriteBeaconScreenHandler screenHandler)
                    screenHandler.setEffects(packet.getPrimaryEffectId(), packet.getSecondaryEffectId(), packet.getTertiaryEffect());
            });
        });
    }
}
