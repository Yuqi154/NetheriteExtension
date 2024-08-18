package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.NetheriteExtension;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileEntity.class)
public abstract class TridentEntityMixin extends Entity {
    public TridentEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createSpawnPacket", at = @At("HEAD"))
    public void sendTridentStackOnSpawn(CallbackInfoReturnable<Packet<?>> info) {
        if ((Object) this instanceof TridentEntity tridentEntity) {
            PacketByteBuf passedData = PacketByteBufs.create();
            passedData.writeInt(Registries.ITEM.getRawId(tridentEntity.tridentStack.getItem()));
            for (ServerPlayerEntity player : this.getWorld().getServer().getPlayerManager().getPlayerList())
                ServerPlayNetworking.send(player, new Identifier(NetheriteExtension.MOD_ID, "netherite_trident"), passedData);
        }
    }
}
