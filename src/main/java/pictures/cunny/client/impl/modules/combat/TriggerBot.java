package pictures.cunny.client.impl.modules.combat;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.impl.events.game.TickEvent;
import pictures.cunny.client.utility.PacketUtils;
import pictures.cunny.client.utility.WorldUtils;

public class TriggerBot extends Module {

    public TriggerBot() {
        super(Categories.COMBAT, "trigger-bot", "Automatically hits an enemy when it's in the cross-hair.");
    }

    @EventListener
    public void onTick(TickEvent.Pre event) {
        if (WorldUtils.isLookingAt(HitResult.Type.ENTITY)) {
            assert mc.hitResult != null;
            Entity entity = ((EntityHitResult) mc.hitResult).getEntity();
            assert mc.player != null;
            if (mc.player.getAttackStrengthScale(0.5f) >= 0.9 && entity.invulnerableTime <= 10)
                PacketUtils.send(ServerboundInteractPacket.createAttackPacket(((EntityHitResult) mc.hitResult).getEntity(), mc.player.isShiftKeyDown()));
        }
    }
}
