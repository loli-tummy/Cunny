package pictures.cunny.client.config.modules.settings;

import imgui.type.ImBoolean;
import org.jetbrains.annotations.NotNull;
import pictures.cunny.client.framework.modules.settings.Setting;

public class BoolSettingConfig extends SettingConfig<ImBoolean> {
    @Override
    public <S> void fromSetting(@NotNull Setting<?, S> setting) {
        this.value = (ImBoolean) setting.value();
    }

    @Override
    public <S> void populateSetting(@NotNull Setting<?, S> setting) {
        setting.setGenericValue(value);
    }
}
