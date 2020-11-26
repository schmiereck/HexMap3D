package de.schmiereck.hexMap3D.view;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Callback;

public class MouseLook {
    private double oldX, oldY;
    private boolean alreadyMoved = false;
    private final Group cameraGroup;
    private final Camera camera;

    public MouseLook(final Group cameraGroup, final Camera camera) {
        this.cameraGroup = cameraGroup;
        this.camera = camera;
        this.cameraGroup.getTransforms().add(affine);
        this.cameraGroup.getTransforms().add(rotateAffineY);
        this.cameraGroup.getTransforms().add(rotateAffineX);
    }

    public void handle(final MouseEvent event) {
        if (this.alreadyMoved) {
            final double newX = event.getSceneX();
            final double xDiff = this.oldX - newX;
            this.oldX = newX;
            final double newY = event.getSceneY();
            final double yDiff = this.oldY - newY;
            this.oldY = newY;
            //rotate2(xDiff, yDiff);
            rotate(xDiff, -yDiff, 0.9D);

        } else {
            this.oldX = (int) event.getSceneX();
            this.oldY = (int) event.getSceneY();
            this.alreadyMoved = true;
        }
    }

    public void movedFinished() {
        this.alreadyMoved = false;
    }

    private final Translate t = new Translate(0, 0, 0);
    private final Affine affine = new Affine();
    private final Affine rotateAffineY = new Affine();
    private final Affine rotateAffineX = new Affine();
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private double mouseModifier = 0.1D;

    private void rotate(final double mouseDeltaX, final double mouseDeltaY, final double mouseSpeed) {
        if ((mouseDeltaX != 0.0D) || (mouseDeltaY != 0)) {
            //final Point3D position = this.getPosition();
            //t.setX(position.getX());
            //t.setY(position.getY());
            //t.setZ(position.getZ());

            //affine.setToIdentity();
/*
            //rotateY.setAngle(clamp(-360, ((rotateY.getAngle() + mouseDeltaX * (mouseSpeed * mouseModifier)) % 360 + 540) % 360 - 180, 360)); // horizontal
            //rotateX.setAngle(clamp(-45, ((rotateX.getAngle() - mouseDeltaY * (mouseSpeed * mouseModifier)) % 360 + 540) % 360 - 180, 35)); // vertical
            rotateX.setAngle(mouseDeltaY * (mouseSpeed * mouseModifier)); // horizontal
            rotateY.setAngle(mouseDeltaX * (mouseSpeed * mouseModifier)); // horizontal
            //affine.prepend(t.createConcatenation(rotateY.createConcatenation(rotateX)));
            //rotateAffine.prepend(rotateY.createConcatenation(rotateX));
            rotateAffine.prepend(rotateX);
            rotateAffine.prepend(rotateY);
*/
            final Point3D n2 = getN();
            rotateAffineY.appendRotation(mouseDeltaX * (mouseSpeed * mouseModifier),
                    0,//n2.getX(),
                    0,//n2.getY(),
                    0,//n2.getZ(),
                    Rotate.Y_AXIS.getX(), Rotate.Y_AXIS.getY(), Rotate.Y_AXIS.getZ());
            final Point3D n = getN();
            //rotateAffine.appendRotation(mouseDeltaX * (mouseSpeed * mouseModifier), getN(), Rotate.Y_AXIS);
            //rotateAffine.appendRotation(mouseDeltaY * (mouseSpeed * mouseModifier), getN(), Rotate.X_AXIS);
            rotateAffineX.appendRotation(mouseDeltaY * (mouseSpeed * mouseModifier),
                    0,//n.getX(),
                    0,//n.getY(),
                    0,//n.getZ(),
                    Rotate.X_AXIS.getX(), Rotate.X_AXIS.getY(), Rotate.X_AXIS.getZ());
        }
    }

