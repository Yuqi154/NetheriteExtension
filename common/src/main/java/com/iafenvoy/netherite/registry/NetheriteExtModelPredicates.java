package com.iafenvoy.netherite.registry;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import static com.iafenvoy.netherite.registry.NetheriteExtItems.*;

public class NetheriteExtModelPredicates {
    public static void registerItemsWithModelProvider() {
        ModelPredicateProviderRegistry.register(NETHERITE_ELYTRA, new Identifier("broken"), (itemStack, clientWorld, livingEntity, i) -> ElytraItem.isUsable(itemStack) ? 0.0F : 1.0F);
        ModelPredicateProviderRegistry.register(NETHERITE_FISHING_ROD, new Identifier("cast"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) return 0.0F;
            boolean bl = livingEntity.getMainHandStack() == itemStack;
            boolean bl2 = livingEntity.getOffHandStack() == itemStack;
            if (livingEntity.getMainHandStack().getItem() instanceof FishingRodItem) bl2 = false;
            return (bl || bl2) && livingEntity instanceof PlayerEntity player && player.fishHook != null ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(NETHERITE_BOW, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F);
        ModelPredicateProviderRegistry.register(NETHERITE_BOW, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : CrossbowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float) CrossbowItem.getPullTime(itemStack));
        ModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("charged"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("firework"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0.0F : CrossbowItem.isCharged(itemStack) && CrossbowItem.hasProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(NETHERITE_TRIDENT, new Identifier("throwing"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
    }
}
