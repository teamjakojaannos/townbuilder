package jakojaannos.townbuilder.entity;

import jakojaannos.townbuilder.Network;
import jakojaannos.townbuilder.network.SpawnTownBuilderCameraMessage;
import jakojaannos.townbuilder.tileentity.TownBuilderTileEntity;
import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TownBuilderCameraEntity extends Entity {
    private PlayerEntity owner;
    private TownBuilderTileEntity townTileEntity;
    private BlockPos origin;
    private float cameraHeight;
    private float cameraOffset;

    private CameraFacing facing;

    public TownBuilderCameraEntity(
            World world,
            PlayerEntity owner,
            TownBuilderTileEntity townTileEntity,
            BlockPos origin,
            CameraFacing facing,
            float cameraHeight,
            float cameraOffset
    ) {
        super(ModEntityTypes.TOWN_BUILDER_CAMERA, world);
        this.rotationPitch = 45.0f;
        this.owner = owner;
        this.townTileEntity = townTileEntity;
        this.origin = origin;
        this.cameraHeight = cameraHeight;
        this.cameraOffset = cameraOffset;
        this.facing = facing;
        facing.applyOffsets(this, origin, cameraOffset, cameraHeight);
    }

    public void rotateCCW() {
        facing = facing.rotateCCW();
        facing.applyOffsets(this, origin, cameraOffset, cameraHeight);
    }

    public void rotate() {
        facing = facing.rotate();
        facing.applyOffsets(this, origin, cameraOffset, cameraHeight);
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
        val spawnMessage = SpawnTownBuilderCameraMessage.builder()
                                                        .entityId(getEntityId())
                                                        .uuid(getUniqueID())
                                                        .owner(owner.getUniqueID())
                                                        .origin(origin)
                                                        .yaw(rotationYaw)
                                                        .offset(cameraOffset)
                                                        .height(cameraHeight)
                                                        .build();
        return Network.getServer().asPacket(spawnMessage);
    }

    public enum CameraFacing {
        SOUTH_WEST(45.0f + 90.0f * 0, 1.0f, -1.0f),
        NORTH_WEST(45.0f + 90.0f * 1, 1.0f, 1.0f),
        NORTH_EAST(45.0f + 90.0f * 2, -1.0f, 1.0f),
        SOUTH_EAST(45.0f + 90.0f * 3, -1.0f, -1.0f);

        private final float yaw;
        private final float xOffset;
        private final float zOffset;

        CameraFacing(float yaw, float xOffset, float zOffset) {
            this.yaw = yaw;
            this.xOffset = xOffset;
            this.zOffset = zOffset;
        }

        public static CameraFacing findFromYaw(float yaw) {
            if (yaw >= 270.0) {
                return SOUTH_EAST;
            } else if (yaw >= 180.0) {
                return NORTH_EAST;
            } else if (yaw >= 90.0f) {
                return NORTH_WEST;
            } else {
                return SOUTH_WEST;
            }
        }

        public void applyOffsets(Entity entity, BlockPos origin, float offset, float height) {
            entity.rotationYaw = yaw;
            entity.posX = origin.getX() + xOffset * offset;
            entity.posY = origin.getY() + height;
            entity.posZ = origin.getZ() + zOffset * offset;
        }

        public CameraFacing rotate() {
            return ordinal() == 3 ? SOUTH_WEST : values()[ordinal() + 1];
        }

        public CameraFacing rotateCCW() {
            return ordinal() == 0 ? SOUTH_EAST : values()[ordinal() - 1];
        }
    }
}
