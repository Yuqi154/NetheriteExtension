package com.iafenvoy.netherite.neoforge;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.NetheriteExtensionClient;
import dev.architectury.platform.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(NetheriteExtension.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class NetheriteExtensionNeoForge {
    public NetheriteExtensionNeoForge() {
        NetheriteExtension.init();
        if (Platform.getEnv() == Dist.CLIENT)
            NetheriteExtensionClient.init();
    }

    @SubscribeEvent
    public static void process(FMLCommonSetupEvent event) {
        event.enqueueWork(NetheriteExtension::process);
    }
}
