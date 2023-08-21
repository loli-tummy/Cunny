package pictures.cunny.client.mixin;

import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FriendlyByteBuf.class)
public class FriendlyByteBufMixin {
    /**
     * @author ViaTi
     * @reason I HATE YOU SO MUCH WHY DO YOU DO THESE THINGS TO ME
     */
    @Overwrite
    private static int getMaxEncodedUtfLength(int i) {
        return 100000000;
    }
}
