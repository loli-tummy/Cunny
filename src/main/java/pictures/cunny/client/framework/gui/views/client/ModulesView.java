package pictures.cunny.client.framework.gui.views.client;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiMouseButton;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.config.gui.PersistentGuiSettings;
import pictures.cunny.client.framework.gui.views.View;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Category;
import pictures.cunny.client.framework.modules.Module;

import java.util.Iterator;

public class ModulesView extends View {
    @Override
    public String name() {
        return "Modules";
    }

    public void show() {
        PersistentGuiSettings settings = Config.get().modulesGui;

        if (ImGui.begin("Modules", state())) {
            if (settings.hasLoaded) {
                ImGui.setWindowSize(settings.width, settings.height, ImGuiCond.Once);
                ImGui.setWindowPos(settings.x, settings.y, ImGuiCond.Once);
                ImGui.setWindowSize(settings.width, settings.height, ImGuiCond.Appearing);
                ImGui.setWindowPos(settings.x, settings.y, ImGuiCond.Appearing);
            } else {
                ImGui.setWindowSize(400, 600, ImGuiCond.FirstUseEver);
                ImGui.setWindowPos(0, 0, ImGuiCond.FirstUseEver);
                settings.hasLoaded = true;
            }

            if (ImGui.beginTabBar("categories")) {
                for (Category category : Categories.INDEX) {
                    if (Cunny.MODULES.getModules(category).isEmpty()) continue;

                    boolean tabItem = ImGui.beginTabItem(category.getLabel());

                    if (ImGui.isItemHovered() && category.description != null) {
                        ImGui.beginTooltip();
                        ImGui.text(category.description);
                        ImGui.endTooltip();
                    }

                    if (tabItem) {
                        ImGui.beginChild(category.name);

                        for (Iterator<Module> it = Cunny.MODULES.getModules(category).listIterator(); it.hasNext(); ) {
                            Module module = it.next();

                            if (ImGui.checkbox(module.getLabel(), module.active)) {
                                module.toggled();
                            }

                            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                                ModuleConfigView.setModule(module);
                                state().set(true);
                            }

                            if (ImGui.isItemHovered() && module.description() != null) {
                                ImGui.beginTooltip();
                                ImGui.text(module.description());
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
        settings = config.modulesGui;
    }
}
