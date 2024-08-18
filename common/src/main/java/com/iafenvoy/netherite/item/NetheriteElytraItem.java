package com.iafenvoy.netherite.item;

import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class NetheriteElytraItem extends ArmorItem implements FabricElytraItem {
    public NetheriteElytraItem(Settings settings) {
        super(NetheriteElytraArmorMaterials.NETHERITE_ELYTRA_MATERIAL, Type.CHESTPLATE, settings);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.getItem() == Items.PHANTOM_MEMBRANE;
    }
}
