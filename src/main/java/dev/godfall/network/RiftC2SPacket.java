package dev.godfall.network;

import dev.godfall.ability.RiftAbility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RiftC2SPacket() {
    public static void encode(RiftC2SPacket packet, FriendlyByteBuf buffer) {
        // No client-provided position or ability data is trusted by the server.
    }

    public static RiftC2SPacket decode(FriendlyByteBuf buffer) {
        return new RiftC2SPacket();
    }

    public static void handle(RiftC2SPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                RiftAbility.cast(player);
            }
        });
        context.setPacketHandled(true);
    }
}
