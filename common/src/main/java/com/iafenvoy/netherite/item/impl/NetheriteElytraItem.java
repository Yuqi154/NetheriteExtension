package com.iafenvoy.netherite.item.impl;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;
import java.util.UUID;

public class NetheriteElytraItem extends ElytraItem {
    protected NetheriteElytraItem(Settings settings) {
        super(settings.component(DataComponentTypes.ATTRIBUTE_MODIFIERS, new AttributeModifiersComponent(List.of(new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), "netherite_elytra", NetheriteExtensionConfig.INSTANCE.damage.elytra_armor_points, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)), true)));
    }

    @ExpectPlatform
    public static NetheriteElytraItem create(Settings settings) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.getItem() == Items.PHANTOM_MEMBRANE;
    }
}
