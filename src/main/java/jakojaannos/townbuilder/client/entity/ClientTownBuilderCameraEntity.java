package jakojaannos.townbuilder.client.entity;

import jakojaannos.townbuilder.client.util.CameraMovementInput;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@Log4j2
@OnlyIn(Dist.CLIENT)
public class ClientTownBuilderCameraEntity extends TownBuilderCameraEntity {
    private boolean local;
    @Nullable private CameraMovementInput movementInput;

    public ClientTownBuilderCameraEntity(World world) {
        super(world);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        super.readSpawnData(additionalData);
        this.local = owner.getUniqueID().equals(Minecraft.getInstance().player.getUniqueID());
        if (local) {
            this.movementInput = new CameraMovementInput();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (movementInput != null && local) {
            updateMovement();
        }
    }

    private void updateMovement() {
        if (movementInput == null) {
            throw LOGGER.throwing(new IllegalStateException("MovementInput null while updating camera movement"));
        }

        movementInput.tick();

        if (movementInput.wantsMove()) {
            val x = movementInput.getInputHorizontal();
            val z = movementInput.getInputVertical();

            setMotion(x, 0.0, z);
            setPosition(posX + x, posY, posZ + z);
        } else {
            setMotion(0.0, 0.0, 0.0);
        }

        if (movementInput.isRotateLeft()) {
            setRotation(getRotatedYaw(Rotation.COUNTERCLOCKWISE_90), rotationPitch);
        }

        if (movementInput.isRotateRight()) {
            setRotation(getRotatedYaw(Rotation.CLOCKWISE_90), rotationPitch);
        }
    }
}
