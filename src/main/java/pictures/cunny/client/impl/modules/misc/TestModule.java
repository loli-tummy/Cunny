package pictures.cunny.client.impl.modules.misc;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.level.block.Blocks;
import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.*;
import pictures.cunny.client.framework.modules.settings.wrappers.ImBlockPos;
import pictures.cunny.client.impl.events.game.PacketEvent;
import pictures.cunny.client.impl.events.game.TickEvent;
import pictures.cunny.client.utility.PacketUtils;

public class TestModule extends Module {
    public SettingGroup testGroup = add(new SettingGroup("SEX !",
            "UOOOHHHH EROTIC CHILD TUMMY !!! EROTIC !!!"));
    public BoolSetting sexSetting = new BoolSetting.Builder()
            .name("sex")
            .description("HAS SEX !")
            .defaultTo(false)
            .addTo(testGroup);
    public IntSetting numberino = new IntSetting.Builder()
            .name("number")
            .description("A setting probably")
            .range(1, 20)
            .defaultTo(6)
            .addTo(testGroup);
    public BlockListSetting blocks = new BlockListSetting.Builder()
            .name("blocks")
            .description("OOOOOOOOOOOOO")
            .defaultTo(ObjectList.of(Blocks.BEDROCK))
            .filter(block -> block.getDescriptionId().contains("be"))
            .addTo(testGroup);

    public BlockPosSetting blockPos = new BlockPosSetting.Builder()
            .name("block-pos")
            .description("OOOOOOOOOOOOO")
            .defaultTo(new ImBlockPos())
            .addTo(testGroup);

    public TestModule() {
        super(Categories.MISC, "testing", "A module designed for testing.");
    }

    @EventListener
    public void onPacketSend(PacketEvent.Received event) {
        if (event.packet instanceof ClientboundContainerSetSlotPacket packet) {
            if (packet.getSlot() < 0) event.cancel();
        }
    }

    @EventListener
    public void onPreTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        for (int i = 0; i < numberino.value().get(); i++) {
            PacketUtils.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
            PacketUtils.send(new ServerboundContainerClosePacket(-1));
            //PacketUtils.send(new ServerboundContainerClickPacket(mc.player.containerMenu.containerId, mc.player.containerMenu.getStateId(), -999, 0, ClickType.QUICK_CRAFT, ItemStack.EMPTY, new Int2ObjectOpenHashMap<>()));
        }
    }
}

