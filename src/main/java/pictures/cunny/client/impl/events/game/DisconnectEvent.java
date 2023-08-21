package pictures.cunny.client.impl.events.game;

import pictures.cunny.client.framework.events.Event;
import net.minecraft.network.chat.Component;

public class DisconnectEvent extends Event {
    public static DisconnectEvent INSTANCE = new DisconnectEvent();
    public Component reason;

    public DisconnectEvent() {
        super(true);
    }


    public static DisconnectEvent get(Component component) {
        INSTANCE.reason = component;
        return INSTANCE;
    }
}
