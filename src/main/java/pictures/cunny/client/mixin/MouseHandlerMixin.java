package pictures.cunny.client.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.framework.gui.Gui;
import pictures.cunny.client.impl.modules.misc.DetachMouse;

@Mixin(value = MouseHandler.class, priority = 1)
public class MouseHandlerMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (Gui.IS_LOADED.get() && window == Cunny.GUI.getHandle()) {
            ci.cancel();
        }
    }

    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (Gui.IS_LOADED.get() && window == Cunny.GUI.getHandle()) {
            ci.cancel();
        }
    }

    @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
    public void onCursorPos(long window, double x, double y, CallbackInfo ci) {
        if (Gui.IS_LOADED.get() && window == Cunny.GUI.getHandle()) {
            ci.cancel();
        }
    }

    @Inject(method = "grabMouse", at = @At("HEAD"), cancellable = true)
    public void grabMouse(CallbackInfo ci) {
        if (Gui.IS_LOADED.get() || Cunny.MODULES.isActive(DetachMouse.class)) {
            ci.cancel();
        }
    }
}
