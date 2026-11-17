package dev.godfall.network;
import dev.godfall.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
public record DialogueS2CPacket(String message) {
    public static void encode(DialogueS2CPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.message(), 32767);
    }
    public static DialogueS2CPacket decode(FriendlyByteBuf buffer) {
        return new DialogueS2CPacket(buffer.readUtf(32767));
    }
    public static void handle(DialogueS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT, () -> () -> ClientPacketHandler.handleDialogue(packet.message())
        ));
        context.setPacketHandled(true);
    }
}
