package dev.godfall.manager;

import dev.godfall.data.GodfallPlayerData;
import dev.godfall.data.GodfallSavedData;
import dev.godfall.entity.EchoEntity;
import dev.godfall.entity.EchoRole;
import dev.godfall.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class EchoManager {
    private static final Map<UUID, List<EchoEntity>> ACTIVE_ECHOES = new HashMap<>();

    private EchoManager() {
    }

    public static EchoEntity refresh(ServerPlayer player) {
        remove(player);

        GodfallPlayerData playerData = GodfallSavedData.get(player.getServer())
                .getOrCreate(player.getUUID());
        ListTag history = playerData.getDeathHistoryCopy();
        if (history.isEmpty()) {
            return null;
        }

        List<EchoEntity> spawned = new ArrayList<>();
        int count = history.size();

        for (int index = 0; index < count; index++) {
            CompoundTag snapshot = history.getCompound(index);
            EchoEntity echo = ModEntities.ECHO.get().create(player.serverLevel());
            if (echo == null) {
                continue;
            }

            UUID echoId = snapshot.hasUUID("EchoId")
                    ? snapshot.getUUID("EchoId")
                    : UUID.nameUUIDFromBytes((player.getUUID() + ":" + index)
                            .getBytes(StandardCharsets.UTF_8));

            echo.configure(
                    player.getUUID(),
                    player.getGameProfile().getName(),
                    echoId,
                    index,
                    EchoRole.forDeathIndex(index),
                    snapshot.getString("SkinValue"),
                    snapshot.getString("SkinSignature"),
                    snapshot.getBoolean("SlimArms")
            );

            restoreEquipment(echo, snapshot);

            double angle = (Math.PI * 2.0D * index) / Math.max(1, count);
            double radius = 2.4D + (index % 5) * 0.42D;
            double x = player.getX() + Math.cos(angle) * radius;
            double z = player.getZ() + Math.sin(angle) * radius;
            echo.moveTo(x, player.getY(), z, player.getYRot(), 0.0F);
            echo.setYHeadRot(player.getYHeadRot());
            echo.setYBodyRot(player.getYRot());

            if (player.serverLevel().addFreshEntity(echo)) {
                spawned.add(echo);
            } else {
                echo.discard();
            }
        }

        ACTIVE_ECHOES.put(player.getUUID(), spawned);
        return spawned.isEmpty() ? null : spawned.get(spawned.size() - 1);
    }

    private static void restoreEquipment(EchoEntity echo, CompoundTag snapshot) {
        ListTag armor = snapshot.getList("Armor", Tag.TAG_COMPOUND);
        EquipmentSlot[] slots = {
                EquipmentSlot.FEET,
                EquipmentSlot.LEGS,
                EquipmentSlot.CHEST,
                EquipmentSlot.HEAD
        };

        for (int i = 0; i < Math.min(armor.size(), slots.length); i++) {
            echo.setItemSlot(slots[i], ItemStack.of(armor.getCompound(i)));
        }

        if (snapshot.contains("MainHand", Tag.TAG_COMPOUND)) {
            echo.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.of(snapshot.getCompound("MainHand")));
        }
        if (snapshot.contains("OffHand", Tag.TAG_COMPOUND)) {
            echo.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.of(snapshot.getCompound("OffHand")));
        }
    }

    public static void remove(ServerPlayer player) {
        List<EchoEntity> echoes = ACTIVE_ECHOES.remove(player.getUUID());
        if (echoes == null) {
            return;
        }

        for (EchoEntity echo : echoes) {
            if (!echo.isRemoved()) {
                echo.discard();
            }
        }
    }
}
