package com.iafenvoy.netherite.item.impl;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.Item;

public class NetheriteShieldItem {
    @ExpectPlatform
    public static Item create(Item.Settings settings) {
        throw new AssertionError("This method should be replaced by Architectury");
    }
}
