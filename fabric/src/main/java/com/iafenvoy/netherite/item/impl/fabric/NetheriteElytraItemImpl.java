package com.iafenvoy.netherite.item.impl.fabric;

import com.iafenvoy.netherite.item.impl.NetheriteElytraItem;
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;

public class NetheriteElytraItemImpl extends NetheriteElytraItem implements FabricElytraItem {
    protected NetheriteElytraItemImpl(Settings settings) {
        super(settings);
    }

    public static NetheriteElytraItem create(Settings settings) {
        return new NetheriteElytraItemImpl(settings);
    }
}
