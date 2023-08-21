package pictures.cunny.client.config.modules.settings;

import imgui.type.ImString;
import pictures.cunny.client.framework.modules.settings.Setting;
import org.jetbrains.annotations.NotNull;

public class StringSettingConfig extends SettingConfig<ImString> {
    @Override
    public <S> void fromSetting(@NotNull Setting<?, S> setting) {
        this.value = (ImString) setting.value();
    }

    @Override
    public <S> void populateSetting(@NotNull Setting<?, S> setting) {
        setting.setGenericValue(value);
    }
}
