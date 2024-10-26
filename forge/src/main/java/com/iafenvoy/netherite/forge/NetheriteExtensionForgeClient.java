package com.iafenvoy.netherite.forge;

import com.iafenvoy.netherite.NetheriteExtensionClient;
import com.iafenvoy.netherite.registry.NetheriteRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NetheriteExtensionForgeClient {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(NetheriteExtensionClient::process);
    }

    @SubscribeEvent
    public static void registerModel(ModelEvent.RegisterAdditional event) {
        NetheriteRenderers.registerModel(event::register);
    }
}
