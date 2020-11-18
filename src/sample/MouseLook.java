package sample;

import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public class MouseLook implements EventHandler<MouseEvent> {
    private Rotate rotation;
    private int oldX, newX;
    private boolean alreadyMoved = false;
    private final Camera camera;

    public MouseLook(final Camera camera) {
        this.camera = camera;
    }

    @Override
    public void handle(MouseEvent event) {
        if ( alreadyMoved ) {
            newX = (int) event.getScreenX();

            if ( oldX < newX ) { // if mouse moved to right
                rotation = new Rotate( 0.1D,
                        // camera rotates around its location
                        camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(),
                        Rotate.Y_AXIS );


            } else if ( oldX > newX ) { // if mouse moved to left
                rotation = new Rotate( -0.1D,
                        // camera rotates around its location
                        camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(),
                        Rotate.Y_AXIS );

            }
            camera.getTransforms().addAll( rotation );

            oldX = newX;
        } else {
            oldX = (int) event.getScreenX();
            alreadyMoved = true;
        }
    }
}