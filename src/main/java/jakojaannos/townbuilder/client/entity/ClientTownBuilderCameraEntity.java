package jakojaannos.townbuilder.client.entity;

import jakojaannos.townbuilder.Network;
import jakojaannos.townbuilder.client.util.CameraMovementInput;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.network.messages.CameraMessages;
import jakojaannos.townbuilder.tileentity.TownBuilderTileEntity;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@Log4j2
@OnlyIn(Dist.CLIENT)
public class ClientTownBuilderCameraEntity extends TownBuilderCameraEntity {
    @Nullable private CameraMovementInput movementInput;

    public ClientTownBuilderCameraEntity(
            World world,
            PlayerEntity owner,
            TownBuilderTileEntity townTileEntity,
            BlockPos origin,
            CameraFacing facing,
            float cameraHeight,
            float cameraOffset
    ) {
        super(world, owner, townTileEntity, origin, facing, cameraHeight, cameraOffset);

        if (owner.getUniqueID().equals(Minecraft.getInstance().player.getUniqueID())) {
            this.movementInput = new CameraMovementInput();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (movementInput != null) {
            updateMovement();
        }
    }

    private void updateMovement() {
        if (movementInput == null) {
            throw LOGGER.throwing(new IllegalStateException("MovementInput null while updating camera movement"));
        }

        movementInput.tick();

        var rotated = false;
        if (movementInput.isRotateLeft()) {
            rotate();
            rotated = true;
        }

        if (movementInput.isRotateRight()) {
            rotateCCW();
            rotated = true;
        }

        if (rotated) {
            Network.getClient().sendToServer(CameraMessages.UpdateFacing.builder()
                                                                        .entityId(getEntityId())
                                                                        .facingIndex(facing.facingIndex())
                                                                        .build());
        }

        if (movementInput.wantsMove()) {
            val xRaw = movementInput.getInputHorizontal();
            val zRaw = movementInput.getInputVertical();

            val sin = Math.sin(Math.toRadians(MathHelper.wrapDegrees(-rotationYaw - 90.0f)));
            val cos = Math.cos(Math.toRadians(MathHelper.wrapDegrees(-rotationYaw - 90.0f)));
            val x = xRaw * sin + zRaw * cos;
            val z = xRaw * cos - zRaw * sin;

            origin = origin.add(x, 0.0, z);
            facing.applyOffsets(this, origin, cameraOffset, cameraHeight);
            Network.getClient().sendToServer(CameraMessages.UpdateOrigin.builder()
                                                                        .entityId(getEntityId())
                                                                        .pos(getPositionVec())
                                                                        .build());
        }

        // FIXME: Lie about server-approved location to prevent snapping
        func_213312_b(posX, posY, posZ);
    }
}
