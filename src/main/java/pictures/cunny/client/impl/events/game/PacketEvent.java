package pictures.cunny.client.impl.events.game;

import pictures.cunny.client.framework.events.Event;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public abstract class PacketEvent extends Event {
    public Packet<? extends PacketListener> packet;


    public PacketEvent() {
        super(false);
    }

    public PacketEvent(boolean cancellable) {
        super(cancellable);
    }

    public static class Received extends PacketEvent {
        public static Received INSTANCE = new Received();

        public Received() {
            super(true);
            INSTANCE = this;
        }

        public static Received get(Packet<? extends PacketListener> packet) {
            INSTANCE.packet = packet;
            return INSTANCE;
        }
    }

    public static class Send extends PacketEvent {
        public static Send INSTANCE = new Send();

        public Send() {
            super(true);
            INSTANCE = this;
        }

        public static Send get(Packet<? extends PacketListener> packet) {
            INSTANCE.packet = packet;
            return INSTANCE;
        }
    }

    public static class Sent extends PacketEvent {
        public static Sent INSTANCE = new Sent();

        public Sent() {
            super();
            INSTANCE = this;
        }

        public static Sent get(Packet<? extends PacketListener> packet) {
            INSTANCE.packet = packet;
            return INSTANCE;
        }
    }
}
