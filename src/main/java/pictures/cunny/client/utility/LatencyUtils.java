package pictures.cunny.client.utility;

import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.impl.events.game.PacketEvent;
import pictures.cunny.client.impl.events.game.ScreenEvent;
import pictures.cunny.client.impl.events.game.TickEvent;

import java.util.ArrayList;

import static pictures.cunny.client.Cunny.mc;

public class LatencyUtils {
    public static final ArrayList<Integer> pings = new ArrayList<>();
    private static boolean expectingPing = false;
    private static long lastRequested = 0;
    private static int ping = 0;

    public static void addPing(int ping) {
        if (pings.size() >= 5000) pings.remove(pings.size() - 1);
        pings.add(0, ping);
    }

    public static void preformPing() {
        if (expectingPing || mc.player == null || mc.isSingleplayer()) return;
        assert mc.player != null;
        PacketUtils.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
        expectingPing = true;
    }

    public static void calculateLatency() {
        if (pings.size() < 10) {
            ping = 0;
            return;
        }

        int sum = 0;
        for (int i : pings) {
            sum += i;
        }
        ping = sum / pings.size();
    }

    public static int getLatency() {
        return ping;
    }

    @EventListener
    public void onPacketReceive(PacketEvent.Received event) {
        if (event.packet instanceof ClientboundAwardStatsPacket && lastRequested != 0) {
            addPing(Math.toIntExact(System.currentTimeMillis() - lastRequested));
            lastRequested = 0;
        }
    }

    @EventListener
    public void onPacketSent(PacketEvent.Sent event) {
        if (event.packet instanceof ServerboundClientCommandPacket packet) {
            if (packet.getAction() == ServerboundClientCommandPacket.Action.REQUEST_STATS && expectingPing) {
                lastRequested = System.currentTimeMillis();
                expectingPing = false;
            }
        }
    }

    @EventListener
    public void onScreenChange(ScreenEvent.Open event) {
        lastRequested = 0;
        expectingPing = false;
    }

    @EventListener
    public void onTick(TickEvent.Pre event) {
        preformPing();
    }
}
