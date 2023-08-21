package pictures.cunny.client.framework.modules.settings;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImString;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.jetbrains.annotations.NotNull;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.config.modules.settings.ListSettingConfig;
import pictures.cunny.client.utility.StringUtils;

import java.util.List;
import java.util.function.Predicate;

public abstract class ListSetting<S, T> extends Setting<S, ObjectList<T>> {
    public final Predicate<T> filter;
    private final ImString textFilter = new ImString();

    public ListSetting(Builder<S, T> builder) {
        super(builder);
        this.settingConfig = new ListSettingConfig();
        this.filter = builder.filter;
        textFilter.inputData.isResizable = true;
    }

    public abstract List<T> getSuggestions();

    @Override
    public void render() {

        ImGui.bulletText(label());

        if (ImGui.checkbox("Hide", Config.get().isHidden(this))) {
            Config.get().hide(this, !Config.get().isHidden(this));
        }

        if (Config.get().isHidden(this)) {
            return;
        }

        ImGui.beginChild(name + "_child", ImGui.getWindowWidth() - 10, ImGui.getWindowHeight() / 2, true);

        ImGui.beginTable(name + "_table", 3,
                ImGuiTableFlags.Borders | ImGuiTableFlags.SizingFixedFit);

        int i = 0;
        for (T entry : value()) {
            i++;
            if (textFilter.isEmpty()
                    || StringUtils.readable(itemToString(entry))
                    .toLowerCase()
                    .contains(textFilter.get().toLowerCase())) {
                ImGui.tableNextColumn();

                ImGui.text(itemToString(entry));

                ImGui.tableNextColumn();
                if (ImGui.button("Remove " + i)) {
                    synchronized (value()) {
                        value().remove(entry);
                    }
                }
            }
        }

        ImGui.endTable();
        ImGui.endChild();

        ImGui.inputTextWithHint("Filter", "dirt", textFilter, ImGuiInputTextFlags.CallbackResize);

        if (getSuggestions() != null) {
            if (ImGui.beginMenu("Suggestions")) {
                boolean isFilterEmpty = textFilter.isEmpty();

                for (T value : getSuggestions()) {
                    if (value().contains(value)) {
                        continue;
                    }

                    String name = StringUtils.readable(itemToString(value));

                    if (isFilterEmpty || name.toLowerCase().contains(textFilter.get().toLowerCase())) {
                        if (ImGui.menuItem(name)) {
                            synchronized (value()) {
                                value().add(value);
                            }
                        }
                    }
                }
                ImGui.endMenu();
            }
        }
    }

    public abstract T parseItem(String v);

    public abstract String itemToString(T v);

    public static class Builder<S, T> extends SettingBuilder<S, Builder<S, T>, ObjectList<T>> {
        private Predicate<T> filter = (item) -> true;

        @Override
        public Builder<S, T> defaultTo(@NotNull ObjectList<T> v) {
            return super.defaultTo(ObjectLists.synchronize(v));
        }

        public Builder<S, T> filter(Predicate<T> v) {
            this.filter = v;
            return this;
        }
    }
}
