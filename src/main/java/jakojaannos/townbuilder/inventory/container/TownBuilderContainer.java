package jakojaannos.townbuilder.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class TownBuilderContainer extends Container {
    public TownBuilderContainer(int transactionId, PlayerInventory playerInventory) {
        super(ModContainerTypes.TOWN_BUILDER, transactionId);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
