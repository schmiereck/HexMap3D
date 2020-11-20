package sample;

import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public class MouseLook {
    private int oldX, oldY;
    private boolean alreadyMoved = false;
    private final Camera camera;

    public MouseLook(final Camera camera) {
        this.camera = camera;
    }

    public void handle(final MouseEvent event) {
        if (this.alreadyMoved) {
            final int newX = (int) event.getScreenX();
            final int xDiff = this.oldX - newX;
            if (xDiff != 0) {
                final Rotate rotation = new Rotate(xDiff / 32.0D,
                        // camera rotates around its location
                        this.camera.getTranslateX(), this.camera.getTranslateY(), this.camera.getTranslateZ(), Rotate.Y_AXIS);
                this.camera.getTransforms().addAll(rotation);
            }
            this.oldX = newX;
            final int newY = (int) event.getScreenY();
            final int yDiff = newY - this.oldY;
            if (yDiff != 0) {
                final Rotate rotation = new Rotate(yDiff / 32.0D,
                        // camera rotates around its location
                        this.camera.getTranslateX(), this.camera.getTranslateY(), this.camera.getTranslateZ(), Rotate.X_AXIS);
                this.camera.getTransforms().addAll(rotation);
            }
            this.oldY = newY;
        } else {
            this.oldX = (int) event.getScreenX();
            this.oldY = (int) event.getScreenY();
            this.alreadyMoved = true;
        }
    }
    public void movedFinished() {
        this.alreadyMoved = false;
    }
}