    private void rotate2(final double xDiff, final double yDiff) {
        if (xDiff != 0) {
            final Rotate rotation = new Rotate(xDiff / 32.0D,
            // camera rotates around its location
                    this.camera.getTranslateX(), this.camera.getTranslateY(), this.camera.getTranslateZ(), Rotate.Y_AXIS);
            this.camera.getTransforms().addAll(rotation);
        }
        if (yDiff != 0) {
            final Rotate rotation = new Rotate(yDiff / 32.0D,
            // camera rotates around its location
                    this.camera.getTranslateX(), this.camera.getTranslateY(), this.camera.getTranslateZ(), Rotate.X_AXIS);
            this.camera.getTransforms().addAll(rotation);
        }
    }

    public void handleMouseScrolling(final ScrollEvent event) {
        moveForward(event.getDeltaY() * 2.0D);
    }

    // https://github.com/FXyz/FXyz/blob/58913cc1328ad95af40b2fbf39044c126c71584b/FXyz-Core/src/main/java/org/fxyz3d/scene/SimpleFPSCamera.java#L465

    public void moveForward(final double moveSpeed) {
        final Point3D position = this.getPosition();
        final Point3D n = getN();
        //affine.setTx(position.getX() + moveSpeed * n.getX());
        //affine.setTy(position.getY() + moveSpeed * n.getY());
        //affine.setTz(position.getZ() + moveSpeed * n.getZ());

        //affine.setTx(1.0D);
        affine.setTx(affine.getTx() + moveSpeed * n.getX());
        affine.setTy(affine.getTy() + moveSpeed * n.getY());
        affine.setTz(affine.getTz() + moveSpeed * n.getZ());
    }

    public void moveBack(final double moveSpeed) {
        affine.setTx(affine.getTx() + moveSpeed * -getN().getX());
        affine.setTy(affine.getTy() + moveSpeed * -getN().getY());
        affine.setTz(affine.getTz() + moveSpeed * -getN().getZ());
    }

    public void strafeLeft(final double moveSpeed) {
        affine.setTx(affine.getTx() + moveSpeed * -getU().getX());
        affine.setTy(affine.getTy() + moveSpeed * -getU().getY());
        affine.setTz(affine.getTz() + moveSpeed * -getU().getZ());
    }

    public void strafeRight(final double moveSpeed) {
        affine.setTx(affine.getTx() + moveSpeed * getU().getX());
        affine.setTy(affine.getTy() + moveSpeed * getU().getY());
        affine.setTz(affine.getTz() + moveSpeed * getU().getZ());
    }

    public void moveUp(final double moveSpeed) {
        affine.setTx(affine.getTx() + moveSpeed * -getV().getX());
        affine.setTy(affine.getTy() + moveSpeed * -getV().getY());
        affine.setTz(affine.getTz() + moveSpeed * -getV().getZ());
    }

    public void moveDown(final double moveSpeed) {
        affine.setTx(affine.getTx() + moveSpeed * getV().getX());
        affine.setTy(affine.getTy() + moveSpeed * getV().getY());
        affine.setTz(affine.getTz() + moveSpeed * getV().getZ());
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

    public static double clamp(double input, double min, double max) {
        return (input < min) ? min : (input > max) ? max : input;
    }

    private Point3D getF() {
        return F.call(this.getLocalToSceneTransform());
    }

    public Point3D getLookDirection() {
        return getF();
    }
    private Point3D getN() {
        return N.call(this.getLocalToSceneTransform());
    }

    public Point3D getLookNormal() {
        return getN();
    }

    private Point3D getR() {
        return R.call(this.getLocalToSceneTransform());
    }

    private Point3D getU() {
        return U.call(this.getLocalToSceneTransform());
    }

    private Point3D getUp() {
        return UP.call(this.getLocalToSceneTransform());
    }

    private Point3D getV() {
        return V.call(this.getLocalToSceneTransform());
    }

    public final Point3D getPosition() {
        return P.call(this.getLocalToSceneTransform());
    }

    public final Transform getLocalToSceneTransform() {
        //return t;
        //return this.camera.getParent().getLocalToSceneTransform();
        //return this.camera.getLocalToSceneTransform();
        return this.camera.getLocalToSceneTransform();
        //return this.cameraGroup.getLocalToParentTransform();
        //return this.cameraGroup.getParent().getLocalToSceneTransform();
        //return this.camera.getLocalToParentTransform();
    }
}