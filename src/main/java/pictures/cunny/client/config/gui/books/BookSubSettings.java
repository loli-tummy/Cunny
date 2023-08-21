package pictures.cunny.client.config.gui.books;

import pictures.cunny.client.config.gui.RandomizationSettings;
import pictures.cunny.client.framework.gui.views.features.BookEditorView;

public class BookSubSettings extends RandomizationSettings {
    public BookEditorView.BookModifier modifier = BookEditorView.BookModifier.NONE;

    @Override
    public String getDefaultText() {
        return "Cunny Client!";
    }

    @Override
    public int getDefaultLength() {
        return 16;
    }
}
