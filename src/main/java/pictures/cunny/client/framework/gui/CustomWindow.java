package pictures.cunny.client.framework.gui;

import imgui.ImGui;
import imgui.extension.implot.ImPlot;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static pictures.cunny.client.Cunny.mc;

public abstract class CustomWindow {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    /**
     * Pointer to the native GLFW window.
     */
    protected long handle;

    /**
     * Method to initialize application.
     */
    protected void init() {
        initWindow();
        initImGui();
        imGuiGlfw.init(handle, true);
        imGuiGl3.init(null);
    }

    /**
     * Method to create and initialize GLFW window.
     */
    protected void initWindow() {
        GLFWErrorCallback.createPrint(System.err).set();


        handle = mc.getWindow().getWindow();

        GLFW.glfwSetWindowSizeCallback(handle, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(final long window, final int width, final int height) {
                runFrame();
            }
        });
    }

    /**
     * Method to initialize Dear ImGui context. Could be overridden to do custom Dear ImGui setup before application start.
     */
    protected void initImGui() {
        ImGui.createContext();
        ImPlot.createContext();
    }

    /**
     * Method called every frame, before calling {@link #process()} method.
     */
    protected void preProcess() {
    }

    /**
     * Method called every frame, after calling {@link #process()} method.
     */
    protected void postProcess() {
    }

    /**
     * Main application loop.
     */
    protected void run() {
    }

    /**
     * Method used to run the next frame.
     */
    public void runFrame() {
        startFrame();
        preProcess();
        process();
        postProcess();
        endFrame();
    }

    /**
     * Method to be overridden by user to provide main application logic.
     */
    public abstract void process();

    /**
     * Method called at the beginning of the main cycle.
     * It clears OpenGL buffer and starts an ImGui frame.
     */
    protected void startFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui and swaps GLFW buffers to show an updated frame.
     */
    protected void endFrame() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    /**
     * @return pointer to the native GLFW window
     */
    public final long getHandle() {
        return handle;
    }
}
