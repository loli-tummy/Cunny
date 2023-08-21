package pictures.cunny.client.impl.events.game;

import pictures.cunny.client.framework.events.Event;
import net.minecraft.client.gui.screens.Screen;

public class ScreenEvent extends Event {
    public Screen screen;

    public ScreenEvent() {
        super(true);
    }

    public static class Open extends ScreenEvent {
        public static Open INSTANCE = new Open();

        public Open() {
            INSTANCE = this;
        }

        public static Open get(Screen screen) {
            INSTANCE.screen = screen;
            return INSTANCE;
        }
    }
}
