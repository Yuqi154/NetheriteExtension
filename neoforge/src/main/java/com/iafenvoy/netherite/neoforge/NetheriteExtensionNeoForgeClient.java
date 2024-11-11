package com.iafenvoy.netherite.neoforge;

import com.iafenvoy.netherite.NetheriteExtensionClient;
import com.iafenvoy.netherite.client.gui.screen.NetheriteAnvilScreen;
import com.iafenvoy.netherite.client.gui.screen.NetheriteBeaconScreen;
import com.iafenvoy.netherite.registry.NetheriteRenderers;
import com.iafenvoy.netherite.registry.NetheriteScreenHandlers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NetheriteExtensionNeoForgeClient {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(NetheriteExtensionClient::process);
    }

    @SubscribeEvent
    public static void registerMenuScreen(RegisterMenuScreensEvent event){
        event.register(NetheriteScreenHandlers.NETHERITE_ANVIL.get(), NetheriteAnvilScreen::new);
        event.register(NetheriteScreenHandlers.NETHERITE_BEACON.get(), NetheriteBeaconScreen::new);
    }

    @SubscribeEvent
    public static void registerModel(ModelEvent.RegisterAdditional event) {
        NetheriteRenderers.registerModel(event::register);
    }
}
