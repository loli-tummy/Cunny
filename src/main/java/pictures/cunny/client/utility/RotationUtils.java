package pictures.cunny.client.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import static pictures.cunny.client.Cunny.mc;

public class RotationUtils {
    public static Float getPitch(BlockPos pos) {
        return getPitch(pos.getCenter());
    }

    public static Float getYaw(BlockPos pos) {
        return getYaw(pos.getCenter());
    }

    public static Float getPitch(Vec3 pos) {
        assert mc.player != null : "Player was null";
        double d = pos.x() - mc.player.getX();
        double e = pos.y() - mc.player.getEyeY();
        double f = pos.z() - mc.player.getZ();
        double g = Math.sqrt(d * d + f * f);
        return Math.abs(e) > (double) 1.0E-5f || Math.abs(g) > (double) 1.0E-5f ? (float) (-(Mth.atan2(e, g) * 57.2957763671875)) : 0;
    }

    public static Float getYaw(Vec3 pos) {
        assert mc.player != null : "Player was null";
        double d = pos.x() - mc.player.getX();
        double e = pos.z() - mc.player.getZ();
        return Math.abs(e) > (double) 1.0E-5f || Math.abs(d) > (double) 1.0E-5f ? (float) (Mth.atan2(e, d) * 57.2957763671875) - 90.0f : 0;
    }

    public static void rotate(float pitch, float yaw) {
        assert mc.player != null : "Player was null";
        mc.player.setYRot(Mth.wrapDegrees(pitch));
        mc.player.setXRot(Mth.wrapDegrees(yaw));
    }
}
