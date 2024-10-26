package com.iafenvoy.netherite.fabric.client;

import com.iafenvoy.netherite.NetheriteExtensionClient;
import net.fabricmc.api.ClientModInitializer;

public final class NetheriteExtensionFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetheriteExtensionClient.init();
        NetheriteExtensionClient.process();
    }
}
