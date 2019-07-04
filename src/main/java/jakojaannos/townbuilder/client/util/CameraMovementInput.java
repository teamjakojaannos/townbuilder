package jakojaannos.townbuilder.client.util;

import jakojaannos.townbuilder.TownBuilder;
import lombok.Getter;
import lombok.val;

public class CameraMovementInput {
    private boolean forwardKeyDown;
    private boolean backKeyDown;
    private boolean leftKeyDown;
    private boolean rightKeyDown;

    @Getter private float inputHorizontal;
    @Getter private float inputVertical;

    public boolean wantsMove() {
        return forwardKeyDown || backKeyDown || leftKeyDown || rightKeyDown;
    }

    public void tick() {
        val gameSettings = TownBuilder.getClient().getGameSettings();
        forwardKeyDown = gameSettings.keyBindBuilderMoveForward.isKeyDown();
        backKeyDown = gameSettings.keyBindBuilderMoveBack.isKeyDown();
        leftKeyDown = gameSettings.keyBindBuilderMoveLeft.isKeyDown();
        rightKeyDown = gameSettings.keyBindBuilderMoveRight.isKeyDown();

        inputVertical = (forwardKeyDown ? 1 : 0) - (backKeyDown ? 1 : 0);
        inputHorizontal = (rightKeyDown ? 1 : 0) - (leftKeyDown ? 1 : 0);
    }
}
