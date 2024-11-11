package com.iafenvoy.netherite.neoforge;

import com.iafenvoy.netherite.NetheriteExtensionClient;
import com.iafenvoy.netherite.registry.NetheriteRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NetheriteExtensionNeoForgeClient {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(NetheriteExtensionClient::process);
    }

    @SubscribeEvent
    public static void registerModel(ModelEvent.RegisterAdditional event) {
        NetheriteRenderers.registerModel(event::register);
    }
}
