package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.NetheriteExtensionClient;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onEntitySpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onSpawnPacket(Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;)V"))
    public void onEntitySpawnMixin(EntitySpawnS2CPacket packet, CallbackInfo ci, @Local Entity entity) {
        if (entity.getType() == EntityType.TRIDENT)
            ((TridentEntity) entity).tridentStack = new ItemStack(Registries.ITEM.get(NetheriteExtensionClient.TRIDENT_QUEUE.remove()));
    }
}
