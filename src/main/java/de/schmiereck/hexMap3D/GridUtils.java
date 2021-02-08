package de.schmiereck.hexMap3D;

import de.schmiereck.hexMap3D.service.universe.Cell;

import static de.schmiereck.hexMap3D.service.universe.Cell.Dir.*;
import static java.lang.String.format;

public class GridUtils {
    public static class OutputDir {
        public final Cell.Dir dir;
        public final int x, y, z;
        OutputDir(final Cell.Dir dir, final int x, final int y, final int z) {
            this.dir = dir;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    /**
         +--> x0     x1    x2    x3
         |
         v
         y0z3: C0/ \ C1/ \ C2/ \ C3/ \   / \   / \
         y0z2:  / B0\ / B1\ / B2\ / B3\ /   \ /   \
         y0z1: A0====A1====A2====A3====*=====*=====	A rot
         y1z0:  \ C0/ \ C1/ \ C2/ \ C3/ \   / \   /
         y1z2: B0\ /#B1\ / B2\ / B3\ /   \ /   \ /
         y1z1: ===A0===#A1====A2====A3====*=====*==	B grün
         y2z0: C0/ \#C1/ \ C2/ \ C3/ \   / \   / \
         y2z2:  / B0\ / B1\ / B2\ / B3\ /   \ /   \
         y2z1: A0====A1====A2====A3====*=====*=====	C blau
         y3z0:  \ C0/ \ C1/ \ C2/ \ C3/ \   / \   /
         y3z2: B0\ / B1\ / B2\ / B3\ /   \ /   \ /
         y3z1: ===A0====A1====A2====A3====*=====*==

         == -1 ===== 0 ===== 1 ===== 2 ===== 3 ==
            / \ LG2 / \     / \     / \     / \
           /  OR1|RE1  \   /   \   /   \   /   \
          /     \|/     \ /     \ /     \ /     \
        -1==DB2==0==DB1==1=======2=======3=======
          \     /|\     / \     / \     / \     /
           \  RE2|OR2  /   \   /   \   /   \   /
            \ / LG1 \ /     \ /     \ /     \ /
         == -1 ==== 0 ===== 1 ===== 2 ===== 3 ==
     */
    public static final OutputDir[] a0ConArr = new OutputDir[12];
    static {
        // A -> A
        a0ConArr[DB_P.dir()] = new OutputDir(DB_P, 1, 0, 0);
        a0ConArr[DB_N.dir()] = new OutputDir(DB_N, -1, 0, 0);
        a0ConArr[OR_P.dir()] = new OutputDir(OR_P, 0, 1, 0);
        a0ConArr[RE_P.dir()] = new OutputDir(RE_P, -1, 1, 0);
        a0ConArr[OR_N.dir()] = new OutputDir(OR_N, -1, -1, 0);
        a0ConArr[RE_N.dir()] = new OutputDir(RE_N, 0, -1, 0);
        // A -> B
        a0ConArr[LG_P.dir()] = new OutputDir(LG_P, 0, 1, 1);
        a0ConArr[GR_N.dir()] = new OutputDir(GR_N, 0, 0, 1);
        a0ConArr[LB_N.dir()] = new OutputDir(LB_N, -1, 0, 1);
        // A -> C
        a0ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, 0, -1);
        a0ConArr[LB_P.dir()] = new OutputDir(LB_P, 0, 1, -1);
        a0ConArr[GR_P.dir()] = new OutputDir(GR_P, -1, 1, -1);
    }

