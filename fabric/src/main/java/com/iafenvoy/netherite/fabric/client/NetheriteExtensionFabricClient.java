package com.iafenvoy.netherite.fabric.client;

import com.iafenvoy.netherite.NetheriteExtensionClient;
import com.iafenvoy.netherite.registry.NetheriteScreenHandlers;
import net.fabricmc.api.ClientModInitializer;

public final class NetheriteExtensionFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetheriteExtensionClient.init();
        NetheriteExtensionClient.process();
        NetheriteScreenHandlers.initializeClient();
    }
}
