package dev.godfall.progression;
import net.minecraft.network.chat.Component;
public enum DivineStage {
    FORGETTING("Забвение", 0),
    AWAKENING("Пробуждение", 1),
    SPARK("Искра", 3),
    ASCENSION("Возвышение", 6),
    RETURN("Возвращение", 10),
    CREATOR("Создатель", 15);
    private final String displayName;
    private final int requiredFragments;
    DivineStage(String displayName, int requiredFragments) {
        this.displayName = displayName;
        this.requiredFragments = requiredFragments;
    }
    public String displayName() { return displayName; }
    public Component component() { return Component.literal(displayName); }
    public int requiredFragments() { return requiredFragments; }
    public static DivineStage forFragments(int fragments) {
        DivineStage result = FORGETTING;
        for (DivineStage stage : values()) {
            if (fragments >= stage.requiredFragments) result = stage;
        }
        return result;
    }
    public static DivineStage fromName(String name) {
        try { return valueOf(name); }
        catch (IllegalArgumentException exception) { return FORGETTING; }
    }
}
