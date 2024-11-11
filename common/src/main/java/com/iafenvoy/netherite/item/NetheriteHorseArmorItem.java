package com.iafenvoy.netherite.item;

import com.iafenvoy.netherite.NetheriteExtension;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.util.Identifier;

public class NetheriteHorseArmorItem extends AnimalArmorItem {
    public NetheriteHorseArmorItem(Settings settings) {
        super(ArmorMaterials.NETHERITE, AnimalArmorItem.Type.EQUESTRIAN, false, settings);
    }

    @Override
    public Identifier getEntityTexture() {
        return new Identifier(NetheriteExtension.MOD_ID, "textures/entity/netherite_horse_armor.png");
    }
}
