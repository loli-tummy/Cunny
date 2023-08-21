package pictures.cunny.client.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {

    protected LevelLoadingScreenMixin(Component component) {
        super(component);
    }

    @Shadow
    public abstract void removed();

    /**
     * @author ViaTi
     * @reason Automatically close this, FUCK YOU!
     */
    @Overwrite
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.onClose();
    }
}
