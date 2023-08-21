package pictures.cunny.client.config.modules.settings;

import pictures.cunny.client.framework.modules.settings.Setting;
import org.jetbrains.annotations.NotNull;

public class SettingConfig<T> {
    public T value;

    public <S> void fromSetting(@NotNull Setting<?, S> setting) {

    }

    public <S> void populateSetting(@NotNull Setting<?, S> setting) {

    }
}
