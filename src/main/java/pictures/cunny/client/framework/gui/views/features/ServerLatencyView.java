package pictures.cunny.client.framework.gui.views.features;

import imgui.ImGui;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.config.gui.PersistentGuiSettings;
import pictures.cunny.client.utility.LatencyUtils;

public class ServerLatencyView {
    public static ImBoolean IS_LOADED = new ImBoolean(false);

    public static void show() {
        if (!IS_LOADED.get()) return;

        PersistentGuiSettings settings = Config.get().latencyGui;


        if (ImGui.begin("Server Latency")) {
            if (ImPlot.beginPlot("Latencies")) {
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

                ImPlot.plotVLines("Latency", LatencyUtils.pings.toArray(new Integer[]{}));
                ImPlot.endPlot();
            }
        }

        settings.x = ImGui.getWindowPosX();
        settings.y = ImGui.getWindowPosY();
        settings.width = ImGui.getWindowWidth();
        settings.height = ImGui.getWindowHeight();
        ImGui.end();
    }
}
