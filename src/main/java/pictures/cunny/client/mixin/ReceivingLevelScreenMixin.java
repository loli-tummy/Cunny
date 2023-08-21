package pictures.cunny.client.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ReceivingLevelScreen.class)
public abstract class ReceivingLevelScreenMixin extends Screen {

    protected ReceivingLevelScreenMixin(Component component) {
        super(component);
    }

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract void onClose();

    /**
     * @author ViaTi
     * @reason Automatically close this, FUCK YOU!
     */
    @Overwrite
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.onClose();
    }
}
