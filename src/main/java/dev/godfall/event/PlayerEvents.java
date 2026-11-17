package dev.godfall.event;

import dev.godfall.data.GodfallPlayerData;
import dev.godfall.data.GodfallSavedData;
import dev.godfall.dialogue.ObserverDialogue;
import dev.godfall.entity.EchoEntity;
import dev.godfall.manager.EchoManager;
import dev.godfall.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class PlayerEvents {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        GodfallSavedData savedData = GodfallSavedData.get(player.getServer());
        GodfallPlayerData data = savedData.getOrCreate(player.getUUID());

        if (!data.hasSeenIntro()) {
            data.markIntroSeen();
            savedData.setDirty();
            NetworkHandler.sendDialogue(player, ObserverDialogue.firstAwakening());
        }

        EchoManager.refresh(player);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        GodfallSavedData savedData = GodfallSavedData.get(player.getServer());
        GodfallPlayerData data = savedData.getOrCreate(player.getUUID());
        data.recordDeath(player);
        savedData.setDirty();
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        GodfallPlayerData data = GodfallSavedData.get(player.getServer()).getOrCreate(player.getUUID());
        EchoEntity newestEcho = EchoManager.refresh(player);

        if (data.getDeathCount() > 0) {
            NetworkHandler.sendDialogue(
                    player,
                    ObserverDialogue.afterDeath(data.getDeathCount())
            );
        }

        if (newestEcho != null) {
            NetworkHandler.sendDialogue(player, newestEcho.getRole().greeting());
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            EchoManager.refresh(player);
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            EchoManager.remove(player);
        }
    }
}
