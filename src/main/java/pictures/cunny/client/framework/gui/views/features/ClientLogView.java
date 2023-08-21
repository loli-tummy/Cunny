package pictures.cunny.client.framework.gui.views.features;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import it.unimi.dsi.fastutil.objects.ObjectBigArrayBigList;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.config.gui.PersistentGuiSettings;
import pictures.cunny.client.framework.gui.utility.ColoredText;
import pictures.cunny.client.framework.gui.utility.GuiUtils;
import pictures.cunny.client.framework.gui.views.View;

public class ClientLogView extends View {
    public static ImString inputText = new ImString();
    public static ObjectBigArrayBigList<ColoredText> LOGS = new ObjectBigArrayBigList<>(5000);

    @Override
    public String name() {
        return "Logs";
    }

    @Override
    public void show() {
        PersistentGuiSettings settings = Config.get().logsGui;

        if (ImGui.begin("Console", state())) {
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

            ImGui.inputText("Command", inputText, ImGuiInputTextFlags.CallbackResize);
            if (ImGui.isItemDeactivatedAfterEdit()
                    && !inputText.get().isBlank()) {
                Cunny.warn("Commands are currently disabled.");
            }

            if (ImGui.beginChild("messages",
                    ImGui.getWindowWidth() - 6, ImGui.getWindowHeight() / 1.24f,
                    true, ImGuiWindowFlags.NoResize)) {
                for (long i = LOGS.size64() - 1; i > 0; i--) {
                    GuiUtils.text(LOGS.get(i));
                }
            }

            ImGui.endChild();
        }

        settings.x = ImGui.getWindowPosX();
        settings.y = ImGui.getWindowPosY();
        settings.width = ImGui.getWindowWidth();
        settings.height = ImGui.getWindowHeight();
        ImGui.end();
    }

    @Override
    public void populateSettings(Config config) {
        settings = config.logsGui;
    }
}
