package com.iafenvoy.netherite.forge;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.NetheriteExtensionClient;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NetheriteExtension.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class NetheriteExtensionForge {
    public NetheriteExtensionForge() {
        EventBuses.registerModEventBus(NetheriteExtension.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        NetheriteExtension.init();
        if (Platform.getEnv() == Dist.CLIENT)
            NetheriteExtensionClient.init();
    }

    @SubscribeEvent
    public static void process(FMLCommonSetupEvent event) {
        event.enqueueWork(NetheriteExtension::process);
    }
}
