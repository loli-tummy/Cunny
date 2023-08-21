package pictures.cunny.client.impl.modules.misc;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.settings.BoolSetting;
import pictures.cunny.client.framework.modules.settings.IntSetting;
import pictures.cunny.client.framework.modules.settings.StringSetting;
import pictures.cunny.client.impl.events.game.PacketEvent;
import pictures.cunny.client.utility.StringUtils;

public class BrandSpoof extends Module {
    public final BoolSetting randomBuffer = new BoolSetting.Builder()
            .name("random")
            .description("Randomize the buffer text size.")
            .defaultTo(true)
            .addTo(coreGroup);
    public final StringSetting stringText = new StringSetting.Builder()
            .name("text")
            .description("The brand text size.")
            .visible((s) -> randomBuffer.value().get())
            .defaultTo("Cunny")
            .addTo(coreGroup);
    public final IntSetting bufferSize = new IntSetting.Builder()
            .name("size")
            .description("The buffer text size.")
            .defaultTo(15)
            .visible(s -> randomBuffer.isVisible())
            .range(1, 256)
            .addTo(coreGroup);

    public BrandSpoof() {
        super(Categories.MISC, "brand-spoof", "Spoofs your client brand.");
    }

    @EventListener(inGame = false)
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof ServerboundCustomPayloadPacket packet) {
            if (packet.getIdentifier() == ServerboundCustomPayloadPacket.BRAND) {
                event.packet = new ServerboundCustomPayloadPacket(
                        ServerboundCustomPayloadPacket.BRAND,
                        new FriendlyByteBuf(Unpooled.buffer())
                                .writeUtf(randomBuffer.value().get()
                                                ? StringUtils.randomText(bufferSize.value().get(), true)
                                                : stringText.value().get(),
                                        randomBuffer.value().get()
                                                ? bufferSize.value().get() * 4
                                                : stringText.value().get().length() * 2));
            }
        }
    }
}
