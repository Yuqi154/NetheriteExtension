package com.iafenvoy.netherite;

import com.iafenvoy.netherite.client.render.DynamicItemRenderer;
import com.iafenvoy.netherite.client.render.NetheriteBeaconBlockEntityRenderer;
import com.iafenvoy.netherite.client.render.NetheritePlusBuiltinItemModelRenderer;
import com.iafenvoy.netherite.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.network.LavaVisionUpdatePacket;
import com.iafenvoy.netherite.registry.NetheriteExtBlocks;
import com.iafenvoy.netherite.registry.NetheriteExtItems;
import com.iafenvoy.netherite.registry.NetheriteExtModelPredicates;
import com.iafenvoy.netherite.registry.NetheriteExtScreenHandlers;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.Queue;

@Environment(EnvType.CLIENT)
public class NetheriteExtensionClient {
    public static final Queue<Integer> TRIDENT_QUEUE = new LinkedList<>();
    public static double LAVA_VISION_DISTANCE = NetheriteExtensionConfig.getInstance().graphics.lava_vision_distance;
    public static final EntityModelLayer NETHERITE_SHIELD_MODEL_LAYER = new EntityModelLayer(Identifier.of(NetheriteExtension.MOD_ID, "netherite_shield"), "main");

    public static void registerBuiltinItemRenderers(MinecraftClient client) {
        NetheritePlusBuiltinItemModelRenderer builtinItemModelRenderer = new NetheritePlusBuiltinItemModelRenderer(client.getBlockEntityRenderDispatcher(), client.getEntityModelLoader());
        DynamicItemRenderer dynamicItemRenderer = builtinItemModelRenderer::render;
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_WHITE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_ORANGE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_MAGENTA_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_LIGHT_BLUE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_YELLOW_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_LIME_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_PINK_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_GRAY_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_LIGHT_GRAY_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_CYAN_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_PURPLE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_BLUE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_BROWN_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_GREEN_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_RED_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_BLACK_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_TRIDENT.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteExtItems.NETHERITE_SHIELD.get(), dynamicItemRenderer);
    }

    public static void process() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, LavaVisionUpdatePacket.ID, (buf, ctx) -> LAVA_VISION_DISTANCE = buf.readDouble());
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, new Identifier(NetheriteExtension.MOD_ID, "lava_vision_update"), (buf, ctx) -> NetheriteExtensionClient.LAVA_VISION_DISTANCE = buf.getDouble(0));
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, new Identifier(NetheriteExtension.MOD_ID, "netherite_trident"), (buf, ctx) -> TRIDENT_QUEUE.add(buf.readInt()));

        BlockEntityRendererRegistry.register(NetheriteExtBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), NetheriteShulkerBoxBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NetheriteExtBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), NetheriteBeaconBlockEntityRenderer::new);

        NetheriteExtModelPredicates.registerItemsWithModelProvider();
        NetheriteExtScreenHandlers.initializeClient();
        RenderTypeRegistry.register(RenderLayer.getCutout(), NetheriteExtBlocks.NETHERITE_BEACON.get());

        EntityModelLayerRegistry.register(NETHERITE_SHIELD_MODEL_LAYER, ShieldEntityModel::getTexturedModelData);
        ClientLifecycleEvent.CLIENT_STARTED.register(NetheriteExtensionClient::registerBuiltinItemRenderers);
    }
}
