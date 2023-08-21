package pictures.cunny.client.utility;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import pictures.cunny.client.utility.blocks.BlockUtils;

import java.util.function.Predicate;

import static pictures.cunny.client.Cunny.mc;

public class WorldUtils {
    public static boolean isLookingAt(HitResult.Type type) {
        if (mc.player == null
                || mc.hitResult == null)
            return false;


        return mc.hitResult.getType() == type;
    }

    public static boolean isLookingAtBlock(Block... block) {
        if (mc.player == null
                || mc.hitResult == null
                || !isLookingAt(HitResult.Type.BLOCK))
            return false;


        return BlockUtils.isBlock(((BlockHitResult) mc.hitResult).getBlockPos(), block);
    }

    public static boolean isLookingAtBlock(Predicate<Block> predicate) {
        if (mc.player == null
                || mc.hitResult == null
                || !isLookingAt(HitResult.Type.BLOCK))
            return false;


        return BlockUtils.testBlock(((BlockHitResult) mc.hitResult).getBlockPos(), predicate);
    }

    public static BlockHitResult getLookingAtBlock() {
        return (BlockHitResult) mc.hitResult;
    }
}
