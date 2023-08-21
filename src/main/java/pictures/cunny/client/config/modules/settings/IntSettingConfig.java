package pictures.cunny.client.config.modules.settings;

import imgui.type.ImInt;
import org.jetbrains.annotations.NotNull;
import pictures.cunny.client.framework.modules.settings.Setting;

public class IntSettingConfig extends SettingConfig<ImInt> {
    @Override
    public <S> void fromSetting(@NotNull Setting<?, S> setting) {
        this.value = (ImInt) setting.value();
    }

    @Override
    public <S> void populateSetting(@NotNull Setting<?, S> setting) {
        setting.setGenericValue(setting.value());
    }
}
