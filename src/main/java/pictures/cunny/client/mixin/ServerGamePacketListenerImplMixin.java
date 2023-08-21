package pictures.cunny.client.mixin;

import pictures.cunny.client.Cunny;
import pictures.cunny.client.impl.modules.exploits.SneakyDisconnect;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Inject(method = "isAcceptingMessages", at = @At("RETURN"), cancellable = true)
    public void isAcceptingMessages(CallbackInfoReturnable<Boolean> cir) {
        if (Cunny.MODULES.isActive(SneakyDisconnect.class)) {
            cir.setReturnValue(false);
        }
    }
}
