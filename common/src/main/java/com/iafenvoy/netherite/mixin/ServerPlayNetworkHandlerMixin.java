package com.iafenvoy.netherite.mixin;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.network.LavaVisionUpdatePacket;
import com.iafenvoy.netherite.screen.NetheriteAnvilScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.SharedConstants;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        NetheriteExtension.CONNECTED_CLIENTS.add(player);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(NetheriteExtensionConfig.getInstance().graphics.lava_vision_distance);
        ServerPlayNetworking.send(player, LavaVisionUpdatePacket.ID, buf);
    }

    @Inject(method = "disconnect", at = @At("RETURN"))
    public void disconnect(Text reason, CallbackInfo info) {
        NetheriteExtension.CONNECTED_CLIENTS.remove(((ServerPlayNetworkHandler) (Object) this).player);
    }

    @Inject(method = "onRenameItem", at = @At("RETURN"))
    public void onRenameItem(RenameItemC2SPacket packet, CallbackInfo info) {
        if (((ServerPlayNetworkHandler) (Object) this).player.currentScreenHandler instanceof NetheriteAnvilScreenHandler anvilScreenHandler) {
            String string = SharedConstants.stripInvalidChars(packet.getName());
            if (string.length() <= 35)
                anvilScreenHandler.setNewItemName(string);
        }
    }
}
