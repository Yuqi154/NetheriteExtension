package com.iafenvoy.netherite;

import com.iafenvoy.netherite.client.render.NetheriteBeaconBlockEntityRenderer;
import com.iafenvoy.netherite.client.render.NetheriteElytraFeatureRenderer;
import com.iafenvoy.netherite.client.render.NetheritePlusBuiltinItemModelRenderer;
import com.iafenvoy.netherite.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.network.LavaVisionUpdatePacket;
import com.iafenvoy.netherite.registry.NetheriteExtBlocks;
import com.iafenvoy.netherite.registry.NetheriteExtItems;
import com.iafenvoy.netherite.registry.NetheriteExtModelPredicates;
import com.iafenvoy.netherite.registry.NetheriteExtScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.Queue;

@Environment(EnvType.CLIENT)
public class NetheriteExtensionClient implements ClientModInitializer {
    public static final Queue<Integer> TRIDENT_QUEUE = new LinkedList<>();
    public static double LAVA_VISION_DISTANCE = NetheriteExtensionConfig.getInstance().graphics.lava_vision_distance;

    public static void registerBuiltinItemRenderers(MinecraftClient client) {
        NetheritePlusBuiltinItemModelRenderer builtinItemModelRenderer = new NetheritePlusBuiltinItemModelRenderer(client.getBlockEntityRenderDispatcher(), client.getEntityModelLoader());
        BuiltinItemRendererRegistry.DynamicItemRenderer dynamicItemRenderer = builtinItemModelRenderer::render;
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_WHITE_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_ORANGE_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_MAGENTA_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_LIGHT_BLUE_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_YELLOW_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_LIME_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_PINK_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_GRAY_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_LIGHT_GRAY_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_CYAN_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_PURPLE_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_BLUE_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_BROWN_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_GREEN_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_RED_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_BLACK_SHULKER_BOX, dynamicItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(NetheriteExtItems.NETHERITE_TRIDENT, dynamicItemRenderer);
    }

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            ClientPlayNetworking.registerReceiver(LavaVisionUpdatePacket.ID, (minecraft, listener, buf, responseSender) -> {
                LAVA_VISION_DISTANCE = buf.readDouble();
            });
            ClientPlayNetworking.registerReceiver(new Identifier(NetheriteExtension.MOD_ID, "lava_vision_update"), (minecraft, listener, buf, sender) -> {
                NetheriteExtensionClient.LAVA_VISION_DISTANCE = buf.getDouble(0);
            });
            ClientPlayNetworking.registerReceiver(new Identifier(NetheriteExtension.MOD_ID, "netherite_trident"), (minecraft, listener, buf, responseSender) -> TRIDENT_QUEUE.add(buf.readInt()));
        });
        BlockEntityRendererRegistry.register(NetheriteExtBlocks.NETHERITE_SHULKER_BOX_ENTITY, NetheriteShulkerBoxBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NetheriteExtBlocks.NETHERITE_BEACON_BLOCK_ENTITY, NetheriteBeaconBlockEntityRenderer::new);

        NetheriteExtModelPredicates.registerItemsWithModelProvider();
        NetheriteExtScreenHandlers.initializeClient();
        BlockRenderLayerMap.INSTANCE.putBlock(NetheriteExtBlocks.NETHERITE_BEACON, RenderLayer.getCutout());
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(((EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> entityRenderer, LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper registrationHelper, EntityRendererFactory.Context context) -> {
            if (entityRenderer.getModel() instanceof PlayerEntityModel || entityRenderer.getModel() instanceof BipedEntityModel || entityRenderer.getModel() instanceof ArmorStandEntityModel)
                registrationHelper.register(new NetheriteElytraFeatureRenderer<>(entityRenderer, context.getModelLoader()));
        }));
    }
}
