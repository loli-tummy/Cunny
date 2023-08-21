package pictures.cunny.client.impl.modules.movement;

import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.impl.events.game.PacketEvent;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;

public class DeSyncInputs extends Module {
    public DeSyncInputs() {
        super(Categories.MOVEMENT, "de-sync-inputs", "De-synchronize input packets from the server.");
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        if (mc.player == null)
            return;

        if (event.packet instanceof ServerboundPlayerInputPacket packet) {
            event.packet = new ServerboundPlayerInputPacket(0, 0, packet.isJumping(), packet.isShiftKeyDown());
        }
    }
}
