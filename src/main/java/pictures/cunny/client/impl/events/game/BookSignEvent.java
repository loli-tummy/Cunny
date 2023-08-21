package pictures.cunny.client.impl.events.game;

import pictures.cunny.client.framework.events.Event;

import java.util.List;

public class BookSignEvent extends Event {
    public static BookSignEvent INSTANCE = new BookSignEvent();
    public List<String> pages;
    public String title;
    public boolean isModified;
    public boolean stop = false;

    public BookSignEvent() {
        super(true);
    }

    public static BookSignEvent get(List<String> pages, String title, boolean isModified) {
        INSTANCE.stop = false;
        INSTANCE.pages = pages;
        INSTANCE.title = title;
        INSTANCE.isModified = isModified;
        return INSTANCE;
    }

    public void stop() {
        stop = true;
    }
}