    public static final OutputDir[] a1ConArr = new OutputDir[12];
    static {
        // A -> A
        a1ConArr[DB_P.dir()] = new OutputDir(DB_P, 1, 0, 0);
        a1ConArr[DB_N.dir()] = new OutputDir(DB_N, -1, 0, 0);
        a1ConArr[RE_P.dir()] = new OutputDir(RE_P, 0, 1, 0);
        a1ConArr[OR_P.dir()] = new OutputDir(OR_P, 1, 1, 0);
        a1ConArr[OR_N.dir()] = new OutputDir(OR_N, 0, -1, 0);
        a1ConArr[RE_N.dir()] = new OutputDir(RE_N, 1, -1, 0);
        // A -> B
        a1ConArr[LG_P.dir()] = new OutputDir(LG_P, 0, 1, 1);
        a1ConArr[LB_N.dir()] = new OutputDir(LB_N, 0, 0, 1);
        a1ConArr[GR_N.dir()] = new OutputDir(GR_N, 1, 0, 1);
        // A -> C
        a1ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, 0, -1);
        a1ConArr[GR_P.dir()] = new OutputDir(GR_P, 0, 1, -1);
        a1ConArr[LB_P.dir()] = new OutputDir(LB_P, 1, 1, -1);
    }

    public static final OutputDir[] b0ConArr = new OutputDir[12];
    static {
        // A -> A
        b0ConArr[DB_P.dir()] = new OutputDir(DB_P, 1, 0, 0);
        b0ConArr[DB_N.dir()] = new OutputDir(DB_N, -1, 0, 0);
        b0ConArr[RE_P.dir()] = new OutputDir(RE_P, 0, 1, 0);
        b0ConArr[OR_P.dir()] = new OutputDir(OR_P, 1, 1, 0);
        b0ConArr[OR_N.dir()] = new OutputDir(OR_N, 0, -1, 0);
        b0ConArr[RE_N.dir()] = new OutputDir(RE_N, 1, -1, 0);
        // A -> B
        b0ConArr[LG_P.dir()] = new OutputDir(LG_P, 0, 1, 1);
        b0ConArr[LB_N.dir()] = new OutputDir(LB_N, 0, 0, 1);
        b0ConArr[GR_N.dir()] = new OutputDir(GR_N, 1, 0, 1);
        // A -> C
        b0ConArr[GR_P.dir()] = new OutputDir(GR_P, 0, 0, -1);
        b0ConArr[LB_P.dir()] = new OutputDir(LB_P, 1, 0, -1);
        b0ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, -1, -1);
    }

    public static final OutputDir[] b1ConArr = new OutputDir[12];
    static {
        // B -> B
        b1ConArr[DB_P.dir()] = new OutputDir(DB_P, 1, 0, 0);
        b1ConArr[DB_N.dir()] = new OutputDir(DB_N, -1, 0, 0);
        b1ConArr[OR_P.dir()] = new OutputDir(OR_P, 0, 1, 0);
        b1ConArr[RE_P.dir()] = new OutputDir(RE_P, -1, 1, 0);
        b1ConArr[RE_N.dir()] = new OutputDir(RE_N, 0, -1, 0);
        b1ConArr[OR_N.dir()] = new OutputDir(OR_N, -1, -1, 0);
        // B -> C
        b1ConArr[LG_P.dir()] = new OutputDir(LG_P, 0, 1, 1);
        b1ConArr[GR_N.dir()] = new OutputDir(GR_N, 0, 0, 1);
        b1ConArr[LB_N.dir()] = new OutputDir(LB_N, -1, 0, 1);
        // B -> A
        b1ConArr[LB_P.dir()] = new OutputDir(LB_P, 0, 0, -1);
        b1ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, -1, -1);
        b1ConArr[GR_P.dir()] = new OutputDir(GR_P, -1, 0, -1);
    }

    public static final OutputDir[] c0ConArr = new OutputDir[12];
    static {
        // C -> C
        c0ConArr[DB_P.dir()] = new OutputDir(DB_P, 1, 0, 0);
        c0ConArr[DB_N.dir()] = new OutputDir(DB_N, -1, 0, 0);
        c0ConArr[OR_P.dir()] = new OutputDir(OR_P, 0, 1, 0);
        c0ConArr[RE_P.dir()] = new OutputDir(RE_P, -1, 1, 0);
        c0ConArr[RE_N.dir()] = new OutputDir(RE_N, 0, -1, 0);
        c0ConArr[OR_N.dir()] = new OutputDir(OR_N, -1, -1, 0);
        // C -> A
        c0ConArr[LG_P.dir()] = new OutputDir(LG_P, 0, 0, 1);
        c0ConArr[LB_N.dir()] = new OutputDir(LB_N, -1, -1, 1);
        c0ConArr[GR_N.dir()] = new OutputDir(GR_N, 0, -1, 1);
        // C -> B
        c0ConArr[LB_P.dir()] = new OutputDir(LB_P, 0, 0, -1);
        c0ConArr[GR_P.dir()] = new OutputDir(GR_P, -1, 0, -1);
        c0ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, -1, -1);
    }

    public static final OutputDir[] c1ConArr = new OutputDir[12];
    static {
        // C -> C
        c1ConArr[DB_P.dir()] = new OutputDir(DB_P, 1, 0, 0);
        c1ConArr[DB_N.dir()] = new OutputDir(DB_N, -1, 0, 0);
        c1ConArr[RE_P.dir()] = new OutputDir(RE_P, 0, 1, 0);
        c1ConArr[OR_P.dir()] = new OutputDir(OR_P, 1, 1, 0);
        c1ConArr[OR_N.dir()] = new OutputDir(OR_N, 0, -1, 0);
        c1ConArr[RE_N.dir()] = new OutputDir(RE_N, 1, -1, 0);
        // C -> A
        c1ConArr[LG_P.dir()] = new OutputDir(LG_P, 0, 0, 1);
        c1ConArr[LB_N.dir()] = new OutputDir(LB_N, 0, -1, 1);
        c1ConArr[GR_N.dir()] = new OutputDir(GR_N, 1, -1, 1);
        // C -> B
        c1ConArr[GR_P.dir()] = new OutputDir(GR_P, 0, 0, -1);
        c1ConArr[LB_P.dir()] = new OutputDir(LB_P, 1, 0, -1);
        c1ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, -1, -1);
    }

    /**
     * For every direction [12] there are [5] neighbours a spin can rotate around for this direction.
     */
    public static final int[][] spinArr = new int[12][5];
    static {
        // DB_P
        spinArr[DB_P.dir()][0] = LG_N.dir();
        spinArr[DB_P.dir()][1] = LB_P.dir();
        spinArr[DB_P.dir()][2] = OR_P.dir();
        spinArr[DB_P.dir()][3] = LG_N.dir();
        spinArr[DB_P.dir()][4] = GR_N.dir();
        // OR_P
        spinArr[OR_P.dir()][0] = LB_P.dir();
        spinArr[OR_P.dir()][1] = GR_P.dir();
        spinArr[OR_P.dir()][2] = RE_P.dir();
        spinArr[OR_P.dir()][3] = LG_N.dir();
        spinArr[OR_P.dir()][4] = DB_P.dir();
    }

    /**
     * Rotation X-Axis.
     */
    public static final Cell.Dir[][] xRotArr = new Cell.Dir[3][4];
    static {
        // Left
        xRotArr[0][0] = LB_P;
        xRotArr[0][1] = LG_N;
        xRotArr[0][2] = RE_N;
        xRotArr[0][3] = DB_P;
        // Middle
        xRotArr[1][0] = OR_P;
        xRotArr[1][1] = GR_P;
        xRotArr[1][2] = OR_N;
        xRotArr[1][3] = GR_N;
        // Right
        xRotArr[2][0] = RE_P;
        xRotArr[2][1] = DB_N;
        xRotArr[2][2] = LB_N;
        xRotArr[2][3] = LG_P;
    }

    /**
     * rotation Y-Axis.
     */
    public static final Cell.Dir[][] yRotArr = new Cell.Dir[3][4];
    static {
        // Left
        yRotArr[0][0] = GR_P;
        yRotArr[0][1] = DB_N;
        yRotArr[0][2] = OR_N;
        yRotArr[0][3] = LG_N;
        // Middle
        yRotArr[1][0] = LB_P;
        yRotArr[1][1] = RE_P;
        yRotArr[1][2] = LB_N;
        yRotArr[1][3] = RE_N;
        // Right
        yRotArr[2][0] = OR_P;
        yRotArr[2][1] = LG_P;
        yRotArr[2][2] = GR_N;
        yRotArr[2][3] = DB_P;
    }

    /**
     * rotation Z-Axis.
     */
    public static final Cell.Dir[][] zRotArr = new Cell.Dir[3][4];
    static {
        // Left
        zRotArr[0][0] = RE_P;
        zRotArr[0][1] = GR_P;
        zRotArr[0][2] = LB_P;
        zRotArr[0][3] = OR_P;
        // Middle
        zRotArr[1][0] = LG_P;
        zRotArr[1][1] = DB_N;
        zRotArr[1][2] = LG_N;
        zRotArr[1][3] = DB_P;
        // Right
        zRotArr[2][0] = LB_N;
        zRotArr[2][1] = OR_N;
        zRotArr[2][2] = RE_N;
        zRotArr[2][3] = GR_N;
    }

    public static int calcXDirOffset(final int xPos, final int yPos, final int zPos, final Cell.Dir calcDir) {
        final OutputDir outputDir = calcDirOffset(xPos, yPos, zPos, calcDir);
        return xPos + outputDir.x;
    }

    public static int calcYDirOffset(final int xPos, final int yPos, final int zPos, final Cell.Dir calcDir) {
        final OutputDir outputDir = calcDirOffset(xPos, yPos, zPos, calcDir);
        return yPos + outputDir.y;
    }

    public static int calcZDirOffset(final int xPos, final int yPos, final int zPos, final Cell.Dir calcDir) {
        final OutputDir outputDir = calcDirOffset(xPos, yPos, zPos, calcDir);
        return zPos + outputDir.z;
    }

    public static OutputDir calcDirOffset(final int xPos, final int yPos, final int zPos, final Cell.Dir calcDir) {
        final OutputDir outputDir;
        switch (zPos % 3) {
            case 0 -> { // A
                switch (yPos % 2) {
                    case 0 -> { // 0
                        outputDir = a0ConArr[calcDir.dir()];
                    }
                    case 1, -1 -> { // 1
                        outputDir = a1ConArr[calcDir.dir()];
                    }
                    default -> throw new RuntimeException(format("Unexpected yPos \"%d\".", yPos));
                }
            }
            case 1, -1 -> { // B
                switch (yPos % 2) {
                    case 0 -> { // 0
                        outputDir = b0ConArr[calcDir.dir()];
                    }
                    case 1, -1 -> { // 1
                        outputDir = b1ConArr[calcDir.dir()];
                    }
                    default -> throw new RuntimeException(format("Unexpected yPos \"%d\".", yPos));
                }
            }
            case 2, -2 -> { // C
                switch (yPos % 2) {
                    case 0 -> { // 0
                        outputDir = c0ConArr[calcDir.dir()];
                    }
                    case 1, -1 -> { // 1
                        outputDir = c1ConArr[calcDir.dir()];
                    }
                    default -> throw new RuntimeException(format("Unexpected yPos \"%d\".", yPos));
                }
            }
            default -> throw new RuntimeException(format("Unexpected zPos \"%d\".", zPos));
        }
        return outputDir;
    }

    public static Cell.Dir calcOppositeDir(final Cell.Dir dir) {
        final Cell.Dir oppositeDir;
        switch (dir) {
            case DB_P -> oppositeDir = DB_N;
            case DB_N -> oppositeDir = DB_P;
            case OR_P -> oppositeDir = OR_N;
            case OR_N -> oppositeDir = OR_P;
            case RE_P -> oppositeDir = RE_N;
            case RE_N -> oppositeDir = RE_P;
            case LB_P -> oppositeDir = LB_N;
            case LB_N -> oppositeDir = LB_P;
            case GR_P -> oppositeDir = GR_N;
            case GR_N -> oppositeDir = GR_P;
            case LG_P -> oppositeDir = LG_N;
            case LG_N -> oppositeDir = LG_P;
            default -> throw new RuntimeException(format("Unexpected dir \"%s\".", dir));
        }
        return oppositeDir;
    }

}
