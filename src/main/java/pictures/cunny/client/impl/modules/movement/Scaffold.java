package pictures.cunny.client.impl.modules.movement;

import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.SettingGroup;
import pictures.cunny.client.impl.events.game.TickEvent;
import pictures.cunny.client.utility.InventoryUtils;
import pictures.cunny.client.utility.blocks.BlockUtils;
import net.minecraft.world.item.Items;

public class Scaffold extends Module {
    public SettingGroup coreGroup = add(new SettingGroup("Core",
            "Core module settings."));

    public Scaffold() {
        super(Categories.MOVEMENT, "sneak", "Automatically sneaks.");
    }

    @EventListener
    public void onTick(TickEvent.Pre event) {
        if (mc.player == null)
            return;

        BlockUtils.placeBlock(InventoryUtils.findAnySlot(Items.OBSIDIAN), mc.player.getOnPos());
    }
}
