package pictures.cunny.client.impl.modules.misc;

import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.BoolSetting;
import pictures.cunny.client.framework.modules.settings.SettingGroup;
import pictures.cunny.client.impl.events.game.PacketEvent;
import pictures.cunny.client.utility.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class Rotation extends Module {
    public SettingGroup bypassGroup = add(new SettingGroup("Bypassing",
            "Settings for bypasses."));
    public final BoolSetting bypass = new BoolSetting.Builder()
            .name("bypass")
            .description("Bypass server rotations.")
            .defaultTo(true)
            .addTo(bypassGroup);
    public final BoolSetting bypassUpdate = new BoolSetting.Builder()
            .name("update")
            .description("Inform the server of your rotations.")
            .defaultTo(true)
            .addTo(bypassGroup);

    public Rotation() {
        super(Categories.MISC, "rotations", "Allows you to modify how you rotate.");
    }

    @EventListener
    public void onPacketSend(PacketEvent.Received event) {
        if (bypass.value().get() && event.packet instanceof ClientboundPlayerLookAtPacket) {
            event.cancel();
            if (bypassUpdate.value().get()) {
                assert mc.player != null;
                PacketUtils.send(new ServerboundMovePlayerPacket.Rot(mc.player.getYRot(), mc.player.getXRot(),
                        mc.player.onGround()));
            }
        }
    }
}
