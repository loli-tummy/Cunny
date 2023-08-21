package pictures.cunny.client.config.gui.books;

import imgui.type.ImBoolean;
import imgui.type.ImInt;
import pictures.cunny.client.config.gui.RandomizationSettings;

public class PageSettings extends BookSubSettings {
    public final ImInt count = new ImInt(50);

    @Override
    public String getDefaultText() {
        return "Uooohhh !!!! SO CUTE !!! SO FUNNY !!!";
    }

    @Override
    public int getDefaultLength() {
        return 1024;
    }
}
