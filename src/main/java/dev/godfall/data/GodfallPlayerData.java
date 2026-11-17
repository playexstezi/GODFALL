package dev.godfall.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import dev.godfall.progression.DivineStage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class GodfallPlayerData {
    private int deathCount;
    private int memoryFragments;
    private boolean introSeen;
    private DivineStage stage = DivineStage.FORGETTING;
    private final Set<String> unlockedAbilities = new HashSet<>();
    private final ListTag deathHistory = new ListTag();

    public int getDeathCount() { return deathCount; }
    public int getMemoryFragments() { return memoryFragments; }
    public DivineStage getStage() { return stage; }
    public boolean hasSeenIntro() { return introSeen; }
    public void markIntroSeen() { introSeen = true; }

    public void addMemoryFragment() {
        memoryFragments++;
        stage = DivineStage.forFragments(memoryFragments);
    }

    public void setMemoryFragments(int memoryFragments) {
        this.memoryFragments = Math.max(0, memoryFragments);
        stage = DivineStage.forFragments(this.memoryFragments);
    }

    public boolean unlockAbility(String abilityId) {
        return unlockedAbilities.add(abilityId);
    }

    public boolean hasAbility(String abilityId) {
        return unlockedAbilities.contains(abilityId);
    }

    public Set<String> getUnlockedAbilities() {
        return Set.copyOf(unlockedAbilities);
    }

    public ListTag getDeathHistoryCopy() {
        return deathHistory.copy();
    }

    public void recordDeath(ServerPlayer player) {
        deathCount++;

        CompoundTag death = new CompoundTag();
        death.putUUID("PlayerUUID", player.getUUID());
        death.putUUID("EchoId", UUID.randomUUID());
        death.putString("PlayerName", player.getGameProfile().getName());
        death.putString("Dimension", player.level().dimension().location().toString());
        death.putDouble("X", player.getX());
        death.putDouble("Y", player.getY());
        death.putDouble("Z", player.getZ());
        death.putFloat("Yaw", player.getYRot());
        death.putFloat("Pitch", player.getXRot());
        death.putLong("GameTime", player.level().getGameTime());

        String skinValue = "";
        String skinSignature = "";
        for (Property property : player.getGameProfile().getProperties().get("textures")) {
            skinValue = property.getValue();
            if (property.hasSignature()) {
                skinSignature = property.getSignature();
            }
            break;
        }
        death.putString("SkinValue", skinValue);
        death.putString("SkinSignature", skinSignature);
        death.putBoolean("SlimArms", isSlimSkin(skinValue));

        ListTag armor = new ListTag();
        for (ItemStack stack : player.getArmorSlots()) {
            armor.add(stack.save(new CompoundTag()));
        }
        death.put("Armor", armor);
        death.put("MainHand", player.getMainHandItem().save(new CompoundTag()));
        death.put("OffHand", player.getOffhandItem().save(new CompoundTag()));

        deathHistory.add(death);
    }

    private static boolean isSlimSkin(String textureValue) {
        if (textureValue == null || textureValue.isBlank()) {
            return false;
        }

        try {
            String decoded = new String(Base64.getDecoder().decode(textureValue), StandardCharsets.UTF_8);
            JsonObject root = JsonParser.parseString(decoded).getAsJsonObject();
            if (!root.has("textures") || !root.getAsJsonObject("textures").has("SKIN")) {
                return false;
            }

            JsonObject skin = root.getAsJsonObject("textures").getAsJsonObject("SKIN");
            if (!skin.has("metadata")) {
                return false;
            }

            JsonObject metadata = skin.getAsJsonObject("metadata");
            return metadata.has("model")
                    && "slim".equalsIgnoreCase(metadata.get("model").getAsString());
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("DeathCount", deathCount);
        tag.putInt("MemoryFragments", memoryFragments);
        tag.putBoolean("IntroSeen", introSeen);
        tag.putString("Stage", stage.name());

        ListTag abilities = new ListTag();
        for (String ability : unlockedAbilities) {
            CompoundTag entry = new CompoundTag();
            entry.putString("Id", ability);
            abilities.add(entry);
        }
        tag.put("UnlockedAbilities", abilities);
        tag.put("DeathHistory", deathHistory.copy());
        return tag;
    }

    public static GodfallPlayerData load(CompoundTag tag) {
        GodfallPlayerData data = new GodfallPlayerData();
        data.deathCount = Math.max(0, tag.getInt("DeathCount"));
        data.memoryFragments = Math.max(0, tag.getInt("MemoryFragments"));
        data.introSeen = tag.getBoolean("IntroSeen");
        data.stage = DivineStage.fromName(tag.getString("Stage"));

        ListTag abilities = tag.getList("UnlockedAbilities", Tag.TAG_COMPOUND);
        for (int i = 0; i < abilities.size(); i++) {
            String id = abilities.getCompound(i).getString("Id");
            if (!id.isBlank()) {
                data.unlockedAbilities.add(id);
            }
        }

        ListTag deaths = tag.getList("DeathHistory", Tag.TAG_COMPOUND);
        for (int i = 0; i < deaths.size(); i++) {
            data.deathHistory.add(deaths.getCompound(i).copy());
        }

        if (data.memoryFragments > 0 && !tag.contains("Stage", Tag.TAG_STRING)) {
            data.stage = DivineStage.forFragments(data.memoryFragments);
        }
        return data;
    }
}
