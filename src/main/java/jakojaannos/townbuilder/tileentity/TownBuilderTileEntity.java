package jakojaannos.townbuilder.tileentity;

import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import lombok.val;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TownBuilderTileEntity extends TileEntity implements INamedContainerProvider {
    public static final float CAMERA_HEIGHT = 32.0f;
    public static final float CAMERA_OFFSET = CAMERA_HEIGHT / 2.0f;

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

        val facing = TownBuilderCameraEntity.CameraFacing.findFromYaw(playerEntity.rotationYaw);
        val cameraEntity = new TownBuilderCameraEntity(world,
                                                       playerEntity,
                                                       this,
                                                       pos,
                                                       facing,
                                                       CAMERA_HEIGHT,
                                                       CAMERA_OFFSET);
        world.addEntity(cameraEntity);

        return new TownBuilderContainer(transactionId, playerInventory, cameraEntity);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.town_builder");
    }
}
