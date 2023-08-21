package pictures.cunny.client.impl.modules.chat;

import net.minecraft.network.protocol.game.ClientboundDeleteChatPacket;
import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.impl.events.game.PacketEvent;

public class BetterChat extends Module {


    public BetterChat() {
        super(Categories.CHAT, "better-chat", "Improves and modifies in-game chat in various ways.");
    }

    @EventListener
    public void onPacketReceived(PacketEvent.Received event) {
        if (event.packet instanceof ClientboundDeleteChatPacket) {

        }
    }
}
