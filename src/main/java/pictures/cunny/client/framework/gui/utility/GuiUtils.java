package pictures.cunny.client.framework.gui.utility;

import imgui.ImGui;

public class GuiUtils {
    public static void text(ColoredText coloredText) {
        ImGui.textColored(coloredText.color().getRed(),
                coloredText.color().getGreen(),
                coloredText.color().getBlue(),
                coloredText.color().getAlpha(),
                coloredText.text());
    }

    public static void text(ColoredText... coloredTexts) {
        if (coloredTexts == null)
            return;

        boolean sameLine = false;

        for (ColoredText coloredText : coloredTexts) {
            if (sameLine)
                ImGui.sameLine();
            ImGui.textColored(coloredText.color().getRGB(), coloredText.text());
            sameLine = true;
        }
    }
}
