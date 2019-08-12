package jakojaannos.townbuilder;

import jakojaannos.townbuilder.block.EMirror;
import jakojaannos.townbuilder.block.ERotation;
import jakojaannos.townbuilder.block.MyTestBlock;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Bootstrap;

@Log4j2
public class TestLauncher {

    public static void main(String[] args) {
        Bootstrap.register();

        LOGGER.info("Launching makeshiFt Junit");

        LOGGER.info("===== Test #1: basic cases (last 2 should fail) =====");
        test(new BlockPos.MutableBlockPos(4, 5, 6), EMirror.NONE, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(4, 5, 6));

        test(new BlockPos.MutableBlockPos(0, 0, 0), EMirror.NONE, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(0, 0, 0));

        test(new BlockPos.MutableBlockPos(-5, 5, 0), EMirror.NONE, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(-5, 5, 0));

        test(new BlockPos.MutableBlockPos(1, 2, 3), EMirror.NONE, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(4, 5, 6));

        test(new BlockPos.MutableBlockPos(9, 9, 9), EMirror.NONE, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(9, 9, 6));


        LOGGER.info("===== Test #2: moving =====");
        test(new BlockPos.MutableBlockPos(6, 3, 9), EMirror.NONE, ERotation.NONE,
             new BlockPos.MutableBlockPos(0, 2, 4),
             new BlockPos.MutableBlockPos(6, 5, 13));

        test(new BlockPos.MutableBlockPos(5, 0, 0), EMirror.NONE, ERotation.NONE,
             new BlockPos.MutableBlockPos(0, 0, 0),
             new BlockPos.MutableBlockPos(5, 0, 0));

        test(new BlockPos.MutableBlockPos(2, 3, 4), EMirror.NONE, ERotation.NONE,
             new BlockPos.MutableBlockPos(-4, 7, -4),
             new BlockPos.MutableBlockPos(-2, 10, 0));

        test(new BlockPos.MutableBlockPos(-1, -2, -3), EMirror.NONE, ERotation.NONE,
             new BlockPos.MutableBlockPos(-2, -1, -4),
             new BlockPos.MutableBlockPos(-3, -3, -7));


        LOGGER.info("===== Test #3.1: rotation =====");
        test(new BlockPos.MutableBlockPos(1, 0, 1), EMirror.NONE, ERotation.CLOCKWISE_90, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(1, 0, -1));

        test(new BlockPos.MutableBlockPos(1, 0, 1), EMirror.NONE, ERotation.CLOCKWISE_180, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(-1, 0, -1));

        test(new BlockPos.MutableBlockPos(1, 8, 1), EMirror.NONE, ERotation.COUNTERCLOCKWISE_90, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(-1, 8, 1));

        test(new BlockPos.MutableBlockPos(0, 4, 0), EMirror.NONE, ERotation.CLOCKWISE_90, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(0, 4, 0));

        test(new BlockPos.MutableBlockPos(-2, 1, 4), EMirror.NONE, ERotation.COUNTERCLOCKWISE_90, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(-4, 1, -2));


        LOGGER.info("===== Test #3.2: rotation + moving =====");
        test(new BlockPos.MutableBlockPos(-1, 0, 3), EMirror.NONE, ERotation.CLOCKWISE_90,
             new BlockPos.MutableBlockPos(3, 0, -1),
             new BlockPos.MutableBlockPos(6, 0, 0));

        test(new BlockPos.MutableBlockPos(-1, 0, 3), EMirror.NONE, ERotation.CLOCKWISE_180,
             new BlockPos.MutableBlockPos(0, 1, 0),
             new BlockPos.MutableBlockPos(1, 1, -3));

        test(new BlockPos.MutableBlockPos(-1, 1, 3), EMirror.NONE, ERotation.COUNTERCLOCKWISE_90,
             new BlockPos.MutableBlockPos(-1, 0, 0),
             new BlockPos.MutableBlockPos(-4, 1, -1));


        LOGGER.info("===== Test #4.1: mirroring =====");
        test(new BlockPos.MutableBlockPos(2, 0, 2), EMirror.Z_AXIS, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(-2, 0, 2));

        test(new BlockPos.MutableBlockPos(1, 3, 4), EMirror.Z_AXIS, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(-1, 3, 4));

        test(new BlockPos.MutableBlockPos(1, 0, 4), EMirror.X_AXIS, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(1, 0, -4));

        test(new BlockPos.MutableBlockPos(1, 0, 4), EMirror.XZ_AXIS, ERotation.NONE, BlockPos.ZERO,
             new BlockPos.MutableBlockPos(-1, 0, -4));


        LOGGER.info("===== Test #4.2: mirroring + moving =====");
        test(new BlockPos.MutableBlockPos(2, 0, 2), EMirror.Z_AXIS, ERotation.NONE,
             new BlockPos.MutableBlockPos(4, 0, 0),
             new BlockPos.MutableBlockPos(2, 0, 2));

        test(new BlockPos.MutableBlockPos(1, 2, 4), EMirror.X_AXIS, ERotation.NONE,
             new BlockPos.MutableBlockPos(3, 2, 0),
             new BlockPos.MutableBlockPos(4, 4, -4));

        test(new BlockPos.MutableBlockPos(4, 0, 3), EMirror.XZ_AXIS, ERotation.NONE,
             new BlockPos.MutableBlockPos(4, 0, 3),
             new BlockPos.MutableBlockPos(0, 0, 0));


        LOGGER.info("===== Test #5: mirroring + rotation =====");
        test(new BlockPos.MutableBlockPos(3, 0, 2), EMirror.Z_AXIS, ERotation.CLOCKWISE_180,
             new BlockPos.MutableBlockPos(-2, 0, 1),
             new BlockPos.MutableBlockPos(1, 0, -1));


        LOGGER.info("Testing complete! {}/{} successful, {}/{} failed", success, testId, failed, testId);
    }


    private static int testId = 0;
    private static int success = 0, failed;


    public static void test(
            BlockPos.MutableBlockPos targetPos,
            EMirror mirrorIn,
            ERotation rotationIn,
            BlockPos offset, BlockPos.MutableBlockPos expected
    ) {
        var result = MyTestBlock.getTransformedPos(targetPos, mirrorIn, rotationIn, offset);

        if (result.equals(expected)) {
            LOGGER.info("Case #{}: OK!", testId++);
            success++;
        } else {
            LOGGER.info("Case #{}: FAILED!", testId++);
            failed++;
        }


    }
}
