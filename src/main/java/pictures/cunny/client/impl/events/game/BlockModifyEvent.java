package pictures.cunny.client.impl.events.game;

import pictures.cunny.client.framework.events.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockModifyEvent extends Event {
    public BlockState state;
    public BlockPos pos;

    public static class Update extends BlockModifyEvent {
        public static final Update INSTANCE = new Update();

        public static Update get(BlockState state, BlockPos pos) {
            INSTANCE.state = state;
            INSTANCE.pos = pos;
            return INSTANCE;
        }
    }
}
