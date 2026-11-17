package dev.godfall.dialogue;

import dev.godfall.entity.EchoRole;
import net.minecraft.util.RandomSource;

import java.util.List;

public final class EchoDialogue {
    private static final List<String> GUIDE = List.of(
            "Я помню это место. Здесь что-то спрятано.",
            "Не торопись. Некоторые двери открываются не силой.",
            "Я уже проходил здесь. Тогда мы не заметили главное.",
            "Если услышишь шаги за спиной, не оборачивайся сразу."
    );
    private static final List<String> WARRIOR = List.of(
            "Слева! В следующий раз не жди удара.",
            "Мы выживали и в худших местах. Держи оружие наготове.",
            "Не трать силу на каждого встречного. Сохрани её для настоящей угрозы.",
            "Я помню эту битву. Мы допустили одну ошибку. Не повторяй её."
    );
    private static final List<String> WANDERER = List.of(
            "Здесь нас было гораздо больше.",
            "Почему ты снова выбрал этот путь?",
            "Иногда мне кажется, что мир узнаёт нас раньше, чем мы его.",
            "Я помню не место. Я помню, что здесь уже прощался с тобой."
    );
    private static final List<String> TRAITOR = List.of(
            "Я знаю, как победить его. Вопрос в том, поверишь ли ты мне.",
            "Не слушай остальных. Они помнят только удобную часть.",
            "Ты уверен, что хочешь вернуть каждое воспоминание?",
            "Я бы рассказал правду. Но тогда игра закончится слишком рано."
    );

    private static final List<String> DEEPER_MEMORY = List.of(
            "Это воспоминание настоящее. Впервые за долгое время.",
            "Ты начинаешь вспоминать. Не все из нас этому рады.",
            "Когда память вернётся полностью, тебе придётся решить, кем ты был.",
            "Не путай возвращение силы с возвращением правоты."
    );

    private EchoDialogue() {
    }

    public static String line(EchoRole role, int memoryFragments, RandomSource random) {
        if (memoryFragments >= 3 && random.nextInt(4) == 0) {
            return DEEPER_MEMORY.get(random.nextInt(DEEPER_MEMORY.size()));
        }

        List<String> lines = switch (role) {
            case GUIDE -> GUIDE;
            case WARRIOR -> WARRIOR;
            case WANDERER -> WANDERER;
            case TRAITOR -> TRAITOR;
        };
        return lines.get(random.nextInt(lines.size()));
    }
}
