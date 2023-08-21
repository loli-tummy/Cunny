package pictures.cunny.client.utility;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static pictures.cunny.client.Cunny.mc;


public class FakePlayerEntity extends LivingEntity {
    public static LivingEntity mockEntity;
    public float actualDamage = 0f;
    public Vec3 expectedPos = Vec3.ZERO;
    public boolean workingHealth = false;
    public boolean noMocking = false;
    public Iterable<ItemStack> armor;

    public FakePlayerEntity(EntityType<? extends LivingEntity> type) {
        super(type, Objects.requireNonNull(mc.player).level());
    }

    public FakePlayerEntity() {
        this(EntityType.VILLAGER);
    }

    @Override
    public @NotNull Pose getPose() {
        assert mc.player != null;
        return mc.player.getPose();
    }

    @Override
    public int getExperienceReward() {
        return 1337;
    }

    @Override
    public boolean ignoreExplosion() {
        if (this.noMocking) return super.ignoreExplosion();
        return false;
    }

    @Override
    public @NotNull ItemStack getOffhandItem() {
        return Items.TOTEM_OF_UNDYING.getDefaultInstance();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (this.noMocking) return super.isInvulnerableTo(damageSource);
        return false;
    }

    @Override
    public float getAbsorptionAmount() {
        if (this.noMocking) return super.getAbsorptionAmount();
        if (mockEntity != null) {
            return mockEntity.getAbsorptionAmount();
        } else {
            assert mc.player != null;
            return mc.player.getAbsorptionAmount();
        }
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        if (noMocking) return armor;
        if (mockEntity != null) {
            return mockEntity.getArmorSlots();
        } else {
            assert mc.player != null;
            return mc.player.getArmorSlots();
        }
    }

    @Override
    public int getArmorValue() {
        if (noMocking) {
            return super.getArmorValue();
        }
        if (mockEntity != null) {
            return mockEntity.getArmorValue();
        } else {
            assert mc.player != null;
            return mc.player.getArmorValue();
        }
    }

    @Override
    public @NotNull ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        if (mockEntity != null) {
            return mockEntity.getItemBySlot(equipmentSlot);
        } else {
            assert mc.player != null;
            return mc.player.getItemBySlot(equipmentSlot);
        }
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }


    @Override
    public void die(DamageSource damageSource) {
        this.setHealth(20);
        if (this.isRemoved()) this.unsetRemoved();
    }


    @Override
    public boolean isDeadOrDying() {
        this.setHealth(20);
        this.dead = false;
        return false;
    }

    public void setActualDamage(float f) {
        this.actualDamage = f;
    }

    @Override
    public boolean isAlive() {
        if (noMocking) return super.isAlive();
        this.setHealth(20);
        return true;
    }


    @Override
    public float getHealth() {
        if (noMocking) {
            return this.getHealth();
        }
        if (mockEntity != null) {
            return mockEntity.getHealth();
        } else {
            assert mc.player != null;
            return mc.player.getHealth();
        }
    }

    public boolean toggleSmart() {
        this.noMocking = !noMocking;
        return this.noMocking;
    }


    public float getExplosionDamage(Vec3 pos, Entity entity2, float power) {
        mockEntity = (LivingEntity) entity2;
        this.setLevel(mockEntity.level());
        this.expectedPos = pos;
        this.lastHurt = 0;
        this.hurtTime = 0;
        this.invulnerableTime = 0;
        this.dead = false;
        this.setHealth(20);
        this.setPos(mockEntity.position().x, mockEntity.position().y, mockEntity.position().z);
        Explosion explosion = new Explosion(level(), null, pos.x(), pos.y(), pos.z(), power, true, Explosion.BlockInteraction.KEEP);
        explosion.finalizeExplosion(false);
        this.setDeltaMovement(0, 0, 0);
        return this.actualDamage;
    }
}
