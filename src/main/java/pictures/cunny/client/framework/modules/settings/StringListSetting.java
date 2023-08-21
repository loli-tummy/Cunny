package pictures.cunny.client.framework.modules.settings;

import imgui.type.ImString;
import pictures.cunny.client.config.modules.settings.ListSettingConfig;

import java.util.List;

public class StringListSetting extends Setting<StringListSetting, List<ImString>> {
    public StringListSetting(SettingBuilder<StringListSetting, Builder, List<ImString>> builder) {
        super(builder);
        this.settingConfig = new ListSettingConfig();
    }

    public static class Builder extends Setting.SettingBuilder<StringListSetting, Builder, List<ImString>> {
        @Override
        public StringListSetting build() {
            check();
            return new StringListSetting(this);
        }
    }
}
