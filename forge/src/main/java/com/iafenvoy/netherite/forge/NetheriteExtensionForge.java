package com.iafenvoy.netherite.forge;

import com.iafenvoy.netherite.NetheriteExtension;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NetheriteExtension.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class NetheriteExtensionForge {
    public NetheriteExtensionForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(NetheriteExtension.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        NetheriteExtension.init();
    }

    @SubscribeEvent
    public static void process(FMLCommonSetupEvent event) {
        event.enqueueWork(NetheriteExtension::process);
    }
}
