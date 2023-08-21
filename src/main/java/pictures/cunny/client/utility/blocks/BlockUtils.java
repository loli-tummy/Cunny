package pictures.cunny.client.utility.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import pictures.cunny.client.Cunny;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.config.sub.enums.ActionMode;
import pictures.cunny.client.config.sub.enums.ActionType;
import pictures.cunny.client.config.sub.enums.AirPlaceMode;
import pictures.cunny.client.config.sub.enums.SwitchMode;
import pictures.cunny.client.mixin.IClipContext;
import pictures.cunny.client.utility.EntityUtils;
import pictures.cunny.client.utility.InventoryUtils;
import pictures.cunny.client.utility.PacketUtils;
import pictures.cunny.client.utility.RotationUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static pictures.cunny.client.Cunny.mc;

public class BlockUtils {
    public static final RaytracingWrapper raytracing = new RaytracingWrapper();
    public static final Direction[] HORIZONTALS = {Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST};
    public static final Direction[] ALL = {Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST, Direction.UP, Direction.DOWN};
    public static Block[] SIGNS;

    public static String getKey(Block v) {
        return BuiltInRegistries.BLOCK.getKey(v).getPath();
    }

    public static boolean isItem(BlockPos pos, Item... items) {
        return mc.player != null
                && Arrays.stream(items).toList().contains(mc.player.level().getBlockState(pos).getBlock().asItem());
    }

    public static boolean isItem(BlockPos pos, List<Item> items) {
        return mc.player != null
                && items.contains(mc.player.level().getBlockState(pos).getBlock().asItem());
    }

    //Misc
    public static boolean isContainer(BlockPos pos) {
        assert mc.player != null;
        Block block = mc.player.level().getBlockState(pos).getBlock();
        return mc.player != null
                && (block == Blocks.CHEST
                || block == Blocks.TRAPPED_CHEST
                || isShulker(mc.player.level().getBlockState(pos).getBlock())
                || block == Blocks.BARREL
                || block == Blocks.ENDER_CHEST
                || block == Blocks.HOPPER);
    }

    public static boolean isContainer(Block block) {
        return mc.player != null
                && (block == Blocks.CHEST
                || block == Blocks.TRAPPED_CHEST
                || isShulker(block)
                || block == Blocks.BARREL
                || block == Blocks.ENDER_CHEST
                || block == Blocks.HOPPER);
    }

    public static boolean isShulker(Block block) {
        return getKey(block).endsWith("shulker_box");
    }

    public static boolean isBed(BlockPos pos) {
        assert mc.player != null;
        return getKey(mc.player.level().getBlockState(pos).getBlock()).endsWith("_bed");
    }

    public static boolean isBed(Block block) {
        return getKey(block).endsWith("_bed");
    }

    public static boolean testBlock(BlockPos pos, Predicate<Block> predicate) {
        return predicate.test(mc.player.level().getBlockState(pos).getBlock());
    }

    public static boolean isBlock(BlockPos pos, Block... blocks) {
        return mc.player != null
                && Arrays.stream(blocks).toList().contains(mc.player.level().getBlockState(pos).getBlock());
    }

    public static boolean isBlock(double x, double y, double z, Block... blocks) {
        return mc.player != null
                && Arrays.stream(blocks)
                .toList()
                .contains(mc.player.level().getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock());
    }

    public static boolean isBlock(int x, int y, int z, Block... blocks) {
        return mc.player != null
                && Arrays.stream(blocks)
                .toList()
                .contains(mc.player.level().getBlockState(new BlockPos(x, y, z)).getBlock());
    }

    public static boolean canInstantMine(BlockPos pos) {
        assert mc.player != null;
        return mc.player.level().getBlockState(pos).getDestroySpeed(mc.player.level(), pos) < 0.3;
    }

    public static boolean doesNotExplode(BlockPos pos) {
        if (isReplaceable(pos)) return true;
        assert mc.player != null;
        return mc.player.level().getBlockState(pos).getBlock().getExplosionResistance() > 100;
    }

