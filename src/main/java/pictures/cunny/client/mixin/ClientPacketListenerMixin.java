package pictures.cunny.client.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.impl.events.game.BlockModifyEvent;
import pictures.cunny.client.impl.modules.exploits.SneakyDisconnect;

import static pictures.cunny.client.Cunny.mc;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "isAcceptingMessages", at = @At("RETURN"), cancellable = true)
    public void isAcceptingMessages(CallbackInfoReturnable<Boolean> cir) {
        if (mc.player == null || !Cunny.MODULES.exists(SneakyDisconnect.class))
            return;
        if (Cunny.MODULES.isActive(SneakyDisconnect.class)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleBlockUpdate", at = @At("TAIL"))
    public void handleBlockUpdate(ClientboundBlockUpdatePacket clientboundBlockUpdatePacket, CallbackInfo ci) {
        Cunny.EVENT_HANDLER.call(BlockModifyEvent.Update.get(clientboundBlockUpdatePacket.getBlockState(),
                clientboundBlockUpdatePacket.getPos()));
    }
}
