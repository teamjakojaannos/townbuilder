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


            Item item = player.getHeldItem(Hand.MAIN_HAND).getItem();


            LOGGER.info("Player is holding {}", item.toString());


            int width = 1, height = 2, length = 3;

            if (item.equals(Items.COBBLESTONE)) {
                // some random test values
                width = 7;
                height = 3;
                length = 4;

            } else if (item.equals(Items.STONE)) {
                // make it a cube
                width = 4;
                height = 4;
                length = 4;

            } else if (item.equals(Items.OBSIDIAN)) {
                // 6x1x1
                width = 6;
                height = 1;
                length = 1;

            } else if (item.equals(Items.DIAMOND)) {
                // test what happens with 6x0x0
                width = 6;
                height = 0;
                length = 0;

            } else if (item.equals(Items.IRON_INGOT)) {
                // same as obsidian, but length instead of width
                width = 1;
                height = 1;
                length = 6;

            } else if (item.equals(Items.GOLD_INGOT)) {
                // try what happens if all are 0
                width = 0;
                height = 0;
                length = 0;

            } else if (item.equals(Items.EGG)) {
                // try with all 1
                width = 1;
                height = 1;
                length = 1;
            } else if (item.equals(Items.BOOK)) {
                // try with negative values to
                width = 7;
                height = -3;
                length = -4;
            }


            var tpl = getEdgeBlocks(blockPos, playerDir, width, height, length);

            world.setBlockState(tpl.getA(), Blocks.COBBLESTONE.getDefaultState());
            world.setBlockState(tpl.getB(), Blocks.STONE.getDefaultState());

        }


        return true;
    }

    /**
     * Returns two edge blocks of a cube. One is in bottom-left-frontside corner (next to MyTestBlock),
     * and the other one is in top-right-backside corner.
     * <p>
     * The resulting cube does not overlap the given MyTestBlock's position as the bottom-left-front block is
     * offset by 1 block from the given origin position.
     *
     * @param blockPos  position of the MyTestBlock
     * @param playerDir which direction the player is facing (opposite of the side the block was clicked on)
     * @param width     width of the cube, must be greater than zero
     * @param height    height of the cube, must be greater than zero
     * @param length    length of the cube, must be greater than zero
     * @return
     */
    private static Tuple<BlockPos, BlockPos> getEdgeBlocks(
            BlockPos blockPos,
            Direction playerDir,
            int width,
            int height,
            int length
    ) {
        // width, height and length have to be greater than 0


        BlockPos left = blockPos.offset(playerDir, 1);


        BlockPos right = blockPos.offset(playerDir, length)
                                 .offset(playerDir.rotateY(), width - 1)
                                 .add(0, height - 1, 0);

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
