package pictures.cunny.client.mixin;

import pictures.cunny.client.Cunny;
import pictures.cunny.client.impl.events.game.BookSignEvent;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin {
    @Shadow
    @Final
    private List<String> pages;

    @Shadow
    private String title;

    @Shadow
    private boolean isModified;

    @Inject(method = "saveChanges", at = @At("HEAD"), cancellable = true)
    public void onSaveChanges(boolean bl, CallbackInfo ci) {
        BookSignEvent event = BookSignEvent.get(pages, title, isModified);
        Cunny.EVENT_HANDLER.call(event);
        if (event.stop) {
            ci.cancel();
        } else {
            pages.clear();
            pages.addAll(event.pages);
            title = event.title;
            isModified = true;
        }
    }
}
