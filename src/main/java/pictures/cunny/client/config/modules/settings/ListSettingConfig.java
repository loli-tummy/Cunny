package pictures.cunny.client.config.modules.settings;

import org.jetbrains.annotations.NotNull;
import pictures.cunny.client.framework.modules.settings.ListSetting;
import pictures.cunny.client.framework.modules.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public class ListSettingConfig extends SettingConfig<List<String>> {
    @Override
    public <S> void fromSetting(@NotNull Setting<?, S> setting) {
        ListSetting<?, S> listSetting = (ListSetting<?, S>) setting;
        this.value = new ArrayList<>();
        for (S v : listSetting.value()) {
            this.value.add(listSetting.itemToString(v));
        }
    }

    @Override
    public <S> void populateSetting(@NotNull Setting<?, S> setting) {
        ListSetting<?, S> listSetting = (ListSetting<?, S>) setting;
        listSetting.value().clear();
        for (String v : value) {
            listSetting.value().add(listSetting.parseItem(v));
        }
    }
}
