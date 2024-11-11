package com.iafenvoy.netherite.item.impl.neoforge;

import net.minecraft.item.Item;
import net.minecraft.item.ShieldItem;

public class NetheriteShieldItemImpl extends ShieldItem {
    public NetheriteShieldItemImpl(Item.Settings settings) {
        super(settings);
    }

    public static Item create(Item.Settings settings) {
        return new NetheriteShieldItemImpl(settings);
    }
}
