package jakojaannos.townbuilder.tileentity;

import jakojaannos.townbuilder.Network;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import jakojaannos.townbuilder.network.CreateTownBuilderCameraMessage;
import lombok.val;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
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
        val world = playerEntity.world;
        if (world.isRemote) {
            throw new IllegalStateException("TownBuilderTE#createMenu(...) called on remote world!");
        }

        val x = pos.getX() + 0.5;
        val y = pos.getY() + 0.5 + 5.0;
        val z = pos.getZ() + 0.5;
        val cameraEntity = new TownBuilderCameraEntity(world, x, y, z);
        world.addEntity(cameraEntity);
        Network.getServer().sendTo((ServerPlayerEntity) playerEntity,
                                   CreateTownBuilderCameraMessage.builder()
                                                                 .entityId(cameraEntity.getEntityId())
                                                                 .build());

        return new TownBuilderContainer(transactionId, playerInventory, cameraEntity);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.town_builder");
    }
}
