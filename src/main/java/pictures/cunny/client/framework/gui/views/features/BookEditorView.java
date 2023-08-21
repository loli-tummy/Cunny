package pictures.cunny.client.framework.gui.views.features;

import com.google.gson.annotations.SerializedName;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.world.item.Items;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.config.gui.BookSettings;
import pictures.cunny.client.config.gui.PersistentGuiSettings;
import pictures.cunny.client.framework.gui.views.View;
import pictures.cunny.client.utility.InventoryUtils;
import pictures.cunny.client.utility.PacketUtils;
import pictures.cunny.client.utility.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static pictures.cunny.client.Cunny.mc;

public class BookEditorView extends View {
    public BookSettings config;

    public static final String NULL_CHARACTER = " ";

    private final List<String> PAGES_CACHE = new ArrayList<>();
    private String title = "";

    @Override
    public String name() {
        return "Book Editor";
    }

    @Override
    public void show() {
        PersistentGuiSettings settings = Config.get().booksGui;

        if (ImGui.begin("Book Editor", state())) {
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

            ImGui.labelText("Title", "Allows modifying book titles.");
            if (config.title.modifier != BookModifier.NONE)
                ImGui.bulletText("Title has the modifier of " + StringUtils.readable(config.title.modifier.name()));
            ImGui.inputText("Title Text", config.title.text, ImGuiInputTextFlags.CallbackResize);

            ImGui.sliderInt("Title Length", config.title.length.getData(),
                    1, 128);

            if (ImGui.button("Set Title")) {
                config.title.modifier = BookModifier.NONE;
                title = config.title.text.get();
            }

            ImGui.sameLine();

            if (ImGui.button("Nullify Title")) {
                config.title.modifier = BookModifier.NULLIFY;
                title = NULL_CHARACTER.repeat(Math.max(1, config.title.length.get()));
            }

            if (ImGui.button("Randomize Title")) {
                config.title.modifier = config.title.unicode.get() ? BookModifier.RANDOMIZED_UNICODE : BookModifier.RANDOMIZED;
            }

            ImGui.sameLine();

            ImGui.checkbox("Unicode Title", config.title.unicode);

            ImGui.separator();

            ImGui.labelText("Pages", "Allows modifying book pages.");

            if (config.pages.modifier != BookModifier.NONE)
                ImGui.bulletText("Pages has the modifier of " + StringUtils.readable(config.pages.modifier.name()));

            ImGui.sliderInt("Page Count", config.pages.count.getData(),
                    1, 100);
            ImGui.sliderInt("Page Length", config.pages.length.getData(),
                    1, 1024);

            ImGui.inputTextMultiline("Page Text", config.pages.text, ImGuiInputTextFlags.CallbackResize);

            if (ImGui.button("Fill Pages")) {
                config.pages.modifier = BookModifier.NONE;
                PAGES_CACHE.clear();
                for (int i = 0; i < config.pages.count.get(); i++) {
                    PAGES_CACHE.add(config.pages.text.get());
                }
            }

            ImGui.sameLine();

            if (ImGui.button("Nullify Pages")) {
                config.pages.modifier = BookModifier.NULLIFY;
                PAGES_CACHE.clear();
                for (int i = 0; i < config.pages.count.get(); i++) {
                    PAGES_CACHE.add(NULL_CHARACTER.repeat(Math.max(1, config.pages.length.get())));
                }
            }

            if (ImGui.button("Randomize Pages")) {
                config.pages.modifier = config.pages.unicode.get() ? BookModifier.RANDOMIZED_UNICODE : BookModifier.RANDOMIZED;
                PAGES_CACHE.clear();
                for (int i = 0; i < config.pages.count.get(); i++) {
                    PAGES_CACHE.add(StringUtils.randomText(config.pages.length.get(), config.pages.unicode.get()));
                }
            }

            ImGui.sameLine();

            ImGui.checkbox("Unicode Pages", config.pages.unicode);

            ImGui.separator();


            if (ImGui.button("Sign Book")) {
                if (config.title.modifier == BookModifier.RANDOMIZED || config.title.modifier == BookModifier.RANDOMIZED_UNICODE) {
                    title = StringUtils.randomText(config.title.length.get(), config.title.unicode.get());
                }

                if (config.pages.modifier == BookModifier.RANDOMIZED || config.pages.modifier == BookModifier.RANDOMIZED_UNICODE) {
                    PAGES_CACHE.clear();
                    for (int i = 0; i < config.pages.count.get(); i++) {
                        PAGES_CACHE.add(StringUtils.randomText(config.pages.length.get(), config.pages.unicode.get()));
                    }
                }

                if (mc.player == null) {
                    Cunny.error("Player is null, not in-game.");
                } else {
                    int slot = InventoryUtils.findAnySlot(Items.WRITABLE_BOOK);

                    if (slot == -1) return;

                    if (!InventoryUtils.isHotbarSlot(slot) && slot != 44) {
                        PacketUtils.send(new ServerboundPickItemPacket(slot));
                        assert mc.player != null;
                        slot = mc.player.getInventory().selected;
                    } else if (InventoryUtils.isHotbarSlot(slot)) {
                        slot = InventoryUtils.getHotbarOffset() - slot;
                    }

                    if (slot == -1) {
                        Cunny.error("Book was not found in hotbar.");
                        return;
                    } else if (InventoryUtils.isHotbarSlot(slot)) {
                        InventoryUtils.swapSlot(slot, false);
                    }

                    PacketUtils.send(new ServerboundEditBookPacket(InventoryUtils.isHotbarSlot(slot) ? InventoryUtils.getHotbarId(slot) : mc.player.getInventory().selected, PAGES_CACHE, Optional.of(title)));
                }
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
        this.settings = config.booksGui;
        this.config = config.bookSettings;
    }

    public enum BookModifier {
        @SerializedName("1")
        RANDOMIZED,
        @SerializedName("2")
        RANDOMIZED_UNICODE,
        @SerializedName("3")
        NULLIFY,
        @SerializedName("0")
        NONE
    }
}
