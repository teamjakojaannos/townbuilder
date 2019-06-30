package jakojaannos.townbuilder.block;

import jakojaannos.townbuilder.tileentity.TownBuilderTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TownBuilderBlock extends Block {
    public TownBuilderBlock(Properties builder) {
        super(builder);
    }

    @Override
    public boolean onBlockActivated(
        BlockState blockState,
        World world,
        BlockPos blockPos,
        PlayerEntity player,
        Hand hand,
        BlockRayTraceResult rayTraceResult
    ) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof TownBuilderTileEntity) {
                player.openContainer((TownBuilderTileEntity) tileEntity);
            }

            return true;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TownBuilderTileEntity();
    }
}
