package pictures.cunny.client.impl.modules.world;

import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.impl.events.game.TickEvent;
import pictures.cunny.client.utility.InventoryUtils;
import pictures.cunny.client.utility.PacketUtils;
import pictures.cunny.client.utility.WorldUtils;
import pictures.cunny.client.utility.blocks.BlockUtils;

public class AutoPlace extends Module {
    public AutoPlace() {
        super(Categories.WORLD, "auto-place", "Places blocks in schematics when looked at.");
    }

    @EventListener
    public void onTick(TickEvent.Pre event) {
        WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
        if (worldSchematic == null || mc.hitResult == null) return;

        if (WorldUtils.isLookingAt(HitResult.Type.BLOCK)) {
            BlockHitResult hitResult = WorldUtils.getLookingAtBlock();
            BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());

            if (!BlockUtils.isReplaceable(pos)) return;

            BlockState blockState = worldSchematic.getBlockState(pos);

            if (blockState.getBlock().asItem() == Items.AIR) return;

            Item item = worldSchematic.getBlockState(pos)
                    .getBlock()
                    .asItem();

            int slot = InventoryUtils.findAnySlot(item);

            if (slot == -1) return;

            boolean offhand = false;

            if (!InventoryUtils.isHotbarSlot(slot) && slot != 44) {
                PacketUtils.send(new ServerboundPickItemPacket(slot));
                assert mc.player != null;
                slot = mc.player.getInventory().selected;
            } else if (InventoryUtils.isHotbarSlot(slot)) {
                slot = InventoryUtils.getHotbarOffset() - slot;
                ;
            }

            Cunny.log("Slot of {}", slot);

            if (slot == 44)
                offhand = true;

            PacketUtils.send(new ServerboundUseItemOnPacket(offhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, hitResult, 0));
        }
    }
}
