package pictures.cunny.client.mixin;

import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.framework.gui.Gui;
import pictures.cunny.client.framework.gui.utility.CKeys;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Category;
import pictures.cunny.client.framework.modules.Module;

@Mixin(value = KeyboardHandler.class, priority = 1)
public abstract class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (Gui.IS_LOADED.get() && window == Cunny.GUI.getHandle()) {
            ci.cancel();
        }

        int remapped = CKeys.getMappedKey(key);

        if (remapped != -1) {
            for (Category category : Categories.INDEX) {
                for (Module module : Cunny.MODULES.getCategory(category)) {
                    module.keyPressed(key);
                }
            }
        }
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    public void onChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
        if (Gui.IS_LOADED.get() && window == Cunny.GUI.getHandle()) {
            ci.cancel();
        }
    }
}
