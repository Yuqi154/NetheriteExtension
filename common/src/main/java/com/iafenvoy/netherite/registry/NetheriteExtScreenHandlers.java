package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.client.gui.screen.NetheriteAnvilScreen;
import com.iafenvoy.netherite.client.gui.screen.NetheriteBeaconScreen;
import com.iafenvoy.netherite.screen.NetheriteAnvilScreenHandler;
import com.iafenvoy.netherite.screen.NetheriteBeaconScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class NetheriteExtScreenHandlers {

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(Identifier id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory, FeatureSet.empty()));
    }    public static ScreenHandlerType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_anvil"), NetheriteAnvilScreenHandler::new);

    public static void init() {
    }    public static ScreenHandlerType<NetheriteBeaconScreenHandler> NETHERITE_BEACON = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_beacon"), NetheriteBeaconScreenHandler::new);

    public static void initializeClient() {
        HandledScreens.register(NetheriteExtScreenHandlers.NETHERITE_ANVIL, NetheriteAnvilScreen::new);
        HandledScreens.register(NetheriteExtScreenHandlers.NETHERITE_BEACON, NetheriteBeaconScreen::new);
    }




}
