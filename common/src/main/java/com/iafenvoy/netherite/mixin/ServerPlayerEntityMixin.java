package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.registry.NetheriteItems;
import com.iafenvoy.netherite.registry.NetheriteStats;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow
    public abstract void increaseStat(Stat<?> stat, int amount);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 7))
    private void increaseTravelMotionStats(double dx, double dy, double dz, CallbackInfo ci) {
        boolean hasNetheriteElytra = false;
        for (ItemStack item : this.getArmorItems())
            hasNetheriteElytra |= item.isOf(NetheriteItems.NETHERITE_ELYTRA.get());
        if (!hasNetheriteElytra) return;
        this.increaseStat(NetheriteStats.FLY_NETHERITE_ELYTRA_ONE_CM, Math.round((float) Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F));
    }
}
