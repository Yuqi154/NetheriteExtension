package com.iafenvoy.netherite.item;

import com.iafenvoy.netherite.NetheriteExtension;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.util.Identifier;

public class NetheriteHorseArmorItem extends HorseArmorItem {
    public NetheriteHorseArmorItem(int bonus, Settings settings) {
        super(bonus, "diamond", settings);
    }

    @Override
    public Identifier getEntityTexture() {
        return new Identifier(NetheriteExtension.MOD_ID, "textures/entity/netherite_horse_armor.png");
    }
}
