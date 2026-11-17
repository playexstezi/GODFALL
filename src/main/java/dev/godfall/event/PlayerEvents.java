package dev.godfall.event;
import dev.godfall.data.GodfallPlayerData;
import dev.godfall.data.GodfallSavedData;
import dev.godfall.dialogue.ObserverDialogue;
import dev.godfall.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
public final class PlayerEvents {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        GodfallSavedData savedData = GodfallSavedData.get(player.getServer());
        GodfallPlayerData data = savedData.getOrCreate(player.getUUID());
        if (!data.hasSeenIntro()) {
            data.markIntroSeen();
            savedData.setDirty();
            NetworkHandler.sendDialogue(player, ObserverDialogue.firstAwakening());
        }
    }
    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        GodfallSavedData savedData = GodfallSavedData.get(player.getServer());
        GodfallPlayerData data = savedData.getOrCreate(player.getUUID());
        data.recordDeath(player);
        savedData.setDirty();
    }
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        GodfallPlayerData data = GodfallSavedData.get(player.getServer()).get(player.getUUID());
        if (data != null && data.getDeathCount() > 0) {
            NetworkHandler.sendDialogue(player, ObserverDialogue.afterDeath(data.getDeathCount()));
        }
    }
}
