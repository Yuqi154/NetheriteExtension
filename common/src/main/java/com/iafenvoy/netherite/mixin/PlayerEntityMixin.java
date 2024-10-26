package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.registry.NetheriteItems;
import com.iafenvoy.netherite.registry.NetheriteStats;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void increaseStat(Identifier stat, int amount);

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 7))
    private void increaseTravelMotionStats(double dx, double dy, double dz, CallbackInfo ci) {
        boolean hasNetheriteElytra = false;
        for (ItemStack item : this.getArmorItems())
            hasNetheriteElytra |= item.isOf(NetheriteItems.NETHERITE_ELYTRA.get());
        if (!hasNetheriteElytra) return;
        this.increaseStat(NetheriteStats.FLY_NETHERITE_ELYTRA_ONE_CM, Math.round((float) Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F));
    }
}
