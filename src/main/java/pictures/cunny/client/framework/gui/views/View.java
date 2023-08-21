package pictures.cunny.client.framework.gui.views;

import imgui.type.ImBoolean;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.config.gui.PersistentGuiSettings;

public abstract class View {
    public PersistentGuiSettings settings = new PersistentGuiSettings();

    public abstract String name();

    public abstract void show();

    public abstract void populateSettings(Config config);

    public ImBoolean state() {
        return ViewHandler.state(getClass());
    }

    public void load() {
        ViewHandler.add(this);
    }
}
