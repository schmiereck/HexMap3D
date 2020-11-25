package de.schmiereck.hexMap3D;

import de.schmiereck.hexMap3D.service.Cell;

import static de.schmiereck.hexMap3D.service.Cell.Dir.*;
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
        a0ConArr[GR_P.dir()] = new OutputDir(GR_P, 0, 0, 1);
        a0ConArr[LB_P.dir()] = new OutputDir(LB_P, -1, 0, 1);
        // A -> C
        a0ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, 0, -1);
        a0ConArr[LB_N.dir()] = new OutputDir(LB_N, 0, 1, -1);
        a0ConArr[GR_N.dir()] = new OutputDir(GR_N, -1, 1, -1);
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
        a1ConArr[LB_P.dir()] = new OutputDir(LB_P, 0, 0, 1);
        a1ConArr[GR_P.dir()] = new OutputDir(GR_P, 1, 0, 1);
        // A -> C
        a1ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, 0, -1);
        a1ConArr[GR_N.dir()] = new OutputDir(GR_N, 0, 1, -1);
        a1ConArr[LB_N.dir()] = new OutputDir(LB_N, 1, 1, -1);
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
        b0ConArr[LB_P.dir()] = new OutputDir(LB_P, 0, 0, 1);
        b0ConArr[GR_P.dir()] = new OutputDir(GR_P, 1, 0, 1);
        // A -> C
        b0ConArr[GR_N.dir()] = new OutputDir(GR_N, 0, 0, -1);
        b0ConArr[LB_N.dir()] = new OutputDir(LB_N, 1, 0, -1);
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
        b1ConArr[GR_P.dir()] = new OutputDir(GR_P, 0, 0, 1);
        b1ConArr[LB_P.dir()] = new OutputDir(LB_P, -1, 0, 1);
        // B -> A
        b1ConArr[LB_N.dir()] = new OutputDir(LB_N, 0, 0, -1);
        b1ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, -1, -1);
        b1ConArr[GR_N.dir()] = new OutputDir(GR_N, -1, 0, -1);
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
        c0ConArr[LB_P.dir()] = new OutputDir(LB_P, -1, -1, 1);
        c0ConArr[GR_P.dir()] = new OutputDir(GR_P, 0, -1, 1);
        // C -> B
        c0ConArr[LB_N.dir()] = new OutputDir(LB_N, 0, 0, -1);
        c0ConArr[GR_N.dir()] = new OutputDir(GR_N, -1, 0, -1);
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
        c1ConArr[LB_P.dir()] = new OutputDir(LB_P, 0, -1, 1);
        c1ConArr[GR_P.dir()] = new OutputDir(GR_P, 1, -1, 1);
        // C -> B
        c1ConArr[GR_N.dir()] = new OutputDir(GR_N, 0, 0, -1);
        c1ConArr[LB_N.dir()] = new OutputDir(LB_N, 1, 0, -1);
        c1ConArr[LG_N.dir()] = new OutputDir(LG_N, 0, -1, -1);
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
}
