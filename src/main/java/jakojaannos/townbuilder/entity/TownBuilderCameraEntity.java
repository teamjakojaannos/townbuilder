package jakojaannos.townbuilder.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class TownBuilderCameraEntity extends Entity {
    public TownBuilderCameraEntity(World world) {
        super(ModEntityTypes.TOWN_BUILDER_CAMERA, world);
    }

    public TownBuilderCameraEntity(World world, double x, double y, double z, float pitch, float yaw) {
        this(world);
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
}
