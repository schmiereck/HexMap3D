package de.schmiereck.hexMap3D;

import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import static java.lang.String.format;

public class GridViewUtils {

    public final static double viewGridStepA = 16.0D * 4.0D;
    public final static double viewGridStepA2 = viewGridStepA / 2.0D;
    public final static double viewGridStepA4 = viewGridStepA / 4.0D;
    public final static double viewGridStepH = viewGridStepA*Math.sqrt(3.0D)/2.0D;
    public final static double viewGridStepMa = viewGridStepA/Math.sqrt(12.0D);
    public final static double viewGridStepMb = viewGridStepH-viewGridStepMa;
    public final static double viewGridStepZ = (viewGridStepA/3.0D)*Math.sqrt(6.0D);

    public static void generateViewGrid(final Group rootGroup, final int xSizeGrid, final int ySizeGrid, final int zSizeGrid) {
        final ObservableList<Node> rootChildList = rootGroup.getChildren();

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
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 1, -1, 0, 0, 1, 0);
                                // A -> B
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 0, -1, 0, 1, 0, +1);

                                createOutputs(rootChildList, point3D, xPos, yPos, zPos, GridUtils.a0ConArr);
                            } else {
                                // A -> A
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 1, 0, +1, 0, +1, 0);
                                // A -> B
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 0, 1, 0, 1, 0, +1);

                                createOutputs(rootChildList, point3D, xPos, yPos, zPos, GridUtils.a1ConArr);
                            }
                        }
                        case 1 -> { // B
                            if (yPos % 2 == 0) {
                                // B -> B
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 1, 0, 1, 0, 1, 0);
                                // B -> C
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 0, 0, 1, 1, 0, 1);

                                createOutputs(rootChildList, point3D, xPos, yPos, zPos, GridUtils.b0ConArr);
                            } else {
                                // B -> B
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 1, -1, 0, 0, 1, 0);
                                // B -> C
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 0, -1, 0, 1, 0, 1);

                                createOutputs(rootChildList, point3D, xPos, yPos, zPos, GridUtils.b1ConArr);
                            }
                        }
                        case 2 -> { // C
                            if (yPos % 2 == 0) {
                                // C -> C
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 1, -1, 0, 0, 1, 0);
                                // C -> A
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 0, -1, 0, 0, -1, 1);

                                createOutputs(rootChildList, point3D, xPos, yPos, zPos, GridUtils.c0ConArr);
                            } else {
                                // C -> C
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 0, 1, 0, 0, 1, 0);
                                // C -> A
                                createConnections(rootChildList, point3D, xPos, yPos, zPos, 0, 0, 1, 0, -1, 1);

                                createOutputs(rootChildList, point3D, xPos, yPos, zPos, GridUtils.c1ConArr);
                            }
                        }
                        default -> throw new RuntimeException(format("Unexpected zPos \"%d\".", zPos));
                    }
                }
            }
        }
    }

    private static void createOutputs(final ObservableList<Node> rootChildList, final Point3D point3D, final int xPos, final int yPos, final int zPos, final int[][] conArr) {
        for (final int[] con : conArr) {
            rootChildList.add(createOutput(point3D, createViewGridPoint3D(xPos + con[0], yPos + con[1], zPos + con[2])));
        }
    }

    private static void createConnections(final ObservableList<Node> rootChildList, final Point3D point3D,
            final int xPos, final int yPos, final int zPos,
            final int x1d, final int x2d, final int x3d,
            final int y1d, final int y2d,
            final int zd) {
        rootChildList.add(createConnection(createViewGridPoint3D(xPos + x1d, yPos + y1d, zPos + zd), point3D));
        rootChildList.add(createConnection(createViewGridPoint3D(xPos + x2d, yPos + y2d, zPos + zd), point3D));
        rootChildList.add(createConnection(createViewGridPoint3D(xPos + x3d, yPos + y2d, zPos + zd), point3D));
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
            case 1, -2 -> { // B
                if (yPos % 2 == 0) {
                    x = xPos * viewGridStepA + viewGridStepA2;
                    y = yPos * viewGridStepH - viewGridStepMa;
                } else {
                    x = xPos * viewGridStepA;
                    y = yPos * viewGridStepH - viewGridStepMa;
                }
            }
            case 2, -1 -> { // C
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

        box.setWidth(16.0);
        box.setHeight(16.0);
        box.setDepth(16.0);

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
        final Point3D yAxis = new Point3D(0.0D, 1.0D, 0.0D);
        final Point3D diff = target.subtract(origin);
        final double height = diff.magnitude();

        final Point3D mid = target.midpoint(origin);
        final Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        final Point3D axisOfRotation = diff.crossProduct(yAxis);
        final double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        final Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        final Cylinder line = new Cylinder(1.0D, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    private static Box createOutput(final Point3D origin, final Point3D target) {
        final Point3D yAxis = new Point3D(0.0D, 1.0D, 0.0D);
        final Point3D target2 = target.midpoint(origin);
        final Point3D diff = target2.subtract(origin);
        final double height = diff.magnitude();

        final Point3D mid = target2.midpoint(origin);
        final Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        final Point3D axisOfRotation = diff.crossProduct(yAxis);
        final double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        final Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        final Box box = new Box(6.0D, height, 6.0D);

        box.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        //box.setHeight(height/2);
        return box;
    }
}
