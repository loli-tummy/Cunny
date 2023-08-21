package pictures.cunny.client.mixin;

import pictures.cunny.client.Cunny;
import pictures.cunny.client.impl.events.game.ScreenEvent;
import pictures.cunny.client.impl.events.game.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        Cunny.postInit();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickHead(CallbackInfo ci) {
        Cunny.EVENT_HANDLER.call(TickEvent.Pre.get());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tickTail(CallbackInfo ci) {
        Cunny.EVENT_HANDLER.call(TickEvent.Post.get());
    }

    @Inject(method = "setScreen", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void setScreen(@Nullable Screen screen, CallbackInfo ci) {
        ScreenEvent.Open event = ScreenEvent.Open.get(screen);
        Cunny.EVENT_HANDLER.call(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
