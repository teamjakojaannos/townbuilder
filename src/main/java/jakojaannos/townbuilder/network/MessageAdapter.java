package jakojaannos.townbuilder.network;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Log4j2
public class MessageAdapter<TMessage, TBuilder> {
    @Getter private final Class<TMessage> messageClass;

    private final Supplier<TBuilder> builderFactory;
    private final Function<TBuilder, TMessage> buildCaller;
    private final List<MessageField<?, TMessage, TBuilder>> fields = new ArrayList<>();
    private BiConsumer<TMessage, Supplier<NetworkEvent.Context>> handler = (msg, ctxSupplier) -> {
        LOGGER.warn("MessageAdapter default handler called! This means that message type is missing " +
                            "a handler or message was sent to wrong direction. Message is \"{}\" with direction {}",
                    msg.getClass().getSimpleName(),
                    ctxSupplier.get().getDirection());
        ctxSupplier.get().setPacketHandled(true);
    };
    @Nullable private BiConsumer<TMessage, Supplier<NetworkEvent.Context>> clientHandler;
    @Nullable private BiConsumer<TMessage, Supplier<NetworkEvent.Context>> serverHandler;

    /**
     * Creates a new message adapter.
     *
     * @param messageClass   message class
     * @param builderFactory factory method used for producing message builders
     * @param buildCaller    the <code>.build()</code> method on the builder used to produce the final message
     */
    public MessageAdapter(
            Class<TMessage> messageClass,
            Supplier<TBuilder> builderFactory,
            Function<TBuilder, TMessage> buildCaller
    ) {
        this.messageClass = messageClass;
        this.builderFactory = builderFactory;
        this.buildCaller = buildCaller;
    }

    /**
     * <p>
     * Relatively safe shorthand form of {@link MessageAdapter#MessageAdapter(Class, Supplier, Function)}. Tries to
     * guess the <code>messageClass</code> from builder output. Works only if {@link TMessage TMessage.class} matches
     * exactly with {@link #buildCaller} output's class. Otherwise, use full constructor and pass
     * <code>messageClass</code> explicitly.
     * </p>
     * <p>
     * e.g. {@code buildCaller.apply(builderFactory.get()).getClass()} must be assignable to {@code Class<TMessage>}
     * </p>
     *
     * @param builderFactory factory method used for producing message builders
     * @param buildCaller    The <code>.build()</code> method on the builder used to produce the final message
     */
    public MessageAdapter(Supplier<TBuilder> builderFactory, Function<TBuilder, TMessage> buildCaller) {
        this(guessMessageClass(builderFactory, buildCaller), builderFactory, buildCaller);
    }

    private static <TMessage, TBuilder> Class<TMessage> guessMessageClass(
            Supplier<TBuilder> builderFactory,
            Function<TBuilder, TMessage> buildCaller
    ) {
        // This is relatively safe, albeit hacky. Using full constructor with messageClass as parameter is more safe,
        // but this should work as long as one does not try anything stupid.
        // noinspection unchecked
        return (Class<TMessage>) buildCaller.apply(builderFactory.get()).getClass();
    }

    public <TField> MessageAdapter<TMessage, TBuilder> withField(
            MessageField<TField, TMessage, TBuilder> field
    ) {
        fields.add(field);
        return this;
    }

    public MessageAdapter<TMessage, TBuilder> withClientsideHandler(
            BiConsumer<TMessage, Supplier<NetworkEvent.Context>> handler
    ) {
        this.clientHandler = handler;
        return this;
    }

    public MessageAdapter<TMessage, TBuilder> withServersideHandler(
            BiConsumer<TMessage, Supplier<NetworkEvent.Context>> handler
    ) {
        this.serverHandler = handler;
        return this;
    }

    public void setClientside() {
        if (clientHandler != null) {
            handler = clientHandler;
        }
    }

    public void setServerside() {
        if (serverHandler != null) {
            handler = serverHandler;
        }
    }

    public void register(int discriminator, SimpleChannel channel) {
        channel.registerMessage(discriminator, messageClass, this::encode, this::decode, this::handle);
    }

    private void encode(TMessage message, PacketBuffer buffer) {
        fields.forEach(f -> f.encode(message, buffer));
    }

    private TMessage decode(PacketBuffer buffer) {
        val builder = builderFactory.get();
        fields.forEach(f -> f.addToBuilder(builder, buffer));

        return buildCaller.apply(builder);
    }

    private void handle(TMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        handler.accept(message, contextSupplier);
    }
}
