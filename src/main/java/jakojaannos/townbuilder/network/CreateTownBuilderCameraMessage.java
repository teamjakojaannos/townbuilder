package jakojaannos.townbuilder.network;

import jakojaannos.townbuilder.client.gui.screen.inventory.TownBuilderScreen;
import lombok.Builder;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
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
            TownBuilderScreen.receiveCameraEntityId(message.getEntityId());
        });
        context.get().setPacketHandled(true);
    }
}
