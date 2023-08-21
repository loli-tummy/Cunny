package pictures.cunny.client.utility;

import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;
import pictures.cunny.client.Cunny;

import java.awt.*;

import static net.minecraft.network.Connection.ATTRIBUTE_PROTOCOL;
import static pictures.cunny.client.Cunny.mc;

public class PacketUtils {
    public static void send(Packet<?> packet) {
        if (mc.player != null)
            send(mc.player.connection.getConnection(), packet);
        else if (mc.pendingConnection != null)
            send(mc.pendingConnection, packet);
        else
            Cunny.log("Go fuck yourself!");
    }

    public static void send(@NotNull Connection connection, Packet<?> packet) {
        if (connection.channel.attr(ATTRIBUTE_PROTOCOL).get()
                != ConnectionProtocol.getProtocolForPacket(packet)) {
            Cunny.log(Color.RED, "");
            return;
        }

        connection.channel.writeAndFlush(packet);
    }

    public static void chat(String string, boolean command) {
        if (command)
            command(string);
        else
            chat(string);
    }

    public static void chat(String string) {
        if (string == null
                || string.isBlank()
                || ServerGamePacketListenerImpl.isChatMessageIllegal(string)
                || mc.getConnection() == null)
            return;

        if (string.startsWith("/"))
            command(string.replaceFirst("/", ""));
        else
            mc.getConnection().sendChat(string);
    }

    public static void command(String string) {
        if (string == null
                || string.isBlank()
                || ServerGamePacketListenerImpl.isChatMessageIllegal(string)
                || mc.getConnection() == null)
            return;

        mc.getConnection().sendCommand(string);
    }
}
