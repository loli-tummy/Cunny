package pictures.cunny.client.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = SharedConstants.class, priority = 1)
public class SharedConstantsMixin {
    /**
     * @author ViaTi
     * @reason I hate you I hate you I hate you I hate you I hate you I hate you
     */
    @Overwrite
    public static boolean isAllowedChatCharacter(char c) {
        return true;
    }

    /**
     * @author ViaTi
     * @reason YOU AREN'T REAL LEAVE ME ALONE LEAVE ME ALONE LEAVE ME ALONE GET OUT OF MY COMPUTER
     */
    @Overwrite
    public static String filterText(String s, boolean a) {
        return s;
    }
}
