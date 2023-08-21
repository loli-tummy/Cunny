package pictures.cunny.client.impl.modules.movement;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.BoolSetting;
import pictures.cunny.client.framework.modules.settings.SettingGroup;
import pictures.cunny.client.impl.events.game.PacketEvent;
import pictures.cunny.client.impl.events.game.TickEvent;
import pictures.cunny.client.utility.PacketUtils;

public class Sneak extends Module {
    public SettingGroup coreGroup = add(new SettingGroup("Core",
            "Core module settings."));

    public final BoolSetting silent = new BoolSetting.Builder()
            .name("silent")
            .description("Only sneak on the server.")
            .defaultTo(false)
            .addTo(coreGroup);
    public final BoolSetting bypassUse = new BoolSetting.Builder()
            .name("bypass-use")
            .description("A bypass to use containers and entities")
            .defaultTo(true)
            .addTo(coreGroup);

    public Sneak() {
        super(Categories.MOVEMENT, "sneak", "Automatically sneaks.");
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        if (mc.player == null)
            return;

        if (event.packet instanceof ServerboundPlayerInputPacket packet) {
            event.packet = new ServerboundPlayerInputPacket(packet.getXxa(), packet.getZza(), packet.isJumping(), true);
        }

        if (event.packet instanceof ServerboundInteractPacket && bypassUse.value().get()) {
            PacketUtils.send(new ServerboundPlayerInputPacket(mc.player.xxa, mc.player.zza, mc.player.jumping, false));
            PacketUtils.send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY));
            event.cancel();
        }
    }

    @EventListener
    public void onTick(TickEvent.Post event) {
        if (mc.player == null)
            return;

        PacketUtils.send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY));

        mc.player.setShiftKeyDown(!silent.value().get());
    }
}
