package com.iafenvoy.netherite;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.network.LavaVisionUpdatePacket;
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
    public static double LAVA_VISION_DISTANCE = NetheriteExtensionConfig.getInstance().graphics.lava_vision_distance;

    public static void init() {
        NetheriteRenderers.registerModelLayers();
    }

    public static void process() {
        NetheriteRenderers.registerModelPredicates();
        NetheriteRenderers.registerBlockEntityRenderers();
        NetheriteRenderers.registerRenderTypes();
        NetheriteScreenHandlers.initializeClient();

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, LavaVisionUpdatePacket.ID, (buf, ctx) -> LAVA_VISION_DISTANCE = buf.readDouble());
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, new Identifier(NetheriteExtension.MOD_ID, "lava_vision_update"), (buf, ctx) -> NetheriteExtensionClient.LAVA_VISION_DISTANCE = buf.getDouble(0));
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, new Identifier(NetheriteExtension.MOD_ID, "netherite_trident"), (buf, ctx) -> TRIDENT_QUEUE.add(buf.readInt()));
    }
}
