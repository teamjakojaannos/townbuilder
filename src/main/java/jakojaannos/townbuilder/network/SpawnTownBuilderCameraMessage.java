package jakojaannos.townbuilder.network;

import jakojaannos.townbuilder.client.entity.ClientTownBuilderCameraEntity;
import jakojaannos.townbuilder.client.gui.screen.inventory.TownBuilderScreen;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.tileentity.TownBuilderTileEntity;
import lombok.Builder;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

@Value
@Log4j2
@Builder(builderClassName = "Builder")
public class SpawnTownBuilderCameraMessage {
    int entityId;
    UUID uuid;
    UUID owner;
    BlockPos origin;
    float yaw;
    float offset;
    float height;

    public static void handleClientside(
            SpawnTownBuilderCameraMessage message,
            Supplier<NetworkEvent.Context> context
    ) {
        context.get().enqueueWork(() -> handleClientside(message));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientside(SpawnTownBuilderCameraMessage message) {
        val world = (ClientWorld) Minecraft.getInstance().player.world;
        val tileEntity = world.getTileEntity(message.origin);
        if (!(tileEntity instanceof TownBuilderTileEntity)) {
            LOGGER.error("Could not find suitable TileEntity for builder camera at origin coordinates specified in spawn data!");
            return;
        }

        val owner = world.getPlayerByUuid(message.owner);
        if (owner == null) {
            LOGGER.error("Could not determine owning player by UUID specified in spawn data!");
            return;
        }

        val facing = TownBuilderCameraEntity.CameraFacing.findFromYaw(owner.rotationYaw);
        val entity = new ClientTownBuilderCameraEntity(world,
                                                       owner,
                                                       (TownBuilderTileEntity) tileEntity,
                                                       message.origin,
                                                       facing,
                                                       message.height,
                                                       message.offset);
        entity.func_213312_b(entity.posX, entity.posY, entity.posZ);
        entity.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
        entity.setRotationYawHead(entity.rotationYaw);
        entity.setRenderYawOffset(entity.rotationYaw);

        entity.setEntityId(message.entityId);
        entity.setUniqueId(message.uuid);
        world.addEntity(message.entityId, entity);

        TownBuilderScreen.receiveCameraEntity(entity);
    }
}
