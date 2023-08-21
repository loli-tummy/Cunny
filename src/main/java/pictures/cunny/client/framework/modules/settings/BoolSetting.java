package pictures.cunny.client.framework.modules.settings;

import imgui.ImGui;
import imgui.type.ImBoolean;
import pictures.cunny.client.config.modules.settings.BoolSettingConfig;

public class BoolSetting extends Setting<BoolSetting, ImBoolean> {
    public BoolSetting(Builder builder) {
        super(builder);
        this.settingConfig = new BoolSettingConfig();
    }

    @Override
    public void render() {
        ImGui.checkbox(label(), value());
    }

    public static class Builder extends SettingBuilder<BoolSetting, Builder, ImBoolean> {
        public Builder defaultTo(boolean v) {
            this.defaultTo(new ImBoolean(v));
            return this;
        }

        @Override
        public BoolSetting build() {
            check();
            return new BoolSetting(this);
        }
    }
}
