package pictures.cunny.client.impl.modules.world;

import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.IntSetting;
import pictures.cunny.client.impl.events.game.TickEvent;
import pictures.cunny.client.utility.PacketUtils;
import pictures.cunny.client.utility.StringUtils;
import pictures.cunny.client.utility.WorldUtils;
import pictures.cunny.client.utility.blocks.BlockUtils;

public class SignFucker extends Module {
    public final IntSetting iterations = new IntSetting.Builder()
            .name("iterations")
            .description("Iterations to run per tick.")
            .defaultTo(5)
            .range(1, 20)
            .addTo(coreGroup);
    public final IntSetting textLength = new IntSetting.Builder()
            .name("length")
            .description("The text length.")
            .defaultTo(16)
            .range(1, 256)
            .addTo(coreGroup);

    public SignFucker() {
        super(Categories.WORLD, "sign-fucker", "Fucks with the sign you're looking at.");
    }

    @EventListener
    public void onTick(TickEvent.Pre event) {
        if (WorldUtils.isLookingAtBlock((block) -> BlockUtils.getKey(block).endsWith("sign"))) {
            BlockHitResult hitResult = WorldUtils.getLookingAtBlock();
            for (int i = 0; i < iterations.value().get(); i++) {
                PacketUtils.send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, hitResult, 0));
                PacketUtils.send(new ServerboundSignUpdatePacket(hitResult.getBlockPos(),
                        Cunny.RANDOM.nextBoolean(),
                        StringUtils.randomText(textLength.value().get()),
                        StringUtils.randomText(textLength.value().get()),
                        StringUtils.randomText(textLength.value().get()),
                        StringUtils.randomText(textLength.value().get())));
            }
        }
    }
}
