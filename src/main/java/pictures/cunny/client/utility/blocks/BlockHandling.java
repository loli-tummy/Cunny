package pictures.cunny.client.utility.blocks;

import pictures.cunny.client.utility.PacketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;

import static pictures.cunny.client.Cunny.mc;


public class BlockHandling {
    private static BlockPos lastBlockBroken = BlockPos.ZERO;

    public static void instantBreak(BlockPos blockPos) {
        assert mc.player != null;
        if (blockPos != lastBlockBroken) {
            swing();
            PacketUtils.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.DOWN));
        }
        swing();
        PacketUtils.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.DOWN));
    }

    public static void swing() {
        assert mc.player != null;
        if (!mc.player.swinging) PacketUtils.send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
    }
}
