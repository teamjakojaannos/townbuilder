package jakojaannos.townbuilder.inventory.container;

import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;

public class TownBuilderContainer extends Container {
    private final PlayerInventory playerInventory;
    @Nullable private TownBuilderCameraEntity cameraEntity;

    public TownBuilderContainer(
            int transactionId,
            PlayerInventory playerInventory
    ) {
        this(transactionId, playerInventory, null);
    }

    public TownBuilderContainer(
            int transactionId,
            PlayerInventory playerInventory,
            @Nullable TownBuilderCameraEntity cameraEntity
    ) {
        super(ModContainerTypes.TOWN_BUILDER, transactionId);
        this.playerInventory = playerInventory;
        this.cameraEntity = cameraEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerEntity) {
        super.onContainerClosed(playerEntity);
        if (cameraEntity != null) {
            cameraEntity.remove();
            cameraEntity = null;
        }
    }
}
