package com.iafenvoy.netherite.entity;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.registry.NetheriteItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class NetheriteTridentEntity extends TridentEntity {
    public NetheriteTridentEntity(EntityType<? extends TridentEntity> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(NetheriteItems.NETHERITE_TRIDENT.get());
    }

    public NetheriteTridentEntity(World world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
        this.stack = stack;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 8.0F;
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.getDamageSources().trident(this, entity2 == null ? this : entity2);
        if (this.getWorld() instanceof ServerWorld serverWorld)
            f = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, f);
        f = (float) (f * NetheriteExtensionConfig.INSTANCE.damage.trident_damage_multiplier + NetheriteExtensionConfig.INSTANCE.damage.trident_damage_addition);
        this.dealtDamage = true;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) return;
            if (this.getWorld() instanceof ServerWorld serverWorld)
                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack());
            if (entity instanceof LivingEntity livingEntity) {
                this.knockback(livingEntity, damageSource);
                this.onHit(livingEntity);
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
    }
}
