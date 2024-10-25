package com.iafenvoy.netherite.item.impl.fabric;

import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricBannerShieldItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;

public class NetheriteShieldItemImpl extends FabricBannerShieldItem {
    public NetheriteShieldItemImpl(Settings settings) {
        super(settings, 5 * 20, ToolMaterials.NETHERITE);
    }

    public static Item create(Item.Settings settings) {
        return new NetheriteShieldItemImpl(settings);
    }
}
