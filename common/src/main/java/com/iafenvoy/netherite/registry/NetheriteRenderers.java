package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.client.render.DynamicItemRenderer;
import com.iafenvoy.netherite.client.render.NetheriteBeaconBlockEntityRenderer;
import com.iafenvoy.netherite.client.render.NetheritePlusBuiltinItemModelRenderer;
import com.iafenvoy.netherite.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static com.iafenvoy.netherite.registry.NetheriteItems.*;

public final class NetheriteRenderers {
    public static final EntityModelLayer NETHERITE_SHIELD_MODEL_LAYER = new EntityModelLayer(Identifier.of(NetheriteExtension.MOD_ID, "netherite_shield"), "main");

    public static void registerModelLayers() {
        EntityModelLayerRegistry.register(NETHERITE_SHIELD_MODEL_LAYER, ShieldEntityModel::getTexturedModelData);
    }

    public static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(NetheriteBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), NetheriteShulkerBoxBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NetheriteBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), NetheriteBeaconBlockEntityRenderer::new);
    }

    public static void registerBuiltinItemRenderers(MinecraftClient client) {
        NetheritePlusBuiltinItemModelRenderer builtinItemModelRenderer = new NetheritePlusBuiltinItemModelRenderer(client.getBlockEntityRenderDispatcher(), client.getEntityModelLoader());
        DynamicItemRenderer dynamicItemRenderer = builtinItemModelRenderer::render;
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_WHITE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_ORANGE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_MAGENTA_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_LIGHT_BLUE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_YELLOW_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_LIME_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_PINK_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_GRAY_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_LIGHT_GRAY_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_CYAN_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_PURPLE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_BLUE_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_BROWN_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_GREEN_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_RED_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_BLACK_SHULKER_BOX.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_TRIDENT.get(), dynamicItemRenderer);
        DynamicItemRenderer.RENDERERS.put(NetheriteItems.NETHERITE_SHIELD.get(), dynamicItemRenderer);
    }

    public static void registerRenderTypes() {
        RenderTypeRegistry.register(RenderLayer.getCutout(), NetheriteBlocks.NETHERITE_BEACON.get());
    }

    public static void registerModelPredicates() {
        ItemPropertiesRegistry.register(NETHERITE_ELYTRA.get(), new Identifier("broken"), (itemStack, clientWorld, livingEntity, i) -> ElytraItem.isUsable(itemStack) ? 0.0F : 1.0F);
        ItemPropertiesRegistry.register(NETHERITE_FISHING_ROD.get(), new Identifier("cast"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) return 0.0F;
            boolean bl = livingEntity.getMainHandStack() == itemStack;
            boolean bl2 = livingEntity.getOffHandStack() == itemStack;
            if (livingEntity.getMainHandStack().getItem() instanceof FishingRodItem) bl2 = false;
            return (bl || bl2) && livingEntity instanceof PlayerEntity player && player.fishHook != null ? 1.0F : 0.0F;
        });
        ItemPropertiesRegistry.register(NETHERITE_BOW.get(), new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F);
        ItemPropertiesRegistry.register(NETHERITE_BOW.get(), new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
        ItemPropertiesRegistry.register(NETHERITE_CROSSBOW.get(), new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : CrossbowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float) CrossbowItem.getPullTime(itemStack));
        ItemPropertiesRegistry.register(NETHERITE_CROSSBOW.get(), new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F);
        ItemPropertiesRegistry.register(NETHERITE_CROSSBOW.get(), new Identifier("charged"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F);
        ItemPropertiesRegistry.register(NETHERITE_CROSSBOW.get(), new Identifier("firework"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : CrossbowItem.isCharged(itemStack) && itemStack.get(DataComponentTypes.CHARGED_PROJECTILES).contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
        ItemPropertiesRegistry.register(NETHERITE_TRIDENT.get(), new Identifier("throwing"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
    }

    public static void registerModel(Consumer<ModelIdentifier> consumer) {
        consumer.accept(new ModelIdentifier(NetheriteExtension.MOD_ID, "netherite_trident", "inventory"));
        consumer.accept(new ModelIdentifier(NetheriteExtension.MOD_ID, "netherite_trident_in_hand", "inventory"));
    }
}
