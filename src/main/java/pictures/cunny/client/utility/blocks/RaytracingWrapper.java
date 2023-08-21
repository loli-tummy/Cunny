package pictures.cunny.client.utility.blocks;

import pictures.cunny.client.utility.FakePlayerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

public class RaytracingWrapper {
    public Vec3 start = Vec3.ZERO;
    public Vec3 end = Vec3.ZERO;
    public ClipContext.ShapeGetter shapeType;
    public ClipContext.Fluid fluid = ClipContext.Fluid.NONE;
    public ClipContext.Block entityPosition = ClipContext.Block.COLLIDER;

    public ClipContext context;

    public ClipContext getContext() {
        if (context == null) {
            context = new ClipContext(start, end, entityPosition, fluid, new FakePlayerEntity(EntityType.GIANT));
        }
        return context;
    }
}
