package pictures.cunny.client.framework.modules.settings;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class SettingGroup {
    public final String name;
    protected final List<Setting<?, ?>> SETTINGS = new ObjectArrayList<>();
    private String description;
    @Setter
    private Predicate<SettingGroup> visible = (group) -> true;

    public SettingGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String description() {
        return description;
    }

    public String description(String v) {
        return description = v;
    }

    public <S> S add(S setting) {
        SETTINGS.add((Setting<S, ?>) setting);
        return setting;
    }

    public Iterator<Setting<?, ?>> get() {
        return SETTINGS.iterator();
    }

    public boolean isVisible() {
        return visible.test(this);
    }
}
