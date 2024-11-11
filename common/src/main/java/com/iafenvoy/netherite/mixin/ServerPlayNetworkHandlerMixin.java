package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.screen.NetheriteAnvilScreenHandler;
import net.minecraft.SharedConstants;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onRenameItem", at = @At("RETURN"))
    public void onRenameItem(RenameItemC2SPacket packet, CallbackInfo info) {
        if (((ServerPlayNetworkHandler) (Object) this).player.currentScreenHandler instanceof NetheriteAnvilScreenHandler anvilScreenHandler) {
            String string = packet.getName();
            if (string.length() <= 35)
                anvilScreenHandler.setNewItemName(string);
        }
    }
}
