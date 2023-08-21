package pictures.cunny.client.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import lombok.SneakyThrows;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.config.adapters.*;
import pictures.cunny.client.config.gui.BookSettings;
import pictures.cunny.client.config.gui.GuiSettings;
import pictures.cunny.client.config.gui.PersistentGuiSettings;
import pictures.cunny.client.config.modules.ModuleConfig;
import pictures.cunny.client.config.modules.SettingGroupConfig;
import pictures.cunny.client.config.sub.Inventory;
import pictures.cunny.client.config.sub.Placing;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Category;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.Setting;
import pictures.cunny.client.framework.modules.settings.SettingGroup;
import pictures.cunny.client.utility.FileSystem;
import pictures.cunny.client.utility.PathIndex;

import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class Config {
    private final static ExclusionStrategy excludeOptional = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType() == Optional.class;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };
    private final static Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .setExclusionStrategies(excludeOptional)
            .registerTypeAdapter(ImInt.class, new ImIntAdapter())
            .registerTypeAdapter(ImBoolean.class, new ImBooleanAdapter())
            .registerTypeAdapter(ImString.class, new ImStringAdapter())
            .registerTypeAdapter(ItemAdapter.class, new ImIntAdapter())
            .registerTypeAdapter(BlockAdapter.class, new ImBooleanAdapter())
            .setPrettyPrinting()
            .create();
    public static Config INSTANCE;
    public Inventory inventory = new Inventory();
    public Placing placing = new Placing();
    public GuiSettings guiSettings = new GuiSettings();
    public PersistentGuiSettings moduleConfigGui = new PersistentGuiSettings();
    public PersistentGuiSettings modulesGui = new PersistentGuiSettings();
    public PersistentGuiSettings logsGui = new PersistentGuiSettings();
    public PersistentGuiSettings booksGui = new PersistentGuiSettings();
    public BookSettings bookSettings = new BookSettings();
    public PersistentGuiSettings latencyGui = new PersistentGuiSettings();
    public Map<String, ModuleConfig> modules = new HashMap<>();

    public static Config get() {
        return INSTANCE;
    }

    public void hide(Setting<?, ?> setting, boolean state) {
        guiSettings.hiddenLists.put(setting.name, state);
    }

    public boolean isHidden(Setting<?, ?> setting) {
        guiSettings.hiddenLists.putIfAbsent(setting.name, false);
        return guiSettings.hiddenLists.get(setting.name);
    }

    public void save() {
        Config.INSTANCE.modules.clear();
        for (Category category : Categories.INDEX) {
            for (Module module : Cunny.MODULES.getModules(category)) {
                ModuleConfig moduleConfig = new ModuleConfig();
                moduleConfig.active = module.active();
                for (SettingGroup sg : module.getSettingGroups()) {
                    SettingGroupConfig groupConfig = new SettingGroupConfig();
                    for (Iterator<Setting<?, ?>> it = sg.get(); it.hasNext(); ) {
                        Setting<?, ?> setting = it.next();

                        if (setting.settingConfig == null) {
                            Cunny.LOG.info("{} in {} had a null setting config", setting.name, module.name);
                            continue;
                        }

                        setting.settingConfig.fromSetting(setting);

                        groupConfig.settings.putIfAbsent(setting.name, setting.settingConfig);
                    }
                    moduleConfig.groups.putIfAbsent(sg.name, groupConfig);
                }
                Config.INSTANCE.modules.putIfAbsent(module.name, moduleConfig);
            }
        }

        try {
            String toJson = gson.toJson(Config.INSTANCE);

            FileSystem.write(PathIndex.CONFIG, toJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void load() {
        if (Files.exists(PathIndex.CONFIG) && Files.isRegularFile(PathIndex.CONFIG)) {
            String file = Files.readString(PathIndex.CONFIG);
            try {
                Config.INSTANCE = gson.fromJson(file, Config.class);
            } catch (Exception e) {
                FileSystem.write(PathIndex.CUNNY.resolve(file.hashCode() + "_c.json"), file);
                Cunny.LOG.info("Config failed to load, reset.", e);
                Config.INSTANCE = this;
            }
        } else {
            Config.INSTANCE = this;
        }
    }

    public void populateModule(Module module) {
        if (modules.containsKey(module.name)) {
            ModuleConfig moduleConfig = INSTANCE.modules.get(module.name);
            module.active(moduleConfig.active);
            for (SettingGroup sg : module.getSettingGroups()) {
                for (Iterator<Setting<?, ?>> it = sg.get(); it.hasNext(); ) {
                    Setting<?, ?> setting = it.next();

                    if (!moduleConfig.groups.containsKey(setting.name)) continue;

                    moduleConfig.groups.get(sg.name).settings.get(setting.name).populateSetting(setting);

                    setting.onPopulated();
                }
            }
        }
    }
}
