package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.service.universe.WaveMoveCalcService;
import de.schmiereck.hexMap3D.service.universe.WaveMoveDirService;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;
import static de.schmiereck.hexMap3D.view.GridViewUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
    ====*=======*=======*=======*=======*===
       / \     / \     / \     / \     / \
      /   \   /   \   /   \   /   \   /   \
     /     \ /     \ /     \ /     \ /     \
    *=======*=======*=======*=======*=======6=======*=======*===
     \     / \     / \     / \     / \     / \     / \     / \
      \   /   \   /   \   /   \   /   \   /   \   /   \   /   \
       \ /     \ /     \ /     \ /     \ /     \ /     \ /     \
    ====*=======*=======3=======4=======5=======*=======*=======
       / \     / \     / \     / \     / \
      /   \   /   \   /   \   /   \   /   \
     /     \ /     \ /     \ /     \ /     \
    0=======1=======2=======*=======*=======
            1	1.5	2	2.5	3	3.5		4.5	5

    1: dirCalcPos:0, moveDirProbSumArr:[4, 0], pos:(1.0, 0.0)
    2: dirCalcPos:0, moveDirProbSumArr:[4, 2], pos:(2.0, 0.0)
    3: dirCalcPos:1, moveDirProbSumArr:[0, 4], pos:(2.5, 0.8660254037844386)
    4: dirCalcPos:0, moveDirProbSumArr:[4, 0], pos:(3.5, 0.8660254037844386)
    5: dirCalcPos:0, moveDirProbSumArr:[4, 2], pos:(4.5, 0.8660254037844386)
    6: dirCalcPos:1, moveDirProbSumArr:[0, 4], pos:(5.0, 1.7320508075688772)
    targetPos:(5.692099788303082, 1.8973665961010273

    ====*=======*=======*=======*=======6===
       / \     / \     / \     / \     / \
      /   \   /   \   /   \   /   \   /   \
     /     \ /     \ /     \ /     \ /     \
    *=======*=======*=======4=======5=======*=======*=======*===
     \     / \     / \     / \     / \     / \     / \     / \
      \   /   \   /   \   /   \   /   \   /   \   /   \   /   \
       \ /     \ /     \ /     \ /     \ /     \ /     \ /     \
    ====*=======2=======3=======*=======*=======*=======*=======
       / \     / \     / \     / \     / \
      /   \   /   \   /   \   /   \   /   \
     /     \ /     \ /     \ /     \ /     \
    0=======1=======*=======*=======*=======
            1	1.5	2	2.5	3	3.5		4.5	5

    dirCalcPos:0, moveDirProbSumArr:[2, 0], pos:(1.0, 0.0)
    dirCalcPos:1, moveDirProbSumArr:[0, 2], pos:(1.5, 0.8660254037844386)
    dirCalcPos:0, moveDirProbSumArr:[2, 0], pos:(2.5, 0.8660254037844386)
    dirCalcPos:1, moveDirProbSumArr:[0, 2], pos:(3.0, 1.7320508075688772)
    dirCalcPos:0, moveDirProbSumArr:[2, 0], pos:(4.0, 1.7320508075688772)
    dirCalcPos:1, moveDirProbSumArr:[0, 2], pos:(4.5, 2.598076211353316)
    line pos:(4.5, 2.598076211353316)
    targetPos:(4.5, 2.5980762113533165)
 */
public class ProbLineTest {
    private static final double FA = (2.0D / 3.0D);
    private static final double PI = Math.PI;
    private static final double PI2 = PI/2.0D;
    private static final double PI_60 =PI2*FA;
    private static final double PI_90 =PI2;
    private static final double PI_150 =PI2+PI_60;

    @org.junit.jupiter.api.Test
    public void testAllTo60Deg() {
        final int probSum = 10;
        final int lineLength = 10;
        final int rotateSteps = 10;
        for (double deg = 0.0D; deg <= PI_60; deg += (PI_60)/rotateSteps) {
            double p1 = (deg * probSum);
            double p2 = p1 / PI_60;
            int prob = (int)Math.round(p2);
            int xProb = probSum - prob;
            int yProb = prob;
            final int[] moveDirProbArr = new int[2];
            moveDirProbArr[0] = (xProb); // x
            moveDirProbArr[1] = (yProb); // y

            double[][] lineArr = drawLine(lineLength, moveDirProbArr, false);

            double xTargetPos = lineLength * Math.cos(deg);
            double yTargetPos = lineLength * Math.sin(deg);

            System.out.print(String.format("prob:(%d, %d)", xProb, yProb));
            System.out.print(String.format(" -\ttargetPos:(%.2f, %.2f)", xTargetPos, yTargetPos));
            System.out.print(String.format(" -\tline pos:(%.2f, %.2f)", lineArr[lineLength - 1][0], lineArr[lineLength - 1][1]));
            final double xDiff = xTargetPos - lineArr[lineLength - 1][0];
            final double yDiff = yTargetPos - lineArr[lineLength - 1][1];
            final double diff = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
            System.out.print(String.format(" -\tdiff:%.2f", diff));
            System.out.println();
            assertEquals(xTargetPos, lineArr[lineLength - 1][0], 1.75D);
            assertEquals(yTargetPos, lineArr[lineLength - 1][1], 1.75D);
        }
    }

    @org.junit.jupiter.api.Test
    public void testAllTo90Deg() {
        final int probSum = 200;
        final int lineLength = 10;
        final int rotateSteps = 10;
        final int probDiff = 30;//probSum / rotateSteps;
        final int[] moveDirProbArr = new int[3];
        moveDirProbArr[0] = probSum;
        for (double deg = 0.0D; deg <= PI_90; deg += (PI_90)/rotateSteps) {
            for (int pPos = 0; pPos < moveDirProbArr.length; pPos++) {
                if (moveDirProbArr[pPos] > 0) {
                    final int d;
                        if (moveDirProbArr[pPos] >= probDiff) {
                            d = probDiff;
                        } else {
                            d = moveDirProbArr[pPos];
                        }
                    moveDirProbArr[pPos] -= d;
                    moveDirProbArr[wrap(pPos + 1, moveDirProbArr.length)] += d;
                    break;
                }
            }

            double[][] lineArr = drawLine(lineLength, moveDirProbArr, false);

            double xTargetPos = lineLength * Math.cos(deg);
            double yTargetPos = lineLength * Math.sin(deg);

            System.out.print(String.format("prob:(%s)", Arrays.toString(moveDirProbArr)));
            System.out.print(String.format(" -\ttargetPos:(%.2f, %.2f)", xTargetPos, yTargetPos));
            System.out.print(String.format(" -\tline pos:(%.2f, %.2f)", lineArr[lineLength - 1][0], lineArr[lineLength - 1][1]));
            final double xDiff = xTargetPos - lineArr[lineLength - 1][0];
            final double yDiff = yTargetPos - lineArr[lineLength - 1][1];
            final double diff = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
            System.out.print(String.format(" -\tdiff:%.2f", diff));
            System.out.println();
            assertEquals(xTargetPos, lineArr[lineLength - 1][0], 2.75D);
            assertEquals(yTargetPos, lineArr[lineLength - 1][1], 2.75D);
        }
    }

    @org.junit.jupiter.api.Test
    public void testAllTo150Deg() {
        final int probSum = 10;
        final int lineLength = 10;
        final int rotateSteps = 10;
        final int probDiff = 2;//probSum / rotateSteps;
        final int[] moveDirProbArr = new int[3];
        moveDirProbArr[0] = probSum;
        for (double deg = 0.0D; deg <= PI_150; deg += (PI_150)/rotateSteps) {
            for (int pPos = 0; pPos < moveDirProbArr.length; pPos++) {
                if (moveDirProbArr[pPos] > 0) {
                    final int d;
                        if (moveDirProbArr[pPos] >= probDiff) {
                            d = probDiff;
                        } else {
                            d = moveDirProbArr[pPos];
                        }
                    moveDirProbArr[pPos] -= d;
                    moveDirProbArr[wrap(pPos + 1, moveDirProbArr.length)] += d;
                    break;
                }
            }

            double[][] lineArr = drawLine(lineLength, moveDirProbArr, false);

            double xTargetPos = lineLength * Math.cos(deg);
            double yTargetPos = lineLength * Math.sin(deg);

            System.out.print(String.format("prob:(%s)", Arrays.toString(moveDirProbArr)));
            System.out.print(String.format(" -\ttargetPos:(%.2f, %.2f)", xTargetPos, yTargetPos));
            System.out.print(String.format(" -\tline pos:(%.2f, %.2f)", lineArr[lineLength - 1][0], lineArr[lineLength - 1][1]));
            final double xDiff = xTargetPos - lineArr[lineLength - 1][0];
            final double yDiff = yTargetPos - lineArr[lineLength - 1][1];
            final double diff = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
            System.out.print(String.format(" -\tdiff:%.2f", diff));
            System.out.println();
            assertEquals(xTargetPos, lineArr[lineLength - 1][0], 2.75D);
            assertEquals(yTargetPos, lineArr[lineLength - 1][1], 2.75D);
        }
    }

    @org.junit.jupiter.api.Test
    public void test0Deg() {
        int xProb = 9;
        int yProb = 0;
        int lineLength = 6;
        final int[] moveDirProbArr = new int[2];
        moveDirProbArr[0] = (xProb); // x
        moveDirProbArr[1] = (yProb); // y
        double[][] lineArr = drawLine(lineLength, moveDirProbArr, false);

        double l = lineLength;
        double m = (((double)yProb) / ((double)xProb)) * (2.0D/3.0D); // 90° = 60°
        //double xTargetPos = (l / Math.sqrt(1.0D + m * m));
        //double yTargetPos = (l * (m / Math.sqrt(1.0D + m * m)));
        double xTargetPos = l * Math.cos(m*Math.PI/4);
        double yTargetPos = l*triangleHeight * Math.sin(m*Math.PI/4);

        System.out.println(String.format("line pos:(%.2f, %.2f)", lineArr[lineLength-1][0], lineArr[lineLength-1][1]));
        System.out.println(String.format("targetPos:(%.2f, %.2f)", xTargetPos, yTargetPos));
        assertEquals(xTargetPos, lineArr[lineLength-1][0], 0.01D);
        assertEquals(yTargetPos, lineArr[lineLength-1][1], 0.01D);
    }

    @org.junit.jupiter.api.Test
    public void test30Deg() {
        int xProb = 9;
        int yProb = 3;
        int lineLength = 10;
        final int[] moveDirProbArr = new int[2];
        moveDirProbArr[0] = (xProb); // x
        moveDirProbArr[1] = (yProb); // y
        double[][] lineArr = drawLine(lineLength, moveDirProbArr, false);

        double l = lineLength;
        //double m = 0.1666D * (2.0D/3.0D);//1.0D/4.0D;//Math.tan((45.0D/360.0D)*Math.PI*2) * (2.0D/3.0D);
        double m = (((double)yProb) / ((double)xProb)) * (2.0D/3.0D);//1.0D/4.0D;//Math.tan((45.0D/360.0D)*Math.PI*2) * (2.0D/3.0D);
        System.out.println(String.format("m:0(%.2f)", Math.tan((0.0D/360.0D)*Math.PI*2)));
        System.out.println(String.format("m:22,5(%.2f)", Math.tan((22.5D/360.0D)*Math.PI*2)));
        System.out.println(String.format("m:30(%.2f)", Math.tan((30.0D/360.0D)*Math.PI*2)));
        System.out.println(String.format("m:45(%.2f)", Math.tan((45.0D/360.0D)*Math.PI*2)));
        System.out.println(String.format("m:90(%.2f)", Math.tan((90.0D/360.0D)*Math.PI*2)));
        double xm = (((double)yProb) / ((double)xProb)) * (2.0D/3.0D); // 90° = 60°
        double ym = (((double)yProb) / ((double)xProb)); // 90° = 60°
        //double xTargetPos = l * Math.cos(m*Math.PI/2);
        double xTargetPos = l * Math.cos(m*Math.PI/4);
        double yTargetPos = l*triangleHeight * Math.sin(m*Math.PI/4);
        //double yTargetPos = l * Math.sin(m*Math.PI/2);

        System.out.println(String.format("line pos:(%.2f, %.2f)", lineArr[lineLength-1][0], lineArr[lineLength-1][1]));
        System.out.println(String.format("targetPos:(%.2f, %.2f)", xTargetPos, yTargetPos));
        assertEquals(xTargetPos, lineArr[lineLength-1][0], 0.01D);
        assertEquals(yTargetPos, lineArr[lineLength-1][1], 0.01D);
    }

    @org.junit.jupiter.api.Test
    public void test40Deg() {
        int xProb = 9;
        int yProb = 4;
        int lineLength = 10;
        final int[] moveDirProbArr = new int[2];
        moveDirProbArr[0] = (xProb); // x
        moveDirProbArr[1] = (yProb); // y
        double[][] lineArr = drawLine(lineLength, moveDirProbArr, false);

        double l = lineLength;
        //double m = 0.1666D * (2.0D/3.0D);//1.0D/4.0D;//Math.tan((45.0D/360.0D)*Math.PI*2) * (2.0D/3.0D);
        double m = (((double)yProb) / ((double)xProb)) * (2.0D/3.0D);//1.0D/4.0D;//Math.tan((45.0D/360.0D)*Math.PI*2) * (2.0D/3.0D);
        System.out.println(String.format("m:0(%.2f)", Math.tan((0.0D/360.0D)*Math.PI*2)));
        System.out.println(String.format("m:22,5(%.2f)", Math.tan((22.5D/360.0D)*Math.PI*2)));
        System.out.println(String.format("m:30(%.2f)", Math.tan((30.0D/360.0D)*Math.PI*2)));
        System.out.println(String.format("m:45(%.2f)", Math.tan((45.0D/360.0D)*Math.PI*2)));
        System.out.println(String.format("m:90(%.2f)", Math.tan((90.0D/360.0D)*Math.PI*2)));
        double xm = (((double)yProb) / ((double)xProb)) * (2.0D/3.0D); // 90° = 60°
        double ym = (((double)yProb) / ((double)xProb)); // 90° = 60°
        //double xTargetPos = l * Math.cos(m*Math.PI/2);
        double xTargetPos = l * Math.cos(m*Math.PI/4);
        double yTargetPos = l*triangleHeight * Math.sin(m*Math.PI/4);
        //double yTargetPos = l * Math.sin(m*Math.PI/2);

        System.out.println(String.format("line pos:(%.2f, %.2f)", lineArr[lineLength-1][0], lineArr[lineLength-1][1]));
        System.out.println(String.format("targetPos:(%.2f, %.2f)", xTargetPos, yTargetPos));
        assertEquals(xTargetPos, lineArr[lineLength-1][0], 0.01D);
        assertEquals(yTargetPos, lineArr[lineLength-1][1], 0.01D);
    }

    @org.junit.jupiter.api.Test
    public void test45Deg() {
        int xProb = 2;
        int yProb = 2;
        int lineLength = 6;
        final int[] moveDirProbArr = new int[2];
        moveDirProbArr[0] = (xProb); // x
        moveDirProbArr[1] = (yProb); // y
        double[][] lineArr = drawLine(lineLength, moveDirProbArr, false);

        double l = lineLength;
        double m = (((double)yProb) / ((double)xProb)) * (2.0D/3.0D); // 90° = 60°
        double xTargetPos = l*triangleHeight * Math.cos(m*Math.PI/4);
        double yTargetPos = l*triangleHeight * Math.sin(m*Math.PI/4);

        System.out.println(String.format("line pos:(%.2f, %.2f)", lineArr[lineLength-1][0], lineArr[lineLength-1][1]));
        System.out.println(String.format("targetPos:(%.2f, %.2f)", xTargetPos, yTargetPos));
        assertEquals(xTargetPos, lineArr[lineLength-1][0], 0.01D);
        assertEquals(yTargetPos, lineArr[lineLength-1][1], 0.01D);
    }

    @org.junit.jupiter.api.Test
    public void test90Deg() {
        int xProb = 0;
        int yProb = 1;
        int lineLength = 6;
        final int[] moveDirProbArr = new int[2];
        moveDirProbArr[0] = (xProb); // x
        moveDirProbArr[1] = (yProb); // y
        double[][] lineArr = drawLine(lineLength, moveDirProbArr, false);

        double l = lineLength;
        double xm = 2.0D * (2.0D/3.0D);//(((double)yProb) / ((double)xProb)) * (2.0D/3.0D); // 90° = 60°
        double ym = 1.0D;//(((double)yProb) / ((double)xProb)) * (2.0D/3.0D); // 90° = 60°
        double xTargetPos = l * Math.cos(xm*Math.PI/4);
        double yTargetPos = l * Math.sin(xm*Math.PI/4);

        System.out.println(String.format("line pos:(%.2f, %.2f)", lineArr[lineLength-1][0], lineArr[lineLength-1][1]));
        System.out.println(String.format("targetPos:(%.2f, %.2f)", xTargetPos, yTargetPos));
        assertEquals(xTargetPos, lineArr[lineLength-1][0], 0.01D);
        assertEquals(yTargetPos, lineArr[lineLength-1][1], 0.01D);
    }

    private double[][] drawLine(int lineLength, final int[] moveDirProbArr, final boolean showLog) {
        double xPos = 0;
        double yPos = 0;
        double[][] lineArr = new double[lineLength][2];
        int[] moveDirProbSumArr = new int[moveDirProbArr.length];
        int dirCalcPos = -1;

        // de.schmiereck.hexMap3D.service.universe.WaveMoveDirService.adjustMaxProb
        int maxProb = WaveMoveDirService.calcMaxProb(moveDirProbArr);

        for (int testPos = 0; testPos < lineLength; testPos++) {
            // de.schmiereck.hexMap3D.service.universe.WaveMoveCalcService.calcActualWaveMoveCalcDir
            dirCalcPos = WaveMoveCalcService.calcWaveMoveCalcDir(moveDirProbArr, maxProb, moveDirProbSumArr, dirCalcPos);
            /*
            int startDirCalcCount = 0;
            do {
                dirCalcPos = wrap(dirCalcPos + 1, moveDirProbArr.length);
                moveDirProbSumArr[dirCalcPos] += moveDirProbArr[dirCalcPos];
                if (startDirCalcCount >= moveDirProbArr.length) {
                    throw new RuntimeException("Do not found next dirCalcPos: " + Arrays.toString(moveDirProbArr));
                }
                startDirCalcCount++;
            } while (moveDirProbSumArr[dirCalcPos] < maxProb);
            */
            switch (dirCalcPos) {
                case 0 -> {
                    xPos += triangleWidth;
                    yPos += 0.0D;
                }
                case 1 -> {
                    xPos += triangleWidth2;
                    yPos += triangleHeight;
                }
                case 2 -> {
                    xPos -= triangleWidth2;
                    yPos += triangleHeight;
                }
            }
            lineArr[testPos][0] = xPos;
            lineArr[testPos][1] = yPos;
            if (showLog)
                System.out.println("dirCalcPos:" + dirCalcPos + ", " +
                        "moveDirProbSumArr:" + Arrays.toString(moveDirProbSumArr) + ", " +
                        "pos:(" + xPos + ", " + yPos + ")");

            // de.schmiereck.hexMap3D.service.universe.WaveMoveCalcService.calcDirMoved
            WaveMoveCalcService.calcDirMoved(moveDirProbSumArr, dirCalcPos, maxProb);
        }
        return lineArr;
    }
}
