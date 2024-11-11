package com.iafenvoy.netherite.item.impl.neoforge;

import com.iafenvoy.netherite.item.impl.NetheriteElytraItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NetheriteElytraItemImpl extends NetheriteElytraItem {
    protected NetheriteElytraItemImpl(Settings settings) {
        super(settings);
    }

    public static NetheriteElytraItem create(Settings settings) {
        return new NetheriteElytraItemImpl(settings);
    }

    @Override
    public boolean canElytraFly(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
         return ElytraItem.isUsable(stack);
    }
}
