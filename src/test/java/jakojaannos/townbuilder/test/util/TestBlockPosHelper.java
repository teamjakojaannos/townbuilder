package jakojaannos.townbuilder.test.util;

import jakojaannos.test.MinecraftRegistriesExtension;
import jakojaannos.test.util.Premise;
import jakojaannos.townbuilder.block.EMirror;
import jakojaannos.townbuilder.block.ERotation;
import jakojaannos.townbuilder.block.MyTestBlock;
import lombok.val;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Bootstrap;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(MinecraftRegistriesExtension.class)
class TestBlockPosHelper {
    @Nested
    class WhenNoTransformationsAreDefined {
        @Test
        void positionDoesNotChange() {
            val original = new BlockPos.MutableBlockPos(4, 5, 6);
            val mutated = MyTestBlock.getTransformedPos(new BlockPos.MutableBlockPos(original),
                                                        EMirror.NONE,
                                                        ERotation.NONE,
                                                        BlockPos.ZERO);
            assertEquals(original, mutated);
        }

        @Test
        void methodDoesNotCreateNewBlockPos() {
            val mutated = new BlockPos.MutableBlockPos(new BlockPos.MutableBlockPos(4, 5, 6));
            val returned = MyTestBlock.getTransformedPos(mutated,
                                                         EMirror.NONE,
                                                         ERotation.NONE,
                                                         BlockPos.ZERO);
            assertSame(mutated, returned);
        }
    }

    @Nested
    class WhenOnlyOffsetIsDefined {
        @Test
        void returnedPositionIsDifferentThanInput() {
            val original = new BlockPos.MutableBlockPos(4, 5, 6);
            val mutated = MyTestBlock.getTransformedPos(new BlockPos.MutableBlockPos(original),
                                                        EMirror.NONE,
                                                        ERotation.NONE,
                                                        new BlockPos(6, 2, 4));
            assertNotEquals(original, mutated);
        }

        @Test
        void transformedPositionIsCorrect() {
            val original = new BlockPos.MutableBlockPos(4, 5, 6);
            val mutated = MyTestBlock.getTransformedPos(new BlockPos.MutableBlockPos(original),
                                                        EMirror.NONE,
                                                        ERotation.NONE,
                                                        new BlockPos(6, 2, 4));
            assertEquals(new BlockPos(10, 7, 10), mutated);
        }

        @Test
        void inputIsMutatedToCorrectValue() {
            val original = new BlockPos.MutableBlockPos(4, 5, 6);
            val mutated = new BlockPos.MutableBlockPos(original);
            MyTestBlock.getTransformedPos(mutated,
                                          EMirror.NONE,
                                          ERotation.NONE,
                                          new BlockPos(6, 2, 4));
            assertEquals(new BlockPos(10, 7, 10), mutated);
        }

        @Test
        void methodDoesNotCreateNewBlockPos() {
            val mutated = new BlockPos.MutableBlockPos(new BlockPos.MutableBlockPos(4, 5, 6));
            val returned = MyTestBlock.getTransformedPos(mutated,
                                                         EMirror.NONE,
                                                         ERotation.NONE,
                                                         new BlockPos(6, 2, 4));
            assertSame(mutated, returned);
        }
    }

    @TestFactory
    Stream<DynamicNode> whenOnlyRotationIsDefined() {
        return Stream.of(
                dynamicContainer("when point is not on the origin", testsForPointNotOnOrigin()),
                dynamicContainer("when point is on the origin", testsForPointOnOrigin())
        );
    }

    private static Stream<DynamicNode> testsForPointOnOrigin() {
        val inputs = Arrays.stream(ERotation.values())
                           .filter((r) -> r != ERotation.NONE);

        return inputs.map(rotation -> dynamicContainer("and rotation is " + rotation, Stream.of(
                dynamicTest("returned position is at the same position as the input", () -> {
                    val original = new BlockPos.MutableBlockPos(0, 0, 0);
                    val mutated = MyTestBlock.getTransformedPos(new BlockPos.MutableBlockPos(original),
                                                                EMirror.NONE,
                                                                rotation,
                                                                BlockPos.ZERO);
                    assertEquals(original, mutated);
                }),
                dynamicTest("returned BlockPos is the exact same object as input", () -> {
                    val original = new BlockPos.MutableBlockPos(0, 0, 0);
                    val mutated = new BlockPos.MutableBlockPos(original);
                    val returned = MyTestBlock.getTransformedPos(mutated,
                                                                 EMirror.NONE,
                                                                 rotation,
                                                                 BlockPos.ZERO);
                    assertSame(mutated, returned);
                })))
        );
    }

    private static Stream<DynamicNode> testsForPointNotOnOrigin() {
        val inputs = new Premise<ERotation, BlockPos>((expectThat) -> Stream.of(
                expectThat.input(ERotation.CLOCKWISE_90).yields(new BlockPos(1, 0, -1)),
                expectThat.input(ERotation.CLOCKWISE_180).yields(new BlockPos(-1, 0, -1)),
                expectThat.input(ERotation.COUNTERCLOCKWISE_90).yields(new BlockPos(-1, 0, 1))
        ));

        return inputs.asStream().map(expectation -> dynamicContainer("and rotation is " + expectation.getInput(), Stream.of(
                dynamicTest("returned position is at different position than the input", () -> {
                    val original = new BlockPos.MutableBlockPos(1, 0, 1);
                    val mutated = MyTestBlock.getTransformedPos(new BlockPos.MutableBlockPos(original),
                                                                EMirror.NONE,
                                                                expectation.getInput(),
                                                                BlockPos.ZERO);
                    assertNotEquals(original, mutated);
                }),
                dynamicTest("returned position is transformed as expected (" + expectation.getExpected() + ")", () -> {
                    val original = new BlockPos.MutableBlockPos(1, 0, 1);
                    val mutated = MyTestBlock.getTransformedPos(new BlockPos.MutableBlockPos(original),
                                                                EMirror.NONE,
                                                                expectation.getInput(),
                                                                BlockPos.ZERO);
                    assertEquals(expectation.getExpected(), mutated);
                }),
                dynamicTest("input is mutated as expected (" + expectation.getExpected() + ")", () -> {
                    val original = new BlockPos.MutableBlockPos(1, 0, 1);
                    val mutated = new BlockPos.MutableBlockPos(original);
                    MyTestBlock.getTransformedPos(mutated,
                                                  EMirror.NONE,
                                                  expectation.getInput(),
                                                  BlockPos.ZERO);
                    assertEquals(expectation.getExpected(), mutated);
                }),
                dynamicTest("returned BlockPos is the exact same object as input", () -> {
                    val original = new BlockPos.MutableBlockPos(1, 0, 1);
                    val mutated = new BlockPos.MutableBlockPos(original);
                    val returned = MyTestBlock.getTransformedPos(mutated,
                                                                 EMirror.NONE,
                                                                 expectation.getInput(),
                                                                 BlockPos.ZERO);
                    assertSame(mutated, returned);
                })))
        );
    }
}
