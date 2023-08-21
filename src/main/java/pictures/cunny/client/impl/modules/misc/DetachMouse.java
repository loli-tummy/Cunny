package pictures.cunny.client.impl.modules.misc;

import pictures.cunny.client.framework.events.EventListener;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.impl.events.game.TickEvent;

public class DetachMouse extends Module {
    public DetachMouse() {
        super(Categories.MISC, "detach-mouse", "Detaches the mouse from the game.");
    }

    @EventListener
    public void onTick(TickEvent.Pre event) {
        if (mc.mouseHandler.isMouseGrabbed()) {
            mc.mouseHandler.releaseMouse();
        }
    }
}
