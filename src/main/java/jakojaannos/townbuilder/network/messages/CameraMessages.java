package jakojaannos.townbuilder.network.messages;

import jakojaannos.townbuilder.client.entity.ClientTownBuilderCameraEntity;
import jakojaannos.townbuilder.client.gui.screen.inventory.TownBuilderScreen;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.network.IMessageHandler;
import jakojaannos.townbuilder.tileentity.TownBuilderTileEntity;
import lombok.Builder;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;


@Log4j2
public class CameraMessages {
    @Value
    @Log4j2
    @Builder(builderClassName = "Builder")
    public static class Spawn {
        int entityId;
        UUID uuid;
        UUID owner;
        BlockPos origin;
        float yaw;
        float offset;
        float height;

        public static class Handler implements IMessageHandler.Client<CameraMessages.Spawn> {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClientside(
                    CameraMessages.Spawn message,
                    Supplier<NetworkEvent.Context> contextSupplier
            ) {
                contextSupplier.get().enqueueWork(() -> {
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
                });
                contextSupplier.get().setPacketHandled(true);
            }
        }
    }

    @Value
    @Log4j2
    @Builder(builderClassName = "Builder")
    public static class UpdateOrigin {
        int entityId;
        Vec3d pos;

        public static class Handler implements IMessageHandler.Server<CameraMessages.UpdateOrigin> {
            @Override
            public void handleServerside(
                    UpdateOrigin message,
                    Supplier<NetworkEvent.Context> contextSupplier
            ) {
                contextSupplier.get().enqueueWork(() -> {
                    Optional<TownBuilderCameraEntity> camera = getCameraFromEntityId(message.entityId,
                                                                                     contextSupplier.get());
                    camera.ifPresent(cam -> cam.updateOrigin(message.pos));
                });
                contextSupplier.get().setPacketHandled(true);
            }
        }
    }

    @Value
    @Log4j2
    @Builder(builderClassName = "Builder")
    public static class UpdateFacing {
        int entityId;
        int facingIndex;

        public static class Handler implements IMessageHandler.Server<CameraMessages.UpdateFacing> {
            @Override
            public void handleServerside(
                    UpdateFacing message,
                    Supplier<NetworkEvent.Context> contextSupplier
            ) {
                contextSupplier.get().enqueueWork(() -> {
                    Optional<TownBuilderCameraEntity> camera = getCameraFromEntityId(message.entityId,
                                                                                     contextSupplier.get());
                    camera.ifPresent(cam -> cam.updateFacing(TownBuilderCameraEntity.CameraFacing.fromIndex(message.facingIndex)));
                });
                contextSupplier.get().setPacketHandled(true);
            }
        }
    }

    private static <TCamera extends TownBuilderCameraEntity> Optional<TCamera> getCameraFromEntityId(
            int entityId,
            NetworkEvent.Context context
    ) {
        val sender = context.getSender();
        if (sender == null) {
            LOGGER.error("Could not determine sender from context! Skipping camera origin update.");
            return Optional.empty();
        }
        val world = sender.world;
        val targetEntity = world.getEntityByID(entityId);

        if (!(targetEntity instanceof TownBuilderCameraEntity)) {
            LOGGER.error("Received origin update for entity ID which does not represent a valid camera entity");
            return Optional.empty();
        }

        return Optional.of((TCamera) targetEntity);
    }
}
