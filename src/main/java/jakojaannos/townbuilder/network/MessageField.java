package jakojaannos.townbuilder.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketBuffer;

import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "of")
public class MessageField<T, TMessage, TBuilder> {
    private final BiConsumer<PacketBuffer, T> encoder;
    private final Function<PacketBuffer, T> decoder;
    private final Function<TMessage, T> valueGetter;
    private final BiConsumer<TBuilder, T> builderSetter;

    void encode(TMessage message, PacketBuffer buffer) {
        encoder.accept(buffer, valueGetter.apply(message));
    }

    void addToBuilder(TBuilder builder, PacketBuffer buffer) {
        builderSetter.accept(builder, decoder.apply(buffer));
    }

    public static <TMessage, TBuilder> MessageField<Integer, TMessage, TBuilder> ofInteger(
            Function<TMessage, Integer> valueGetter,
            BiConsumer<TBuilder, Integer> builderSetter
    ) {
        return MessageField.of(PacketBuffer::writeInt,
                               PacketBuffer::readInt,
                               valueGetter,
                               builderSetter);
    }
}
