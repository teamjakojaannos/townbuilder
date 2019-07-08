package jakojaannos.townbuilder.client.entity;

import jakojaannos.townbuilder.client.util.CameraMovementInput;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.tileentity.TownBuilderTileEntity;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
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

        if (movementInput.isRotateLeft()) {
            rotate();
        }

        if (movementInput.isRotateRight()) {
            rotateCCW();
        }

        if (movementInput.wantsMove()) {
            val xRaw = movementInput.getInputHorizontal();
            val zRaw = movementInput.getInputVertical();

            val cos = Math.cos(Math.toRadians(rotationYaw));
            val sin = Math.sin(Math.toRadians(rotationYaw));

            // TODO: Something fishy with these
            val x = xRaw * cos + zRaw * sin;
            val z = xRaw * sin - zRaw * cos;

            setMotion(x, 0.0, z);
            setPosition(posX + x, posY, posZ + z);
        } else {
            setMotion(0.0, 0.0, 0.0);
        }
    }
}
