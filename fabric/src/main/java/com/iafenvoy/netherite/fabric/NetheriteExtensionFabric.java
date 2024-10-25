package com.iafenvoy.netherite.fabric;

import com.iafenvoy.netherite.NetheriteExtension;
import net.fabricmc.api.ModInitializer;

public final class NetheriteExtensionFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NetheriteExtension.init();
        NetheriteExtension.process();
    }
}
