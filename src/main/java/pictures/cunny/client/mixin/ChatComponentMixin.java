package pictures.cunny.client.mixin;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ChatComponent.class, priority = 999999999)
public class ChatComponentMixin {
    /**
     * @author ViaTi
     * @reason Let's not log all of our chat locally, maybe.
     */
    @Overwrite
    private void logChatMessage(Component component, @Nullable GuiMessageTag guiMessageTag) {}
}
