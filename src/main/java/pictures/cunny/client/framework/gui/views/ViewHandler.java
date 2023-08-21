package pictures.cunny.client.framework.gui.views;

import imgui.type.ImBoolean;
import pictures.cunny.client.config.Config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ViewHandler {
    protected static final Map<Class<? extends View>, ImBoolean> VIEW_STATES = new HashMap<>();
    protected static final Map<Class<? extends View>, View> VIEWS = new HashMap<>();

    public static void add(View view) {
        VIEWS.put(view.getClass(), view);
        view.populateSettings(Config.get());
        VIEW_STATES.put(view.getClass(), view.settings.isLoaded);
    }

    public static ImBoolean state(Class<? extends View> view) {
        VIEW_STATES.putIfAbsent(view, new ImBoolean(false));
        return VIEW_STATES.get(view);
    }

    public static View get(Class<? extends View> view) {
        return VIEWS.get(view);
    }

    public static Collection<View> getViews() {
        return VIEWS.values();
    }

    public static void showAll() {
        for (View view : VIEWS.values()) {
            if (state(view.getClass()).get()) {
                view.show();
            }
        }
    }
}
