package pictures.cunny.client.mixin;

import io.netty.channel.Channel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.impl.events.game.DisconnectEvent;
import pictures.cunny.client.impl.events.game.PacketEvent;
import pictures.cunny.client.impl.modules.exploits.LoginFuckery;
import pictures.cunny.client.impl.modules.exploits.SneakyDisconnect;
import pictures.cunny.client.utility.PacketUtils;

import static pictures.cunny.client.Cunny.mc;

@Mixin(value = Connection.class, priority = -999999)
public abstract class ConnectionMixin {

    @Shadow
    public Channel channel;

    @Inject(method = "genericsFtw", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void handlePacketHead(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if (listener instanceof ClientPacketListener) {
            PacketEvent.Received event = PacketEvent.Received.get(packet);
            Cunny.EVENT_HANDLER.call(event);
            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Shadow
    public abstract void setProtocol(ConnectionProtocol connectionProtocol);

    @Inject(method = "disconnect", at = @At("HEAD"), cancellable = true)
    public void disconnect(Component component, CallbackInfo ci) {
        DisconnectEvent event = DisconnectEvent.get(component);
        Cunny.EVENT_HANDLER.call(event);
        if (event.isCancelled()) {
            if (Cunny.MODULES.isActive(SneakyDisconnect.class)) {
                channel = null;
            }
            ci.cancel();
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V", at = @At("HEAD"), cancellable = true)
    public void sendHead(Packet<?> packet, @Nullable PacketSendListener packetSendListener, CallbackInfo ci) {
        if (Cunny.MODULES.isActive(LoginFuckery.class) && packet instanceof ClientIntentionPacket) {
            ci.cancel();
            return;
        }

        PacketEvent.Send event = PacketEvent.Send.get(packet);

        Cunny.EVENT_HANDLER.call(event);

        if (event.isCancelled()) {
            ci.cancel();
            return;
        }

        if (mc.player != null) {
            PacketUtils.send((Connection) (Object) this, event.packet);
            ci.cancel();
        }

        Cunny.EVENT_HANDLER.call(PacketEvent.Sent.get(event.packet));
    }
}
