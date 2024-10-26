package com.iafenvoy.netherite;

import com.iafenvoy.netherite.registry.NetheriteRenderers;
import com.iafenvoy.netherite.registry.NetheriteScreenHandlers;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.Queue;

@Environment(EnvType.CLIENT)
public class NetheriteExtensionClient {
    public static final Queue<Integer> TRIDENT_QUEUE = new LinkedList<>();

    public static void init() {
        NetheriteRenderers.registerModelLayers();
    }

    public static void process() {
        NetheriteRenderers.registerModelPredicates();
        NetheriteRenderers.registerBlockEntityRenderers();
        NetheriteRenderers.registerRenderTypes();
        NetheriteScreenHandlers.initializeClient();
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, new Identifier(NetheriteExtension.MOD_ID, "netherite_trident"), (buf, ctx) -> TRIDENT_QUEUE.add(buf.readInt()));
    }
}
