package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import static java.lang.String.format;

public class Main extends Application {

    private final static double viewGridStepA = 16.0D * 4.0D;
    private final static double viewGridStepA2 = viewGridStepA / 2.0D;
    private final static double viewGridStepA4 = viewGridStepA / 4.0D;
    private final static double viewGridStepH = viewGridStepA*Math.sqrt(3.0D)/2.0D;
    private final static double viewGridStepMa = viewGridStepA/Math.sqrt(12.0D);
    private final static double viewGridStepMb = viewGridStepH-viewGridStepMa;
    private final static double viewGridStepZ = (viewGridStepA/3.0D)*Math.sqrt(6.0D);

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
        primaryStage.setScene(scene);

        //-----------------------------------------------------------------------------
        primaryStage.show();

        final MouseLook mouseLook = new MouseLook(camera);
        scene.setOnMousePressed((event) -> {
            event.setDragDetect(true);
        });
        scene.setOnMouseReleased((event) -> {
            //event.setDragDetect(false);
            mouseLook.movedFinished();
        });
        //scene.setOnMouseMoved(new MouseLook(camera));
        scene.setOnMouseDragged((event) -> {
            mouseLook.handle(event);
            event.setDragDetect(false);
        });

        //-----------------------------------------------------------------------------
    }

    private static void generateViewGrid(final ObservableList<Node> rootChildList, final int xSizeGrid, final int ySizeGrid, final int zSizeGrid) {
        for (int zPos = 0; zPos < zSizeGrid; zPos++) {
            for (int yPos = 0; yPos < ySizeGrid; yPos++) {
                for (int xPos = 0; xPos < xSizeGrid; xPos++) {
                    final Color color;
                    switch (zPos % 3) {
                        case 0 -> { // A
                            color = Color.DARKRED;
                        }
                        case 1 -> { // B
                            color = Color.DARKOLIVEGREEN;
                        }
                        case 2 -> { // C
                            color = Color.DARKSLATEBLUE;
                        }
                        default -> throw new RuntimeException(format("Unexpected zPos \"%d\".", zPos));
                    }
                    final Point3D point3D = createViewGridPoint3D(xPos, yPos, zPos);
                    final Box box = createGidBox(point3D, color);
                    rootChildList.add(box);
                    switch (zPos % 3) {
                        case 0 -> { // A
                            if (yPos % 2 == 0) {
                                // A -> A
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos - 1, yPos + 1, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos), point3D));
                                // A -> B
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos - 1, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos + 1), point3D));
                            } else {
                                // A -> A
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos + 1, zPos), point3D));
                                // A -> B
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos + 1), point3D));
                            }
                        }
                        case 1 -> { // B
                            if (yPos % 2 == 0) {
                                // B -> B
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos + 1, zPos), point3D));
                                // B -> C
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos + 1), point3D));
                            } else {
                                // B -> B
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos - 1, yPos + 1, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos), point3D));
                                // B -> C
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos - 1, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos + 1), point3D));
                            }
                        }
                        case 2 -> { // C
                            if (yPos % 2 == 0) {
                                // C -> C
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos - 1, yPos + 1, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos), point3D));
                                // C -> A
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos - 1, yPos - 1, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos - 1, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos + 1), point3D));
                            } else {
                                // C -> C
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos + 1, zPos), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos + 1, zPos), point3D));
                                // C -> A
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos, yPos - 1, zPos + 1), point3D));
                                rootChildList.add(createConnection(createViewGridPoint3D(xPos + 1, yPos - 1, zPos + 1), point3D));
                            }
                        }
                        default -> throw new RuntimeException(format("Unexpected zPos \"%d\".", zPos));
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
                if (yPos % 2 == 0) {
                    x = xPos * viewGridStepA;
                    y = yPos * viewGridStepH;
                } else {
                    x = xPos * viewGridStepA + viewGridStepA2;
                    y = yPos * viewGridStepH;
                }
            }
            case 1 -> { // B
                if (yPos % 2 == 0) {
                    x = xPos * viewGridStepA + viewGridStepA2;
                    y = yPos * viewGridStepH - viewGridStepMa;
                } else {
                    x = xPos * viewGridStepA;
                    y = yPos * viewGridStepH - viewGridStepMa;
                }
            }
            case 2 -> { // C
                if (yPos % 2 == 0) {
                    x = xPos * viewGridStepA;
                    y = yPos * viewGridStepH - viewGridStepMb;
                } else {
                    x = xPos * viewGridStepA + viewGridStepA2;
                    y = yPos * viewGridStepH - viewGridStepMb;
                }
            }
            default -> throw new RuntimeException(format("Unexpected zPos \"%d\".", zPos));
        }
        z = zPos * viewGridStepZ;

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
