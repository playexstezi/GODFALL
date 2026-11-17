package dev.godfall.item;

import dev.godfall.data.GodfallPlayerData;
import dev.godfall.data.GodfallSavedData;
import dev.godfall.network.NetworkHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class MemoryFragmentItem extends Item {
    public MemoryFragmentItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            GodfallSavedData savedData = GodfallSavedData.get(serverPlayer.getServer());
            GodfallPlayerData data = savedData.getOrCreate(serverPlayer.getUUID());

            data.addMemoryFragment();
            boolean unlockedRift = data.getMemoryFragments() >= 1
                    && data.unlockAbility("godfall:rift");
            savedData.setDirty();

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            NetworkHandler.sendDialogue(
                    serverPlayer,
                    "Воспоминание вернулось. На мгновение ты вспомнил, как пространство подчинялось твоей воле."
            );

            if (unlockedRift) {
                NetworkHandler.sendDialogue(
                        serverPlayer,
                        "Способность пробуждена: «Разлом». Попробуй команду /godfall rift."
                );
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
