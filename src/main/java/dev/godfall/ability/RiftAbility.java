package dev.godfall.ability;

import dev.godfall.data.GodfallPlayerData;
import dev.godfall.data.GodfallSavedData;
import dev.godfall.network.NetworkHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class RiftAbility {
    private static final long COOLDOWN_TICKS = 100L;
    private static final double MAX_DISTANCE = 5.0D;

    private RiftAbility() {
    }

    public static int cast(ServerPlayer player) {
        GodfallSavedData savedData = GodfallSavedData.get(player.getServer());
        GodfallPlayerData data = savedData.getOrCreate(player.getUUID());

        if (!data.hasAbility("godfall:rift")) {
            player.sendSystemMessage(Component.literal(
                    "Ты пока не помнишь, как разрывать пространство."
            ));
            return 0;
        }

        long now = player.getServer().overworld().getGameTime();
        long remaining = data.getRiftCooldownUntil() - now;
        if (remaining > 0L) {
            player.sendSystemMessage(Component.literal(
                    "Пространство ещё не восстановилось. Осталось: "
                            + ((remaining + 19L) / 20L) + " сек."
            ));
            return 0;
        }

        Vec3 look = player.getLookAngle();
        Vec3 horizontal = new Vec3(look.x, 0.0D, look.z);
        if (horizontal.lengthSqr() < 1.0E-5D) {
            float yaw = player.getYRot() * ((float) Math.PI / 180.0F);
            horizontal = new Vec3(-Math.sin(yaw), 0.0D, Math.cos(yaw));
        } else {
            horizontal = horizontal.normalize();
        }

        double destinationX = player.getX();
        double destinationZ = player.getZ();
        boolean found = false;

        for (double distance = MAX_DISTANCE; distance >= 2.0D; distance -= 0.5D) {
            double x = player.getX() + horizontal.x * distance;
            double z = player.getZ() + horizontal.z * distance;
            AABB destinationBox = player.getBoundingBox().move(
                    x - player.getX(), 0.0D, z - player.getZ()
            );

            if (player.serverLevel().getWorldBorder().isWithinBounds(destinationBox)
                    && player.serverLevel().noCollision(player, destinationBox)) {
                destinationX = x;
                destinationZ = z;
                found = true;
                break;
            }
        }

        if (!found) {
            player.sendSystemMessage(Component.literal(
                    "Разлом не нашёл безопасного места по ту сторону пространства."
            ));
            return 0;
        }

        double startX = player.getX();
        double startY = player.getY();
        double startZ = player.getZ();

        player.teleportTo(destinationX, player.getY(), destinationZ);
        data.setRiftCooldownUntil(now + COOLDOWN_TICKS);
        savedData.setDirty();

        player.serverLevel().sendParticles(
                ParticleTypes.PORTAL, startX, startY + 0.9D, startZ,
                24, 0.35D, 0.55D, 0.35D, 0.12D
        );
        player.serverLevel().sendParticles(
                ParticleTypes.REVERSE_PORTAL, destinationX, startY + 0.9D, destinationZ,
                32, 0.45D, 0.65D, 0.45D, 0.08D
        );
        player.serverLevel().playSound(
                null, startX, startY, startZ,
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.65F, 0.8F
        );
        player.serverLevel().playSound(
                null, destinationX, startY, destinationZ,
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.55F, 1.25F
        );

        NetworkHandler.sendDialogue(
                player,
                "Хм. Пространство всё ещё помнит тебя. Жаль, что ты сам почти ничего не помнишь."
        );
        player.sendSystemMessage(Component.literal("Пространство поддалось твоей воле."));
        return 1;
    }
}
