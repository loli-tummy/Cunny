package pictures.cunny.client.utility;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Objects;

import static pictures.cunny.client.Cunny.mc;

public class EntityUtils {
    private static final List<EntityType<?>> collidable = List.of(EntityType.ITEM, EntityType.TRIDENT, EntityType.ARROW, EntityType.AREA_EFFECT_CLOUD);
    private static final List<Direction> horizontals = List.of(Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST);

    public static List<Direction> getHorizontals() {
        return horizontals;
    }

    public static float getActualHealth() {
        assert mc.player != null;
        return mc.player.getHealth() + mc.player.getAbsorptionAmount();
    }

    public static TypeOfEntity getType(Entity entity) {
        switch (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath()) {
            case "creeper":
                if (((Creeper) entity).isIgnited()) return TypeOfEntity.EXPLOSION;
            case "tnt":
                return TypeOfEntity.EXPLOSION_INANIMATE;
            case "end_crystal":
                return TypeOfEntity.EXPLOSION;
            case "llama":
                if (((Llama) entity).isAggressive()) return TypeOfEntity.HOSTILE;
                return TypeOfEntity.RIDEABLE;
            case "wolf":
                assert mc.player != null;
                if (((Wolf) entity).getOwnerUUID() != null) {
                    if (((Wolf) entity).getOwnerUUID() == mc.player.getUUID()) return TypeOfEntity.PET;
                }
                if (((Wolf) entity).isAngryAt(mc.player)) {
                    return TypeOfEntity.HOSTILE;
                }
                return TypeOfEntity.NEUTRAL;
            case "cat":
                if (((Cat) entity).getOwnerUUID() != null) return TypeOfEntity.PET;
                return TypeOfEntity.PASSIVE;
            case "enderman":
                if (((EnderMan) entity).isAggressive() && ((EnderMan) entity).isAngryAt(mc.player)) {
                    return TypeOfEntity.HOSTILE;
                }
                return TypeOfEntity.NEUTRAL;
            case "pig":
                if (((Pig) entity).isSaddled()) return TypeOfEntity.RIDEABLE;
                return TypeOfEntity.PASSIVE;
            case "mule":
            case "donkey":
            case "skeleton_horse":
            case "zombie_horse":
            case "horse":
                if (((AbstractHorse) entity).getOwnerUUID() != null) return TypeOfEntity.PET;
                return TypeOfEntity.RIDEABLE;
            case "minecart":
            case "boat":
                return TypeOfEntity.RIDEABLE;
            default:
                if (!entity.isAlive() && entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() <= 0) {
                    return TypeOfEntity.UNKNOWN;
                }
                if (entity instanceof Player) {
                    if (entity.getStringUUID() == Objects.requireNonNull(mc.player).getStringUUID()) {
                        return TypeOfEntity.SELF;
                    }
                    return TypeOfEntity.PLAYER;
                }

                if (!entity.isAttackable()) {
                    return TypeOfEntity.INANIMATE;
                }

                if (entity.getType().getCategory().isFriendly()) {
                    return TypeOfEntity.PASSIVE;
                } else {
                    return TypeOfEntity.HOSTILE;
                }
        }
    }

    public static boolean canPlaceIn(Entity entity) {
        return collidable.contains(entity.getType()) || entity.isRemoved() || entity.isSpectator();
    }

    public enum TypeOfEntity {
        RIDEABLE,
        NEUTRAL,
        PASSIVE,
        HOSTILE,
        INANIMATE,
        EXPLOSION_INANIMATE,
        EXPLOSION,
        UNKNOWN,
        PLAYER,
        PET,
        SELF
    }
}
