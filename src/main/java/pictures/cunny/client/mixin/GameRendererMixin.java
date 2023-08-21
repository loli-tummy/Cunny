package pictures.cunny.client.mixin;

import pictures.cunny.client.Cunny;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, priority = 1)
public class GameRendererMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (Cunny.GUI != null) Cunny.GUI.runFrame();
    }
}
