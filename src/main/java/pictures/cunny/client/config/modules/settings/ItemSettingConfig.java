package pictures.cunny.client.config.modules.settings;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import pictures.cunny.client.framework.modules.settings.Setting;

public class ItemSettingConfig extends SettingConfig<Item> {
    @Override
    public <S> void fromSetting(@NotNull Setting<?, S> setting) {
        this.value = (Item) setting.value();
    }

    @Override
    public <S> void populateSetting(@NotNull Setting<?, S> setting) {
        setting.setGenericValue(setting.value());
    }
}
