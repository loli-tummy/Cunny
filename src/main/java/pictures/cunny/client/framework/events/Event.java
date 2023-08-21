package pictures.cunny.client.framework.events;

public class Event {
    private static String id;
    private final boolean cancellable;
    private boolean cancel = false;

    public Event() {
        this(false);
    }

    public Event(boolean cancellable) {
        this.cancellable = cancellable;
    }

    public boolean cancel() {
        if (cancellable) {
            cancel = !cancel;
            return cancel;
        }
        return false;
    }

    public boolean isCancelled() {
        return cancellable && cancel;
    }

    public boolean isCancellable() {
        return cancellable;
    }
}
