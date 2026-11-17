package dev.godfall.data;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public final class GodfallSavedData extends SavedData {
    private static final String DATA_NAME = "godfall_player_data";
    private final Map<UUID, GodfallPlayerData> players = new HashMap<>();
    public static GodfallSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                GodfallSavedData::load, GodfallSavedData::new, DATA_NAME
        );
    }
    public GodfallPlayerData getOrCreate(UUID playerId) {
        return players.computeIfAbsent(playerId, ignored -> new GodfallPlayerData());
    }
    public GodfallPlayerData get(UUID playerId) { return players.get(playerId); }
    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag playerList = new ListTag();
        for (Map.Entry<UUID, GodfallPlayerData> entry : players.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("PlayerUUID", entry.getKey());
            playerTag.put("Data", entry.getValue().save());
            playerList.add(playerTag);
        }
        tag.put("Players", playerList);
        return tag;
    }
    public static GodfallSavedData load(CompoundTag tag) {
        GodfallSavedData data = new GodfallSavedData();
        ListTag playerList = tag.getList("Players", Tag.TAG_COMPOUND);
        for (int i = 0; i < playerList.size(); i++) {
            CompoundTag playerTag = playerList.getCompound(i);
            if (playerTag.hasUUID("PlayerUUID") && playerTag.contains("Data", Tag.TAG_COMPOUND)) {
                data.players.put(playerTag.getUUID("PlayerUUID"),
                        GodfallPlayerData.load(playerTag.getCompound("Data")));
            }
        }
        return data;
    }
}