    public static boolean isReplaceable(BlockPos pos) {
        return mc.player != null && mc.player.level().getBlockState(pos).isAir()
                || mc.player.level().getBlockState(pos).canBeReplaced();
    }

    public static boolean isLiquid(BlockPos pos) {
        return mc.player != null && !mc.player.level().getBlockState(pos).getFluidState().isEmpty();
    }

    public static BlockState getState(BlockPos pos) {
        assert mc.player != null;
        return mc.player.level().getBlockState(pos);
    }

    public static boolean canUse(BlockPos pos) {
        Block block = getState(pos).getBlock();
        return isContainer(pos)
                || block instanceof LeverBlock
                || block instanceof ButtonBlock
                || block instanceof DoorBlock
                || block instanceof DragonEggBlock
                || block instanceof AnvilBlock
                || block instanceof TrapDoorBlock
                || isBed(block);
    }

    // Crystal utilities
    public static boolean isAir(BlockPos pos) {
        return mc.player != null && mc.player.level().getBlockState(pos).isAir();
    }

    public static boolean hasCrystal(BlockPos pos) {
        return mc.player != null
                && mc.player
                .level()
                .getEntities(
                        EntityTypeTest.forClass(EndCrystal.class),
                        AABB.ofSize(center(pos), 1, 1, 1),
                        entity -> entity.blockPosition().equals(pos))
                .size()
                >= 1;
    }

    public static List<EndCrystal> imposedCrystals(BlockPos pos) {
        assert mc.player != null;
        return mc.player.level().getEntities(EntityTypeTest.forClass(EndCrystal.class), new AABB(pos.offset(3, 3, 3), pos.offset(-3, -3, -3)), entity -> {
            assert mc.level != null;
            return entity.isColliding(pos, Blocks.BEDROCK.defaultBlockState()) || isOnSurround(entity);
        });
    }

    public static boolean isOnSurround(EndCrystal crystal) {
        for (Direction direction : HORIZONTALS) {
            assert mc.player != null;
            BlockPos offset = mc.player.blockPosition().offset(direction.getNormal());
            if (crystal.blockPosition().equals(offset.offset(Direction.UP.getNormal()))
                    || crystal.blockPosition().equals(offset.offset(Direction.DOWN.getNormal()))) {
                return true;
            }
        }
        return false;
    }

    public static boolean canPlace(BlockPos pos) {
        assert mc.player != null;

        if (Config.get().placing.mode == ActionMode.Raytrace) {
            BlockHitResult hitResult = getBlockHitResult(pos);

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                Cunny.LOG.info("Block: {} {} {}, Direction: {}", hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ(), hitResult.getDirection().getName());
            }

            if (hitResult.getType() != HitResult.Type.BLOCK || !(hitResult.getBlockPos().offset(hitResult.getDirection().getNormal()).equals(pos) || hitResult.getBlockPos().offset(hitResult.getDirection().getOpposite().getNormal()).equals(pos))) {
                return false;
            }
        }

        List<Entity> entities = mc.player.level().getEntities((Entity) null, new AABB(pos.offset(3, 3, 3), pos.offset(-3, -3, -3)), entity -> {
            if (EntityUtils.canPlaceIn(entity)) {
                return false;
            }

            return entity.isColliding(pos, Blocks.BEDROCK.getStateDefinition().any());
        });

