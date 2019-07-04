package jakojaannos.townbuilder.entity;

import jakojaannos.townbuilder.tileentity.TownBuilderTileEntity;
import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class TownBuilderCameraEntity extends Entity implements IEntityAdditionalSpawnData {
    protected PlayerEntity owner;
    protected TownBuilderTileEntity townTileEntity;

    protected TownBuilderCameraEntity(World world) {
        super(ModEntityTypes.TOWN_BUILDER_CAMERA, world);
    }

    public TownBuilderCameraEntity(
            World world,
            PlayerEntity owner,
            TownBuilderTileEntity townTileEntity,
            double x,
            double y,
            double z,
            float pitch,
            float yaw
    ) {
        this(world);
        this.owner = owner;
        this.townTileEntity = townTileEntity;

        setPosition(x, y, z);
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;

        setRotation(yaw, pitch);
        prevRotationPitch = pitch;
        prevRotationYaw = yaw;
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeUniqueId(owner.getUniqueID());
        buffer.writeBlockPos(townTileEntity.getPos());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        val ownerUUID = additionalData.readUniqueId();
        val tileEntityPos = additionalData.readBlockPos();

        val world = getEntityWorld();
        val tileEntity = world.getTileEntity(tileEntityPos);
        if (tileEntity instanceof TownBuilderTileEntity) {
            this.townTileEntity = (TownBuilderTileEntity) tileEntity;
        } else {
            LOGGER.error("Could not find suitable TileEntity for builder camera at coordinates specified in spawn data!");
        }

        val owner = world.getPlayerByUuid(ownerUUID);
        if (owner != null) {
            this.owner = owner;
        } else {
            LOGGER.error("Could not determine owning player by UUID specified by spawn data!");
        }
    }
}
