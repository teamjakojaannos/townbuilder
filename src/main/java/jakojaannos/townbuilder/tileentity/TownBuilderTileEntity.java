package jakojaannos.townbuilder.tileentity;

import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TownBuilderTileEntity extends TileEntity implements INamedContainerProvider {
    public TownBuilderTileEntity() {
        super(ModTileEntityTypes.TOWN_BUILDER);
    }

    @Nullable
    @Override
    public TownBuilderContainer createMenu(
        int transactionId,
        PlayerInventory playerInventory,
        PlayerEntity playerEntity
    ) {
        return new TownBuilderContainer(transactionId, playerInventory);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.town_builder");
    }
}
