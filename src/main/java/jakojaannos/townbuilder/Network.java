package jakojaannos.townbuilder;

import jakojaannos.townbuilder.network.CreateTownBuilderCameraMessage;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class for handling communication between client and the server.
 */
public class Network {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(TownBuilder.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    private static Map<Class, MessageEntry> entries = new HashMap<>();

    private static void createMessages() {
        addEntry(CreateTownBuilderCameraMessage.class,
                 CreateTownBuilderCameraMessage::encode,
                 CreateTownBuilderCameraMessage::decode);
    }

    public static ModServerNetworkManager getServer() {
        return ModServerNetworkManager.getInstance(CHANNEL_INSTANCE);
    }

    // TODO: Move these to client/server managers
    // TODO: This does not work. Make wrapper handler which points to a MessageHandler instance, which contains
    //       the actual handler mapping
    public static MessageHandler createServerHandler() {
        return () -> {

        };
    }

    @OnlyIn(Dist.CLIENT)
    public static MessageHandler createClientHandler() {
        return () -> {
            setHandler(CreateTownBuilderCameraMessage.class, CreateTownBuilderCameraMessage::handle);
        };
    }

    private static <TMessage> void setHandler(
        Class<? extends TMessage> messageClass,
        BiConsumer<TMessage, Supplier<NetworkEvent.Context>> handler
    ) {

    }

    public static void registerMessages(MessageHandler handler) {
        createMessages();
        handler.apply();

        // noinspection unchecked
        entries.forEach((key, value) -> CHANNEL_INSTANCE.registerMessage(value.discriminator,
                                                                         key,
                                                                         value.encoder,
                                                                         value.decoder,
                                                                         value.messageConsumer));
    }

    private static <TMessage> void addEntry(
        Class<TMessage> messageClass,
        BiConsumer<TMessage, PacketBuffer> encoder,
        Function<PacketBuffer, TMessage> decoder
    ) {
        entries.put(messageClass, new MessageEntry<>(entries.size(), encoder, decoder));
    }

    @RequiredArgsConstructor
    public static class MessageEntry<TMessage> {
        private final int discriminator;
        private final BiConsumer<TMessage, PacketBuffer> encoder;
        private final Function<PacketBuffer, TMessage> decoder;
        private BiConsumer<TMessage, Supplier<NetworkEvent.Context>> messageConsumer = (a, b) -> {};
    }

    public interface MessageHandler {
        void apply();
    }
}
