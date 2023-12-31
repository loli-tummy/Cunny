package pictures.cunny.client.config.modules.settings;

import org.jetbrains.annotations.NotNull;
import pictures.cunny.client.framework.modules.settings.Setting;
import pictures.cunny.client.framework.modules.settings.wrappers.ImBlockPos;

public class BlockPosSettingConfig extends SettingConfig<ImBlockPos> {
    @Override
    public <S> void fromSetting(@NotNull Setting<?, S> setting) {
        this.value = (ImBlockPos) setting.value();
    }

    @Override
    public <S> void populateSetting(@NotNull Setting<?, S> setting) {
        setting.setGenericValue(setting.value());
    }
}
