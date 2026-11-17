package dev.godfall.block;

import dev.godfall.network.NetworkHandler;
import dev.godfall.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public final class MemoryAltarBlock extends Block {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public MemoryAltarBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ACTIVATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(ACTIVATED)) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        level.setBlock(pos, state.setValue(ACTIVATED, true), 3);

        ItemStack fragment = new ItemStack(ModItems.MEMORY_FRAGMENT.get());
        if (!serverPlayer.getInventory().add(fragment)) {
            serverPlayer.drop(fragment, false);
        }

        NetworkHandler.sendDialogue(serverPlayer,
                "Ты коснулся алтаря. Камень узнал тебя раньше, чем ты узнал самого себя.");
        return InteractionResult.CONSUME;
    }
}
