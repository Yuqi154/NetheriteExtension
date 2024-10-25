package com.iafenvoy.netherite.item.impl.forge;

import com.iafenvoy.netherite.item.impl.NetheriteElytraItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;

public class NetheriteElytraItemImpl extends NetheriteElytraItem {
    protected NetheriteElytraItemImpl(Settings settings) {
        super(settings);
    }

    public static NetheriteElytraItem create(Settings settings) {
        return new NetheriteElytraItemImpl(settings);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return ElytraItem.isUsable(stack);
    }
}
