package pictures.cunny.client.config.sub;


import pictures.cunny.client.config.sub.enums.ActionMode;
import pictures.cunny.client.config.sub.enums.ActionType;
import pictures.cunny.client.config.sub.enums.RotationMode;

public class Interaction {
    public RotationMode rotationMode = RotationMode.Default;
    public ActionType type = ActionType.Packet;
    public ActionMode mode = ActionMode.Raytrace;
    public double distance = 4.5;
}
