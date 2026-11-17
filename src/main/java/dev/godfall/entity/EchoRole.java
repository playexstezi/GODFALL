package dev.godfall.entity;

public enum EchoRole {
    GUIDE("Проводник", "Я помню это место. Здесь что-то изменилось."),
    WARRIOR("Воин", "Держись рядом. На этот раз я предупрежу тебя заранее."),
    WANDERER("Скиталец", "Здесь нас было гораздо больше."),
    TRAITOR("Предатель", "Я знаю, как всё закончится. Но не уверен, что тебе понравится.");

    private final String displayName;
    private final String greeting;

    EchoRole(String displayName, String greeting) {
        this.displayName = displayName;
        this.greeting = greeting;
    }

    public String displayName() {
        return displayName;
    }

    public String greeting() {
        return greeting;
    }

    public static EchoRole forDeathIndex(int index) {
        return values()[Math.floorMod(index, values().length)];
    }

    public static EchoRole fromOrdinal(int ordinal) {
        EchoRole[] roles = values();
        return ordinal >= 0 && ordinal < roles.length ? roles[ordinal] : GUIDE;
    }
}
