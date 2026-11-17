package dev.godfall.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.godfall.data.GodfallPlayerData;
import dev.godfall.data.GodfallSavedData;
import dev.godfall.dialogue.EchoDialogue;
import dev.godfall.network.NetworkHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public final class EchoEntity extends PathfinderMob {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(EchoEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> OWNER_NAME =
            SynchedEntityData.defineId(EchoEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SKIN_VALUE =
            SynchedEntityData.defineId(EchoEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SKIN_SIGNATURE =
            SynchedEntityData.defineId(EchoEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SLIM_ARMS =
            SynchedEntityData.defineId(EchoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DEATH_INDEX =
            SynchedEntityData.defineId(EchoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ROLE =
            SynchedEntityData.defineId(EchoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> ECHO_ID =
            SynchedEntityData.defineId(EchoEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private long lastSpokeAt = -1000L;

    public EchoEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        setPersistenceRequired();
        setInvulnerable(true);
        setSilent(true);
    }

    @Override
    protected void registerGoals() {
        // Echoes follow their owner through the server-side tick logic below.
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(OWNER_UUID, Optional.empty());
        entityData.define(OWNER_NAME, "");
        entityData.define(SKIN_VALUE, "");
        entityData.define(SKIN_SIGNATURE, "");
        entityData.define(SLIM_ARMS, false);
        entityData.define(DEATH_INDEX, 0);
        entityData.define(ROLE, 0);
        entityData.define(ECHO_ID, Optional.empty());
    }

    public void configure(UUID ownerId, String ownerName, UUID echoId, int deathIndex,
                          EchoRole role, String skinValue, String skinSignature, boolean slimArms) {
        entityData.set(OWNER_UUID, Optional.of(ownerId));
        entityData.set(OWNER_NAME, ownerName == null ? "" : ownerName);
        entityData.set(ECHO_ID, Optional.of(echoId));
        entityData.set(DEATH_INDEX, Math.max(0, deathIndex));
        entityData.set(ROLE, role.ordinal());
        entityData.set(SKIN_VALUE, skinValue == null ? "" : skinValue);
        entityData.set(SKIN_SIGNATURE, skinSignature == null ? "" : skinSignature);
        entityData.set(SLIM_ARMS, slimArms);
    }

    public UUID getOwnerUUID() { return entityData.get(OWNER_UUID).orElse(null); }
    public String getOwnerName() { return entityData.get(OWNER_NAME); }
    public UUID getEchoId() { return entityData.get(ECHO_ID).orElse(null); }
    public int getDeathIndex() { return entityData.get(DEATH_INDEX); }
    public EchoRole getRole() { return EchoRole.fromOrdinal(entityData.get(ROLE)); }
    public String getSkinValue() { return entityData.get(SKIN_VALUE); }
    public String getSkinSignature() { return entityData.get(SKIN_SIGNATURE); }
    public boolean hasSlimArms() { return entityData.get(SLIM_ARMS); }

    public GameProfile getOwnerProfile() {
        UUID ownerId = getOwnerUUID();
        GameProfile profile = new GameProfile(
                ownerId == null ? new UUID(0L, 0L) : ownerId,
                getOwnerName().isBlank() ? "Unknown" : getOwnerName()
        );

        String value = getSkinValue();
        if (!value.isBlank()) {
            String signature = getSkinSignature();
            Property textures = signature.isBlank()
                    ? new Property("textures", value)
                    : new Property("textures", value, signature);
            profile.getProperties().put("textures", textures);
        }
        return profile;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        UUID ownerId = getOwnerUUID();
        if (ownerId == null || !ownerId.equals(player.getUUID())) {
            return InteractionResult.PASS;
        }

        if (!level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            long now = level().getGameTime();
            if (now - lastSpokeAt >= 40L) {
                lastSpokeAt = now;
                GodfallPlayerData data = GodfallSavedData.get(serverPlayer.getServer())
                        .getOrCreate(serverPlayer.getUUID());
                NetworkHandler.sendDialogue(
                        serverPlayer,
                        EchoDialogue.line(getRole(), data.getMemoryFragments(), random)
                );
            }
        }

        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            if (tickCount % 6 == 0) {
                level().addParticle(
                        ParticleTypes.SOUL,
                        getX() + (random.nextDouble() - 0.5D) * 0.55D,
                        getY() + random.nextDouble() * 1.55D,
                        getZ() + (random.nextDouble() - 0.5D) * 0.55D,
                        0.0D, 0.015D, 0.0D
                );
            }
            return;
        }

        if (tickCount % 10 != 0 || !(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        UUID ownerId = getOwnerUUID();
        ServerPlayer owner = ownerId == null
                ? null
                : serverLevel.getServer().getPlayerList().getPlayer(ownerId);

        if (owner == null || owner.serverLevel() != serverLevel) {
            discard();
            return;
        }

        if (distanceToSqr(owner) > 20.25D) {
            getNavigation().moveTo(owner, 0.95D);
        } else if (distanceToSqr(owner) < 3.0D) {
            getNavigation().stop();
        }
        getLookControl().setLookAt(owner, 25.0F, 25.0F);
    }

    @Override
    public boolean broadcastToPlayer(ServerPlayer player) {
        UUID ownerId = getOwnerUUID();
        return ownerId != null && ownerId.equals(player.getUUID());
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        UUID ownerId = getOwnerUUID();
        return ownerId == null || !ownerId.equals(player.getUUID());
    }

    @Override
    public boolean shouldBeSaved() {
        // Echoes are reconstructed from the owner's saved death history on login.
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
}
