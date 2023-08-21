package pictures.cunny.client.framework.gui.views.client;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.config.gui.PersistentGuiSettings;
import pictures.cunny.client.framework.gui.views.View;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.Setting;
import pictures.cunny.client.framework.modules.settings.SettingGroup;
import pictures.cunny.client.utility.StringUtils;

import java.util.Iterator;

public class ModuleConfigView extends View {
    private static Module module;
    private static boolean awaitingWindowAdjustment;

    @Override
    public String name() {
        return "Module Config";
    }

    public static void setModule(Module module) {
        //SettingsComponents.clear();
        if (ModuleConfigView.module != null) ModuleConfigView.awaitingWindowAdjustment = true;
        ModuleConfigView.module = module;
    }

    @Override
    public void show() {
        PersistentGuiSettings settings = Config.get().moduleConfigGui;

        if (module == null)
            return;

        if (ImGui.begin(StringUtils.readable(module.name), state())) {
            if (settings.hasLoaded) {
                ImGui.setWindowSize(settings.width, settings.height, ImGuiCond.Once);
                ImGui.setWindowPos(settings.x, settings.y, ImGuiCond.Once);
                ImGui.setWindowSize(settings.width, settings.height, ImGuiCond.Appearing);
                ImGui.setWindowPos(settings.x, settings.y, ImGuiCond.Appearing);
            } else {
                ImGui.setWindowSize(400, 600, ImGuiCond.FirstUseEver);
                ImGui.setWindowPos(ImGui.getMainViewport().getPosX() + 100, ImGui.getMainViewport().getPosY() + 100, ImGuiCond.FirstUseEver);
                settings.hasLoaded = true;
            }

            if (awaitingWindowAdjustment) {
                ImGui.setWindowSize(settings.width, settings.height);
                ImGui.setWindowPos(settings.x, settings.y);
                awaitingWindowAdjustment = false;
            }

            if (ImGui.beginTabBar("section")) {
                for (SettingGroup group : module.getSettingGroups()) {
                    boolean tabItem = ImGui.beginTabItem(group.name);
                    if (ImGui.isItemHovered() && !group.description().isBlank()) {
                        ImGui.beginTooltip();
                        ImGui.text(group.description());
                        ImGui.endTooltip();
                    }

                    if (!group.get().hasNext()) continue;

                    if (tabItem) {
                        ImGui.beginChild(group.name);

                        for (Iterator<Setting<?, ?>> it = group.get(); it.hasNext(); ) {
                            Setting<?, ?> setting = it.next();

                            if (!setting.isVisible()) continue;

                            setting.render();

                            if (ImGui.isItemHovered() && !setting.description.isBlank()) {
                                ImGui.beginTooltip();
                                ImGui.text(setting.description);
                                ImGui.endTooltip();
                            }

                            if (it.hasNext()) {
                                ImGui.separator();
                            }
                        }

                        ImGui.endChild();
                        ImGui.endTabItem();
                    }
                }
                ImGui.endTabBar();
            }
        }

        settings.x = ImGui.getWindowPosX();
        settings.y = ImGui.getWindowPosY();
        settings.width = ImGui.getWindowWidth();
        settings.height = ImGui.getWindowHeight();
        ImGui.end();
    }

    @Override
    public void populateSettings(Config config) {
        settings = config.moduleConfigGui;
    }
}
