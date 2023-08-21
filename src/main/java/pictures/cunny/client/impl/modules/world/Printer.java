package pictures.cunny.client.impl.modules.world;

import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.SettingGroup;
import pictures.cunny.client.impl.events.mods.CloseSchematicEvent;
import pictures.cunny.client.utility.litematica.LiteHacks;

public class Printer extends Module {

    public Printer() {
        super(Categories.WORLD, "printer", "Prints blocks in the schematic world.");
    }

    @EventListener
    public void onClose(CloseSchematicEvent event) {
        LiteHacks.liteChunks.chunkMap.clear();
        LiteHacks.liteChunks.chunkItemMap.clear();
    }
}
