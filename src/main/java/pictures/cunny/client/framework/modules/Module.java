package pictures.cunny.client.framework.modules;

import imgui.type.ImBoolean;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.framework.modules.settings.SettingGroup;
import pictures.cunny.client.utility.StringUtils;

import java.util.List;

public class Module {
    protected static final Minecraft mc = Cunny.mc;
    public final ImBoolean active = new ImBoolean(false);
    public final Category category;
    public final String name;
    private final Runnable onEnabled;
    private final Runnable onDisabled;
    @Getter
    private final List<SettingGroup> settingGroups = new ObjectArrayList<>(1);
    public int keybind = -1;
    public SettingGroup coreGroup = add(new SettingGroup("Core",
            "Core module settings."));
    @Getter
    @Setter
    private String label;
    private String description;

    public Module(Category category, String name, String description) {
        this(category, name, description, null, null);
    }

    public Module(Category category, String name, String description, @Nullable Runnable onEnabled, @Nullable Runnable onDisabled) {
        this.category = category;
        this.name = name;
        this.label = StringUtils.readable(name);
        this.onEnabled = onEnabled;
        this.onDisabled = onDisabled;
        this.description = description;
    }

    protected void enabled() {
        Cunny.EVENT_HANDLER.add(this);
        if (onEnabled != null) {
            onEnabled.run();
        }
    }

    protected void disabled() {
        Cunny.EVENT_HANDLER.remove(this);
        if (onDisabled != null) {
            onDisabled.run();
        }
    }

    public void toggled() {
        Cunny.log("Module {} was {}", name, this.active.get() ? "enabled." : "disabled.");
        if (this.active.get()) {
            enabled();
        } else {
            disabled();
        }
    }

    public String description() {
        return description;
    }

    public String description(String description) {
        return this.description = description;
    }

    public void active(boolean active) {
        if (this.active.get() != active) {
            if (active) {
                enabled();
            } else {
                disabled();
            }

            Cunny.log("Module {} was {}", name, active ? "enabled." : "disabled.");
        }

        this.active.set(active);
    }

    public boolean active() {
        return this.active.get();
    }

    public SettingGroup add(SettingGroup sg) {
        settingGroups.add(sg);
        return sg;
    }

    public void keyPressed(int key) {
        if (key == keybind) {
            active(!active.get());
        }
    }

    public void load() {
        Cunny.MODULES.add(this);
    }
}
