package pictures.cunny.client.framework.modules.settings;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import pictures.cunny.client.config.modules.settings.StringSettingConfig;
import org.jetbrains.annotations.NotNull;

public class StringSetting extends Setting<StringSetting, ImString> {
    public StringSetting(Builder builder) {
        super(builder);
        this.settingConfig = new StringSettingConfig();
    }

    public ImString value(String value) {
        value().set(value);
        return value();
    }

    @Override
    public void render() {
        ImGui.inputText(label(), value(), ImGuiInputTextFlags.CallbackResize);
    }

    public static class Builder extends SettingBuilder<StringSetting, Builder, ImString> {
        public Builder defaultTo(@NotNull String v) {
            this.defaultTo(new ImString(v));
            return this;
        }

        @Override
        public StringSetting build() {
            check();
            return new StringSetting(this);
        }
    }
}
