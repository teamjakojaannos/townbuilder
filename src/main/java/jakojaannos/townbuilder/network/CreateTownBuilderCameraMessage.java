package jakojaannos.townbuilder.network;

import lombok.Builder;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@Value
@Log4j2
@Builder(builderClassName = "Builder")
public class CreateTownBuilderCameraMessage {
    int entityId;

    public static void handle(
            CreateTownBuilderCameraMessage message,
            Supplier<NetworkEvent.Context> context
    ) {
        context.get().enqueueWork(() -> {
            val player = Minecraft.getInstance().player;
            LOGGER.warn("CALLED! ============================================================");
            LOGGER.warn("local player: {}", player.getGameProfile().getName());
            LOGGER.warn("received entityId: {}", message.entityId);
        });
        context.get().setPacketHandled(true);
    }
}
