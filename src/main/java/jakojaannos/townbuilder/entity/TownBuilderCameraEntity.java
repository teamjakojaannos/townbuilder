package jakojaannos.townbuilder.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;

public class TownBuilderCameraEntity extends Entity {
    public TownBuilderCameraEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    public TownBuilderCameraEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.TOWN_BUILDER_CAMERA, world);
        setPosition(x, y, z);
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
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
        return new SSpawnObjectPacket(this);
    }
}
