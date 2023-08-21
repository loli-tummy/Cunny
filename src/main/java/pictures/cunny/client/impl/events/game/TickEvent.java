package pictures.cunny.client.impl.events.game;

import pictures.cunny.client.framework.events.Event;

public class TickEvent extends Event {

    public static class Pre extends Event {
        private static final Pre INSTANCE = new Pre();

        public static Pre get() {
            return INSTANCE;
        }
    }

    public static class Post extends Event {
        private static final Post INSTANCE = new Post();

        public static Post get() {
            return INSTANCE;
        }
    }
}
