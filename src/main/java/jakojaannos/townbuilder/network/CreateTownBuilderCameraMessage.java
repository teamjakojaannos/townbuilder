package jakojaannos.townbuilder.network;

import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CreateTownBuilderCameraMessage {
    private final int entityId;

    public CreateTownBuilderCameraMessage(Entity cameraEntity) {
        this.entityId = cameraEntity.getEntityId();
    }

    public static void encode(CreateTownBuilderCameraMessage message, PacketBuffer packetBuffer) {

    }

    public static CreateTownBuilderCameraMessage decode(PacketBuffer packetBuffer) {

        return null;
    }

    public static void handle(
        CreateTownBuilderCameraMessage message,
        Supplier<NetworkEvent.Context> contextSupplier
    ) {
        val ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {

        });
        ctx.setPacketHandled(true);
    }
}
