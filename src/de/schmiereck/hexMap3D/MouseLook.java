package de.schmiereck.hexMap3D;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Callback;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

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

    public void handleMouseScrolling(final ScrollEvent event) {
        moveForward(event.getDeltaY() * 2.0D);
    }

    // https://github.com/FXyz/FXyz/blob/58913cc1328ad95af40b2fbf39044c126c71584b/FXyz-Core/src/main/java/org/fxyz3d/scene/SimpleFPSCamera.java#L465

    private void moveForward(final double moveSpeed) {
        camera.setTranslateX(camera.getTranslateX() + moveSpeed * getN().getX());
        camera.setTranslateY(camera.getTranslateY() + moveSpeed * getN().getY());
        camera.setTranslateZ(camera.getTranslateZ() + moveSpeed * getN().getZ());
    }

    private void moveBack(final double moveSpeed) {
        camera.setTranslateX(camera.getTranslateX() + moveSpeed * -getN().getX());
        camera.setTranslateY(camera.getTranslateY() + moveSpeed * -getN().getY());
        camera.setTranslateZ(camera.getTranslateZ() + moveSpeed * -getN().getZ());
    }

    private void strafeLeft(final double moveSpeed) {
        camera.setTranslateX(camera.getTranslateX() + moveSpeed * -getU().getX());
        camera.setTranslateY(camera.getTranslateY() + moveSpeed * -getU().getY());
        camera.setTranslateZ(camera.getTranslateZ() + moveSpeed * -getU().getZ());
    }

    private void strafeRight(final double moveSpeed) {
        camera.setTranslateX(camera.getTranslateX() + moveSpeed * getU().getX());
        camera.setTranslateY(camera.getTranslateY() + moveSpeed * getU().getY());
        camera.setTranslateZ(camera.getTranslateZ() + moveSpeed * getU().getZ());
    }
    
    private void moveUp(final double moveSpeed) {
        camera.setTranslateX(camera.getTranslateX() + moveSpeed * -getV().getX());
        camera.setTranslateY(camera.getTranslateY() + moveSpeed * -getV().getY());
        camera.setTranslateZ(camera.getTranslateZ() + moveSpeed * -getV().getZ());
    }

    private void moveDown(final double moveSpeed) {
        camera.setTranslateX(camera.getTranslateX() + moveSpeed * getV().getX());
        camera.setTranslateY(camera.getTranslateY() + moveSpeed * getV().getY());
        camera.setTranslateZ(camera.getTranslateZ() + moveSpeed * getV().getZ());
    }

    //Forward / look direction
    private final Callback<Transform, Point3D> F = (a) -> {
        return new Point3D(a.getMzx(), a.getMzy(), a.getMzz());
    };
    private final Callback<Transform, Point3D> N = (a) -> {
        return new Point3D(a.getMxz(), a.getMyz(), a.getMzz());
    };

    // up direction
    private final Callback<Transform, Point3D> UP = (a) -> {
        return new Point3D(a.getMyx(), a.getMyy(), a.getMyz());
    };
    private final Callback<Transform, Point3D> V = (a) -> {
        return new Point3D(a.getMxy(), a.getMyy(), a.getMzy());
    };
    // right direction
    private final Callback<Transform, Point3D> R = (a) -> {
        return new Point3D(a.getMxx(), a.getMxy(), a.getMxz());
    };
    private final Callback<Transform, Point3D> U = (a) -> {
        return new Point3D(a.getMxx(), a.getMyx(), a.getMzx());
    };
    //position
    private final Callback<Transform, Point3D> P = (a) -> {
        return new Point3D(a.getTx(), a.getTy(), a.getTz());
    };


    private Point3D getF() {
        return F.call(this.camera.getLocalToSceneTransform());
    }

    public Point3D getLookDirection() {
        return getF();
    }
    private Point3D getN() {
        return N.call(this.camera.getLocalToSceneTransform());
    }

    public Point3D getLookNormal() {
        return getN();
    }

    private Point3D getR() {
        return R.call(this.camera.getLocalToSceneTransform());
    }

    private Point3D getU() {
        return U.call(this.camera.getLocalToSceneTransform());
    }

    private Point3D getUp() {
        return UP.call(this.camera.getLocalToSceneTransform());
    }

    private Point3D getV() {
        return V.call(this.camera.getLocalToSceneTransform());
    }

    public final Point3D getPosition() {
        return P.call(this.camera.getLocalToSceneTransform());
    }}