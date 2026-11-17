package dev.godfall.command;
import com.mojang.brigadier.CommandDispatcher;
import dev.godfall.data.GodfallPlayerData;
import dev.godfall.data.GodfallSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
public final class GodfallCommands {
    private GodfallCommands() {}
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("godfall")
                .then(Commands.literal("status").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    GodfallPlayerData data = GodfallSavedData.get(player.getServer())
                            .getOrCreate(player.getUUID());
                    context.getSource().sendSuccess(() -> Component.literal(
                            "Состояние Создателя: " + data.getStage().displayName()
                                    + " | Фрагменты памяти: " + data.getMemoryFragments()
                                    + " | Зафиксированные смерти: " + data.getDeathCount()
                    ), false);
                    return 1;
                }))
                .then(Commands.literal("memories").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    GodfallPlayerData data = GodfallSavedData.get(player.getServer())
                            .getOrCreate(player.getUUID());
                    context.getSource().sendSuccess(() -> Component.literal(
                            "Восстановлено фрагментов памяти: " + data.getMemoryFragments()
                    ), false);
                    return 1;
                }))
        );
    }
}
