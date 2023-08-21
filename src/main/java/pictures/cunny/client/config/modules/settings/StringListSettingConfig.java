package pictures.cunny.client.config.modules.settings;

import imgui.type.ImString;
import org.jetbrains.annotations.NotNull;
import pictures.cunny.client.framework.modules.settings.Setting;

import java.util.List;

public class StringListSettingConfig extends SettingConfig<List<ImString>> {
    @Override
    public <S> void fromSetting(@NotNull Setting<?, S> setting) {
        this.value = (List<ImString>) setting.value();
    }

    @Override
    public <S> void populateSetting(@NotNull Setting<?, S> setting) {
        setting.setGenericValue(setting.value());
    }
}
