package pictures.cunny.client.framework.gui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.type.ImBoolean;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.framework.gui.utility.CKeys;
import pictures.cunny.client.framework.gui.views.View;
import pictures.cunny.client.framework.gui.views.ViewHandler;

import static pictures.cunny.client.Cunny.mc;

public class Gui extends CustomWindow {
    public static ImBoolean IS_LOADED = new ImBoolean(false);
    public static ImBoolean IS_STYLING = new ImBoolean(false);
    public static ImGuiIO io;

    public void launch() {
        initialize();
        preRun();
        run();
    }

    private void initialize() {
        init();

        io = ImGui.getIO();

        io.setIniFilename("");

        io.setKeyMap(CKeys.keyMap);

        ImGui.styleColorsDark();
        ImGui.getStyle().setTabRounding(0.0f);
        ImGui.getStyle().setGrabRounding(0.0f);
        ImGui.getStyle().setTabBorderSize(0.3f);
        ImGui.getStyle().setChildBorderSize(0.3f);
        ImGui.getStyle().setWindowRounding(0.0f);
        ImGui.getStyle().setButtonTextAlign(0.1f, 0.5f);
        ImGui.getStyle().setItemSpacing(4, 4);
    }

    public void process() {

        if (ImGui.isKeyPressed(CKeys.BACKSLASH)) {
            IS_LOADED.set(!IS_LOADED.get());
            if (Config.get() != null) {
                Config.get().save();
            }
        }

        if (IS_LOADED.get()) {
            if (mc.mouseHandler.isMouseGrabbed())
                mc.mouseHandler.releaseMouse();

            if (ImGui.beginMainMenuBar()) {
                if (ImGui.beginMenu("Views")) {

                    for (View view : ViewHandler.getViews()) {
                        ImGui.menuItem(view.name(), "", ViewHandler.state(view.getClass()));
                    }

                    ImGui.menuItem("Styling", "", IS_STYLING);
                    ImGui.endMenu();
                }

                if (ImGui.button("Close")) {
                    IS_LOADED.set(false);
                }

                ImGui.endMainMenuBar();
            }

            if (Config.get() == null) return;

            ViewHandler.showAll();

            if (IS_STYLING.get()) {
                ImGui.showStyleEditor(ImGui.getStyle());
            }
        } else if (!mc.mouseHandler.isMouseGrabbed() && mc.screen == null)
            mc.mouseHandler.grabMouse();
    }

    protected void preRun() {
        Cunny.LOG.info("Starting cunny GUI");
    }
}
