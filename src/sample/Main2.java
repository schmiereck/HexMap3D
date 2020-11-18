package sample;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import static java.lang.String.format;

public class Main2 extends Application {

    private final static double viewGridStepA = 16.0D * 4.0D;
    private final static double viewGridStepA2 = viewGridStepA / 2.0D;
    private final static double viewGridStepA4 = viewGridStepA / 4.0D;
    private final static double viewGridStepH = Math.sqrt(3.0D)/2.0D*viewGridStepA;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //-----------------------------------------------------------------------------
        primaryStage.setTitle("Hello World");

        //-----------------------------------------------------------------------------
        final Group root = new Group();
        //final Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        root.setTranslateX(viewGridStepA4);
        root.setTranslateY(viewGridStepA4);
        root.setTranslateZ(70);

        final ObservableList<Node> rootChildList = root.getChildren();

        //-----------------------------------------------------------------------------
        final int xSizeGrid = 16;
        final int ySizeGrid = 16;
        final int zSizeGrid = 3;
        generateViewGrid(rootChildList, xSizeGrid, ySizeGrid, zSizeGrid);

        //-----------------------------------------------------------------------------
        final PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setTranslateX(xSizeGrid / 2 * viewGridStepA);
        camera.setTranslateY(ySizeGrid / 2 * viewGridStepH);
        camera.setTranslateZ(-900.0D);

        camera.setRotationAxis(Rotate.Y_AXIS);
        camera.setRotate(0.0D);

        camera.setFarClip(8000.0D);
        camera.setFieldOfView(50.0D);

        //-----------------------------------------------------------------------------
        final Scene scene = new Scene(root,
                xSizeGrid * viewGridStepA + viewGridStepA2, ySizeGrid * viewGridStepH,
                true, SceneAntialiasing.BALANCED);

        scene.setCamera(camera);
        scene.setOnMouseMoved(new MouseLook(camera));

        primaryStage.setScene(scene);

        //-----------------------------------------------------------------------------
        primaryStage.show();

        //-----------------------------------------------------------------------------
    }

    private static void generateViewGrid(final ObservableList<Node> rootChildList, final int xSizeGrid, final int ySizeGrid, final int zSizeGrid) {
        for (int zPos = 0; zPos < zSizeGrid; zPos++) {
            for (int yPos = 0; yPos < ySizeGrid; yPos++) {
                for (int xPos = 0; xPos < xSizeGrid; xPos++) {
                    final Color color;
                    switch (zPos % 3) {
                        case 0 -> { // A
                            color = Color.DARKSLATEBLUE;
                        }
                        case 1 -> { // B
                            color = Color.DARKGOLDENROD;
                        }
                        case 2 -> { // C
                            color = Color.DARKOLIVEGREEN;
                        }
                        default -> throw new RuntimeException(format("Unexpected zPos \"%d\".", zPos));
                    }
                    final Point3D point3D = createViewGridPoint3D(xPos, yPos, zPos);
                    final Box box = createGidBox(point3D, color);
                    rootChildList.add(box);

                    if (xPos > 0) {
                        final Point3D lxPoint3D = createViewGridPoint3D(xPos - 1, yPos, zPos);
                        rootChildList.add(createConnection(lxPoint3D, point3D));
                    }
                    if (yPos > 0) {
                        final Point3D lyPoint3D = createViewGridPoint3D(xPos, yPos - 1, zPos);
                        rootChildList.add(createConnection(lyPoint3D, point3D));
                    }
                    if (zPos > 0) {
                        if (yPos % 2 == 0) {
                            if (zPos % 2 == 0) {
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos - 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos - 1, yPos, zPos - 1), point3D));
                            } else {
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos - 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos - 1), point3D));
                            }
                        } else {
                            if (zPos % 2 == 0) {
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos - 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos - 1), point3D));
                            } else {
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos - 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos - 1, yPos, zPos - 1), point3D));
                            }
                        }
                    }
                    if (zPos % 2 == 0) {
                        if (xPos > 0) {
                            if (yPos % 2 == 0) {
                                if (yPos < ySizeGrid - 1) {
                                    final Point3D lxPoint3D = createViewGridPoint3D(xPos - 1, yPos + 1, zPos);
                                    rootChildList.add(createConnection(lxPoint3D, point3D));
                                }
                                if (yPos > 0) {
                                    final Point3D lxPoint3D = createViewGridPoint3D(xPos - 1, yPos - 1, zPos);
                                    rootChildList.add(createConnection(lxPoint3D, point3D));
                                }
                            }
                        }
                    } else {
                        if (xPos > 0) {
                            if (yPos % 2 == 1) {
                                if (yPos < ySizeGrid - 1) {
                                    final Point3D lxPoint3D = createViewGridPoint3D(xPos - 1, yPos + 1, zPos);
                                    rootChildList.add(createConnection(lxPoint3D, point3D));
                                }
                                if (yPos > 0) {
                                    final Point3D lxPoint3D = createViewGridPoint3D(xPos - 1, yPos - 1, zPos);
                                    rootChildList.add(createConnection(lxPoint3D, point3D));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static Point3D createViewGridPoint3D(final int xPos, final int yPos, final int zPos) {
        final double x;
        final double y;
        final double z;
        switch (zPos % 3) {
            case 0 -> { // A

            }
            case 1 -> { // B

            }
            case 2 -> { // C

            }
            default -> throw new RuntimeException(format("Unexpected zPos \"%d\".", zPos));
        }
        if (zPos % 2 == 0) {
            if (yPos % 2 == 0) {
                x = xPos * viewGridStepA;
            } else {
                x = xPos * viewGridStepA + viewGridStepA2;
            }
            y = yPos * viewGridStepH;
            z = zPos * viewGridStepH;
        } else {
            if (yPos % 2 == 0) {
                x = xPos * viewGridStepA + viewGridStepA2;
            } else {
                x = xPos * viewGridStepA;
            }
            y = yPos * viewGridStepH;
            z = zPos * viewGridStepH;
        }
        final Point3D point3D = new Point3D(x, y, z);
        return point3D;
    }

    private static Box createGidBox(final Point3D point3D, final Color color) {
        final Box box = new Box();

        box.setWidth(10.0);
        box.setHeight(10.0);
        box.setDepth(10.0);

        box.setTranslateX(point3D.getX());
        box.setTranslateY(point3D.getY());
        box.setTranslateZ(point3D.getZ());

        box.setDrawMode(DrawMode.FILL);
        {
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(color);
            box.setMaterial(material);
        }
        return box;
    }

    private static Cylinder createConnection(final Point3D origin, final Point3D target) {
        final Point3D yAxis = new Point3D(0, 1, 0);
        final Point3D diff = target.subtract(origin);
        final double height = diff.magnitude();

        final Point3D mid = target.midpoint(origin);
        final Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        final Point3D axisOfRotation = diff.crossProduct(yAxis);
        final double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        final Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        final Cylinder line = new Cylinder(1, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }
}
