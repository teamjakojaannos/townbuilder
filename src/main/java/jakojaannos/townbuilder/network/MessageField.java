package jakojaannos.townbuilder.network;

import jakojaannos.townbuilder.util.PacketBufferHelper;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Field on networked message, providing utilities for easy serialization. Most built-in variants are provided via this
 * class as named static constructors (methods prefixed with "<code>of</code>").
 * <p>
 * Provided constructors all follow pattern where required parameters are reference to a getter for field value and
 * reference to a method for setting the value in the message builder. Former is for message encoding and latter for
 * decoding. Further, there exists raw constructor {@link #of(BiConsumer, Function, Function, BiConsumer)} which is
 * meant for defining specialized constructors. For usage example, see one of the built-in specialized constructors.
 * e.g. {@link #ofInteger(Function, BiConsumer) ofInteger} or {@link #ofVec3d(Function, BiConsumer) ofVec3d}
 * <p>
 * API is designed to work with classes annotated with {@link lombok.Builder} and {@link lombok.Value}. If both of these
 * are fulfilled, marking message fields for serialization is done by simply calling one of the <code>of</code>
 * -methods.
 *
 * @param <T>        Type of the stored value
 * @param <TMessage> Type of the message providing values for serialization
 * @param <TBuilder> Type of the message instance builder used during deserialization
 */
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

    public static <TMessage, TBuilder> MessageField<Float, TMessage, TBuilder> ofFloat(
            Function<TMessage, Float> valueGetter,
            BiConsumer<TBuilder, Float> builderSetter
    ) {
        return MessageField.of(PacketBuffer::writeFloat,
                               PacketBuffer::readFloat,
                               valueGetter,
                               builderSetter);
    }

    public static <TMessage, TBuilder> MessageField<UUID, TMessage, TBuilder> ofUUID(
            Function<TMessage, UUID> valueGetter,
            BiConsumer<TBuilder, UUID> builderSetter
    ) {
        return MessageField.of(PacketBuffer::writeUniqueId,
                               PacketBuffer::readUniqueId,
                               valueGetter,
                               builderSetter);
    }

    public static <TMessage, TBuilder> MessageField<BlockPos, TMessage, TBuilder> ofBlockPos(
            Function<TMessage, BlockPos> valueGetter,
            BiConsumer<TBuilder, BlockPos> builderSetter
    ) {
        return MessageField.of(PacketBuffer::writeBlockPos,
                               PacketBuffer::readBlockPos,
                               valueGetter,
                               builderSetter);
    }

    public static <TMessage, TBuilder> MessageField<Vec3d, TMessage, TBuilder> ofVec3d(
            Function<TMessage, Vec3d> valueGetter,
            BiConsumer<TBuilder, Vec3d> builderSetter
    ) {
        return MessageField.of(PacketBufferHelper::encodeVec3d,
                               PacketBufferHelper::decodeVec3d,
                               valueGetter,
                               builderSetter);
    }
}
