package com.iafenvoy.netherite.item;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class NetheriteBowItem extends BowItem {
    public NetheriteBowItem(Settings settings) {
        super(settings);
    }

    @Override
    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        Item var8 = projectileStack.getItem();
        ArrowItem var10000;
        if (var8 instanceof ArrowItem arrowItem) var10000 = arrowItem;
        else var10000 = (ArrowItem) Items.ARROW;
        ArrowItem arrowItem2 = var10000;
        PersistentProjectileEntity persistentProjectileEntity = arrowItem2.createArrow(world, projectileStack, shooter, weaponStack);
        this.modifyBaseDamage(persistentProjectileEntity);
        if (critical) persistentProjectileEntity.setCritical(true);
        return persistentProjectileEntity;
    }

    public void modifyBaseDamage(PersistentProjectileEntity projectile) {
        projectile.setDamage(projectile.getDamage() * NetheriteExtensionConfig.INSTANCE.damage.bow_damage_multiplier + NetheriteExtensionConfig.INSTANCE.damage.bow_damage_addition);
    }
}