        return isReplaceable(pos) && entities.size() == 0 && !shouldAirPlace(pos);
    }

    public static boolean hasEntitiesInside(BlockPos pos) {
        assert mc.player != null;
        List<Entity> entities = mc.player.level().getEntities((Entity) null, new AABB(pos.offset(3, 3, 3), pos.offset(-3, -3, -3)), entity -> {
            if (EntityUtils.canPlaceIn(entity)) {
                return false;
            }

            return entity.isColliding(pos, Blocks.BEDROCK.getStateDefinition().any());
        });
        return entities.size() != 0;
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        return isBlock(pos, Blocks.OBSIDIAN, Blocks.BEDROCK) && isAir(pos.relative(Direction.UP));
    }

    public static BlockPos getCevPos(Player player) {
        BlockPos pos = player.getOnPos().relative(Direction.UP);
        return null;
    }

    public static Direction getPlaceDirection(BlockPos pos) {
        if (Config.get().placing.airPlace == AirPlaceMode.Horizon) return Direction.UP;
        for (Direction direction : ALL) {
            if (!isAir(pos.relative(direction))) return direction;
        }
        return Direction.DOWN;
    }

    public static BlockPos isOnEntity(BlockPos pos, Entity entity) {
        for (Direction dir : ALL) {
            if (pos.relative(dir).equals(entity.blockPosition()))
                return pos.relative(dir);
            if (pos.relative(dir)
                    .relative(Direction.UP).equals(entity.blockPosition().relative(Direction.UP)))
                return pos.relative(dir);
        }
        return pos;
    }

    public static boolean shouldAirPlace(BlockPos pos) {
        for (Direction direction : ALL) {
            if (!BlockUtils.isAir(pos.offset(direction.getNormal()))) return false;
        }
        return true;
    }

    public static Map.Entry<Float, Float> getRotation(BlockPos pos) {
        if (Config.get().placing.mode == ActionMode.Raytrace) {
            for (Direction direction : HORIZONTALS) {
                BlockPos pos1 = pos.offset(direction.getNormal());
                if (!BlockUtils.isAir(pos1)) {
                    Vec3 vec3d = new Vec3((double) pos.getX() + ((double) direction.getOpposite().getStepX() * 0.5),
                            (double) pos.getY() + ((double) direction.getOpposite().getStepY() * 0.5),
                            (double) pos.getZ() + ((double) direction.getOpposite().getStepZ() * 0.5));
                    float yaw = RotationUtils.getYaw(vec3d), pitch = RotationUtils.getPitch(vec3d);
                    if (canRaycast(pos, pitch, yaw)) {
                        return Map.entry(yaw, pitch);
                    }
                }
            }
            return Map.entry(RotationUtils.getYaw(pos), RotationUtils.getPitch(pos));
        }
        return Map.entry(RotationUtils.getYaw(clickOffset(pos)), RotationUtils.getPitch(clickOffset(pos)));
        //Rotation normalized = rotation.get().normalizeAndClamp();
        //return Map.entry(normalized.getYaw(), normalized.getPitch());
    }

    public static boolean canRaycast(BlockPos pos, float pitch, float yaw) {
        assert mc.player != null;
        Cunny.LOG.info("Yaw {}, Pitch  {}", yaw, pitch);

        Vec3 vec3d = mc.player.getEyePosition();
        Vec3 vec3d2 = mc.player.calculateViewVector(pitch, yaw);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * Config.get().placing.distance, vec3d2.y * Config.get().placing.distance, vec3d2.z * Config.get().placing.distance);
        ((IClipContext) raytracing.getContext()).setFrom(vec3d);
        ((IClipContext) raytracing.getContext()).setTo(vec3d3);
        ((IClipContext) raytracing.getContext()).setCollisionContext(CollisionContext.of(mc.player));
        BlockHitResult hitResult = mc.player.level().clip(raytracing.getContext());

        return hitResult.getType() == HitResult.Type.BLOCK && hitResult.getBlockPos().relative(hitResult.getDirection()).equals(pos);
    }

    public static Vec3 center(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public static Vec3 clickOffset(BlockPos pos) {
        return new Vec3((double) pos.getX() + ((double) getPlaceDirection(pos).getStepX() * 0.5), (double) pos.getY() + ((double) getPlaceDirection(pos).getStepY() * 0.5), (double) pos.getZ() + ((double) getPlaceDirection(pos).getStepZ() * 0.5));
        //return new Vec3d(pos.getX() + Math.min(0.9f, Math.random()), pos.getY() + Math.min(0.9f, Math.random()), pos.getZ() + Math.min(0.9f, Math.random()));
    }

    public static BlockHitResult getBlockHitResult(BlockPos pos) {
        return getBlockHitResult(pos, null);
    }

    public static BlockHitResult getBlockHitResult(BlockPos pos, Direction direction) {
        if (Config.get().placing.mode == ActionMode.Raytrace) {
            assert mc.player != null;

            Map.Entry<Float, Float> rotation = getRotation(pos);
            Vec3 vec3d = mc.player.getEyePosition();
            Vec3 vec3d2 = mc.player.calculateViewVector(rotation.getValue(), rotation.getKey());
            Vec3 vec3d3 = vec3d.add(vec3d2.x * Config.get().placing.distance, vec3d2.y * Config.get().placing.distance, vec3d2.z * Config.get().placing.distance);
            ((IClipContext) raytracing.getContext()).setFrom(vec3d);
            ((IClipContext) raytracing.getContext()).setTo(vec3d3);
            ((IClipContext) raytracing.getContext()).setCollisionContext(CollisionContext.of(mc.player));

            return mc.player.level().clip(raytracing.getContext());
        }
        return new BlockHitResult(clickOffset(pos), direction == null ? getPlaceDirection(pos).getOpposite() : direction, pos.relative(direction == null ? getPlaceDirection(pos) : direction.getOpposite()), false);
    }

    public static boolean placeBlock(int item, BlockPos pos) {
        assert mc.player != null;
        assert mc.gameMode != null;


        if (canUse(pos)) {
            PacketUtils.send(new ServerboundPlayerInputPacket(mc.player.xxa, mc.player.zza, mc.player.input.jumping, true));
            mc.player.setShiftKeyDown(true);
        }

        if (item == 0) return false;

        Direction dir = null;

        switch (Config.get().placing.rotationMode) {
            case Default -> {
                Map.Entry<Float, Float> rot = getRotation(pos);
                RotationUtils.rotate(rot.getValue(), rot.getKey());
            }

            case Packet -> {
                Map.Entry<Float, Float> rot = getRotation(pos);
                PacketUtils.send(new ServerboundMovePlayerPacket.Rot(rot.getValue(), rot.getKey(), mc.player.onGround()));
            }
        }

        InteractionHand hand = InteractionHand.MAIN_HAND;

        int prevSlot = mc.player.getInventory().selected;

        if (InventoryUtils.isHotbarSlot(item)) {
            InventoryUtils.swapSlot(InventoryUtils.getHotbarId(item), Config.get().inventory.switching == SwitchMode.Silent);
        } else {
            InventoryUtils.swapSlot(InventoryUtils.findEmptySlotInHotbar(7), Config.get().inventory.switching == SwitchMode.Silent);
            PacketUtils.send(new ServerboundPickItemPacket(item));
        }

        if (shouldAirPlace(pos) || Config.get().placing.airPlace == AirPlaceMode.Horizon) {
            switch (Config.get().placing.airPlace) {
                case None -> {
                    return false;
                }
                case Horizon -> {
                    BlockPos airPos = pos.relative(Direction.DOWN);
                    if (Config.get().placing.type == ActionType.Packet) {
                        BlockHandling.swing();
                        PacketUtils.send(new ServerboundUseItemOnPacket(hand, getBlockHitResult(airPos), 0));
                    } else {
                        BlockHandling.swing();
                        mc.gameMode.useItemOn(mc.player, hand, getBlockHitResult(airPos));
                    }
                }
            }
        }

        if (Config.get().placing.type == ActionType.Packet) {
            BlockHandling.swing();
            PacketUtils.send(new ServerboundUseItemOnPacket(hand, getBlockHitResult(pos), 0));
        } else {
            BlockHandling.swing();
            mc.gameMode.useItemOn(mc.player, hand, getBlockHitResult(pos));
        }

        if (Config.get().inventory.switching == SwitchMode.Silent) {
            mc.player.getInventory().selected = prevSlot;
            InventoryUtils.syncHand();
        }

        if (canUse(pos)) {
            PacketUtils.send(new ServerboundPlayerInputPacket(mc.player.xxa, mc.player.zza, mc.player.input.jumping, false));
            mc.player.setShiftKeyDown(false);
        }
        return true;
    }
}
