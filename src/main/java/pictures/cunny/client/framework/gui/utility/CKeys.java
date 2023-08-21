package pictures.cunny.client.framework.gui.utility;

import imgui.flag.ImGuiKey;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public class CKeys {
    public static int[] keyMap = new int[512];
    public static int additions = ImGuiKey.COUNT;
    public static final int
            TAB = keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB,
            LEFT = keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT,
            RIGHT = keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT,
            UP = keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP,
            DOWN = keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN,
            PAGE_UP = keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP,
            PAGE_DOWN = keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN,
            HOME = keyMap[ImGuiKey.Home] = GLFW_KEY_HOME,
            END = keyMap[ImGuiKey.End] = GLFW_KEY_END,
            INSERT = keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT,
            DELETE = keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE,
            BACKSPACE = keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE,
            SPACE = keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE,
            ENTER = keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER,
            ESCAPE = keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE,
            KP_ENTER = keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER,
            NUM_0 = keyMap[++additions] = GLFW_KEY_0,
            NUM_1 = keyMap[++additions] = GLFW_KEY_1,
            NUM_2 = keyMap[++additions] = GLFW_KEY_2,
            NUM_3 = keyMap[++additions] = GLFW_KEY_3,
            NUM_4 = keyMap[++additions] = GLFW_KEY_4,
            NUM_5 = keyMap[++additions] = GLFW_KEY_5,
            NUM_6 = keyMap[++additions] = GLFW_KEY_6,
            NUM_7 = keyMap[++additions] = GLFW_KEY_7,
            NUM_8 = keyMap[++additions] = GLFW_KEY_8,
            NUM_9 = keyMap[++additions] = GLFW_KEY_9,
            SEMICOLON = keyMap[++additions] = GLFW_KEY_SEMICOLON,
            EQUAL = keyMap[++additions] = GLFW_KEY_EQUAL,
            A = keyMap[ImGuiKey.A] = GLFW_KEY_A,
            B = keyMap[++additions] = GLFW_KEY_B,
            C = keyMap[ImGuiKey.C] = GLFW_KEY_C,
            D = keyMap[++additions] = GLFW_KEY_D,
            E = keyMap[++additions] = GLFW_KEY_E,
            F = keyMap[++additions] = GLFW_KEY_F,
            G = keyMap[++additions] = GLFW_KEY_G,
            H = keyMap[++additions] = GLFW_KEY_H,
            I = keyMap[++additions] = GLFW_KEY_I,
            J = keyMap[++additions] = GLFW_KEY_J,
            K = keyMap[++additions] = GLFW_KEY_K,
            L = keyMap[++additions] = GLFW_KEY_L,
            M = keyMap[++additions] = GLFW_KEY_M,
            N = keyMap[++additions] = GLFW_KEY_N,
            O = keyMap[++additions] = GLFW_KEY_O,
            P = keyMap[++additions] = GLFW_KEY_P,
            Q = keyMap[++additions] = GLFW_KEY_Q,
            R = keyMap[++additions] = GLFW_KEY_R,
            S = keyMap[++additions] = GLFW_KEY_S,
            T = keyMap[++additions] = GLFW_KEY_T,
            U = keyMap[++additions] = GLFW_KEY_U,
            V = keyMap[ImGuiKey.V] = GLFW_KEY_V,
            W = keyMap[++additions] = GLFW_KEY_W,
            X = keyMap[ImGuiKey.X] = GLFW_KEY_X,
            Y = keyMap[ImGuiKey.Y] = GLFW_KEY_Y,
            Z = keyMap[ImGuiKey.Z] = GLFW_KEY_Z,
            LEFT_BRACKET = keyMap[++additions] = GLFW_KEY_LEFT_BRACKET,
            BACKSLASH = keyMap[++additions] = GLFW_KEY_BACKSLASH,
            RIGHT_BRACKET = keyMap[++additions] = GLFW_KEY_RIGHT_BRACKET,
            GRAVE_ACCENT = keyMap[++additions] = GLFW_KEY_GRAVE_ACCENT,
            WORLD_1 = keyMap[++additions] = GLFW_KEY_WORLD_1,
            WORLD_2 = keyMap[++additions] = GLFW_KEY_WORLD_2,
            COUNT = keyMap[ImGuiKey.COUNT] = additions;
    public static Map<Integer, Integer> remappedKeys = new HashMap<>();

    public static int getMappedKey(int key) {
        if (remappedKeys.containsKey(key))
            return remappedKeys.get(key);
        for (int i = 0; i < COUNT - 1; i++) {
            if (keyMap[i] == key) {
                remappedKeys.put(key, i);
                break;
            }
        }

        return remappedKeys.getOrDefault(key, -1);
    }

    public static int getUnMappedKey(int mapped) {
        return keyMap[mapped];
    }
}
