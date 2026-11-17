package dev.godfall.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.godfall.ability.RiftAbility;
import dev.godfall.data.GodfallPlayerData;
import dev.godfall.data.GodfallSavedData;
import dev.godfall.registry.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class GodfallCommands {
    private GodfallCommands() {
    }

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
                                    + " | Способности: " + data.getUnlockedAbilities().size()
                    ), false);
                    return 1;
                }))
                .then(Commands.literal("memories").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    GodfallPlayerData data = GodfallSavedData.get(player.getServer())
                            .getOrCreate(player.getUUID());

                    context.getSource().sendSuccess(() -> Component.literal(
                            "Восстановлено фрагментов памяти: " + data.getMemoryFragments()
                                    + " | Стадия: " + data.getStage().displayName()
                    ), false);
                    return 1;
                }))
                .then(Commands.literal("rift").executes(context ->
                        RiftAbility.cast(context.getSource().getPlayerOrException())
                ))
                .then(Commands.literal("give_fragment")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ItemStack fragment = new ItemStack(ModItems.MEMORY_FRAGMENT.get());
                            if (!player.getInventory().add(fragment)) {
                                player.drop(fragment, false);
                            }
                            context.getSource().sendSuccess(
                                    () -> Component.literal("Фрагмент памяти выдан."),
                                    false
                            );
                            return 1;
                        }))
        );
    }
}
