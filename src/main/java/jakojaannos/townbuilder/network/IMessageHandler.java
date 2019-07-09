package jakojaannos.townbuilder.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMessageHandler<TMessage> {
    void handleServerside(TMessage message, Supplier<NetworkEvent.Context> contextSupplier);

    interface Server<TMessage> extends IMessageHandler<TMessage> {
        @Override
        default void handleClientside(TMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            contextSupplier.get().setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    void handleClientside(TMessage message, Supplier<NetworkEvent.Context> contextSupplier);

    interface Client<TMessage> extends IMessageHandler<TMessage> {
        @Override
        @OnlyIn(Dist.CLIENT)
        default void handleServerside(TMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            contextSupplier.get().setPacketHandled(true);
        }
    }
}
