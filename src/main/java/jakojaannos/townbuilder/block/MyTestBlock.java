package jakojaannos.townbuilder.block;

import lombok.extern.log4j.Log4j2;
import lombok.var;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

@Log4j2
public class MyTestBlock extends Block {

    public MyTestBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
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
        }


        if (Direction.Plane.HORIZONTAL.test(rayTraceResult.getFace())) {
            Direction playerDir = rayTraceResult.getFace().getOpposite();

            Item item = player.getActiveItemStack().getItem();
            int width = 1, height = 2, length = 3;

            if (item.equals(Items.COBBLESTONE)) {

            } else if (item.equals(Items.STONE)) {

            } else if (item.equals(Items.OBSIDIAN)) {

            } else if (item.equals(Items.DIAMOND)) {

            } else if (item.equals(Items.IRON_INGOT)) {

            } else if (item.equals(Items.GOLD_INGOT)) {

            } else if (item.equals(Items.EGG)) {

            }


            var tpl = getEdgeBlocks(blockPos, playerDir, 4, 2, 7);

            world.setBlockState(tpl.getA(), Blocks.COBBLESTONE.getDefaultState());
            world.setBlockState(tpl.getB(), Blocks.STONE.getDefaultState());

        }


        return true;
    }

    private static Tuple<BlockPos, BlockPos> getEdgeBlocks(
            BlockPos blockPos,
            Direction playerDir,
            int width,
            int height,
            int length
    ) {
        BlockPos left = blockPos.offset(playerDir, 1);


        BlockPos right = blockPos.offset(playerDir, length)
                                 .offset(playerDir.rotateY(), width)
                                 .add(0, height, 0);

        return new Tuple<>(left, right);
    }


    // TODO: finish later
    private static Tuple<BlockPos, BlockPos> getEdgeBlocksCentered(
            BlockPos blockPos,
            Direction playerDir,
            int width,
            int height,
            int length
    ) {
        int fx = playerDir.getXOffset();
        int fz = playerDir.getZOffset();

        // right
        int rx = playerDir.rotateY().getXOffset() * 2;
        int rz = playerDir.rotateY().getZOffset() * 2;

        // left
        int lx = playerDir.rotateYCCW().getXOffset() * 2;
        int lz = playerDir.rotateYCCW().getZOffset() * 2;

        BlockPos left = blockPos.add(fx + lx, 0, fz + lz);
        BlockPos right = blockPos.add(fx * 5 + rx, 0, fz * 5 + rz);

        return new Tuple<>(left, right);
    }


    @Override
    public boolean ticksRandomly(BlockState p_149653_1_) {
        return false;
    }


}
