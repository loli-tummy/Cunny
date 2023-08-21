package pictures.cunny.client.mixin;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ResourceLocation.class)
public class ResourceLocationMixin {
    /**
     * @author ViaTi
     * @reason THE VOICES WON'T STOP ! THEY WILL NOT SHUT UP ! HA HA HA HA HA ! ! !
     */
    @Overwrite
    private static boolean isValidNamespace(String string) {
        return true;
    }
}
