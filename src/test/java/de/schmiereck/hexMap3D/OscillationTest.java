package de.schmiereck.hexMap3D;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

public class OscillationTest {

    class Cell {
        int f;
        int e;
        int m;

        public void set(final int f, final int e, final int m) {
            this.f = f;
            this.e = e;
            this.m = m;
        }
    }

    @org.junit.jupiter.api.Test
    public void testSimpleOscillation() {
        final Cell[][] cellArr = new Cell[2][8];
        Arrays.setAll(cellArr[0], pos -> new Cell());
        Arrays.setAll(cellArr[1], pos -> new Cell());

        int autoCalcPos = 0;
        int nextAutoCalcPos = 1;
        final int maxAmplitude = 3;

        cellArr[autoCalcPos][4].f = 1;
        cellArr[autoCalcPos][4].e = 1;
        cellArr[autoCalcPos][4].m = 0;

        printCellArr(cellArr[autoCalcPos]);

        for (int runNr = 0; runNr < 12; runNr++) {
            for (int cellPos = 0; cellPos < cellArr[autoCalcPos].length; cellPos++) {
                final Cell cell = cellArr[autoCalcPos][cellPos];
                final Cell nextCell = cellArr[nextAutoCalcPos][cellPos];
                final Cell in1Cell = cellArr[autoCalcPos][wrap(cellPos + 1, cellArr[autoCalcPos].length)];
                final Cell in2Cell = cellArr[autoCalcPos][wrap(cellPos - 1, cellArr[autoCalcPos].length)];
                final int f = cell.f;
                final int ie = cell.e;//in1Cell.e + in2Cell.e;
                final int im = cell.m;//in1Cell.m + in2Cell.m;

                calcNextCell(nextCell, f, ie, im, maxAmplitude);
            }

            printCellArr(cellArr[nextAutoCalcPos]);

            final int lastAutoCalcPos = autoCalcPos;
            autoCalcPos = nextAutoCalcPos;
            nextAutoCalcPos = lastAutoCalcPos;
        }
    }

    @org.junit.jupiter.api.Test
    public void testDistributedOscillation() {
        final Cell[][] cellArr = new Cell[2][24];
        Arrays.setAll(cellArr[0], pos -> new Cell());
        Arrays.setAll(cellArr[1], pos -> new Cell());

        int autoCalcPos = 0;
        int nextAutoCalcPos = 1;
        final int maxAmplitude = 3;

        cellArr[autoCalcPos][12].f = 1;
        cellArr[autoCalcPos][12].e = 1;
        cellArr[autoCalcPos][12].m = 0;

        printCellArr(cellArr[autoCalcPos]);

        for (int runNr = 0; runNr < 24; runNr++) {
            for (int cellPos = 0; cellPos < cellArr[autoCalcPos].length; cellPos++) {
                final Cell cell = cellArr[autoCalcPos][cellPos];
                final Cell nextCell = cellArr[nextAutoCalcPos][cellPos];
                final Cell in1Cell = cellArr[autoCalcPos][wrap(cellPos + 1, cellArr[autoCalcPos].length)];
                final Cell in2Cell = cellArr[autoCalcPos][wrap(cellPos - 1, cellArr[autoCalcPos].length)];
                final int ie;
                final int im;
                final int f;
                if (cell.f == 0) {
                    if (in1Cell.f != 0) {
                        f = in1Cell.f;
                        ie = in1Cell.e;
                        im = in1Cell.m;
                    } else {
                        if (in2Cell.f != 0) {
                            f = in2Cell.f;
                            ie = in2Cell.e;
                            im = in2Cell.m;
                        } else {
                            f = cell.f;
                            ie = cell.e;
                            im = cell.m;
                        }
                    }
                    nextCell.f = f;
                    nextCell.e = ie;
                    nextCell.m = im;
                } else {
                    f = cell.f;
                    ie = cell.e;
                    im = cell.m;
                    calcNextCell(nextCell, f, ie, im, maxAmplitude);
                }
            }

            printCellArr(cellArr[nextAutoCalcPos]);

            final int lastAutoCalcPos = autoCalcPos;
            autoCalcPos = nextAutoCalcPos;
            nextAutoCalcPos = lastAutoCalcPos;
        }
    }

    private void calcNextCell(final Cell nextCell, final int f, final int ie, final int im, final int maxAmplitude) {
        final int diffe;
        final int diffm;
        final int e;
        final int m;

        if (ie > im) {
            if (ie < maxAmplitude) {
                diffe = 1;
            } else {
                diffe = -1;//im - ie;
            }
        } else {
            if (ie < im) {
                if (ie > -maxAmplitude) {
                    diffe = -1;
                } else {
                    diffe = 1;//ie - im;
                }
            } else {
                diffe = 0;
            }
        }

        if (im < ie) {
            if (im < maxAmplitude) {
                diffm = 1;
            } else {
                diffm = -1;
            }
        } else {
            if (im > ie) {
                if (im > -maxAmplitude) {
                    diffm = -1;
                } else {
                    diffm = 1;
                }
            } else {
                diffm = 0;
            }
        }

        e = ie + diffe;
        m = im + diffm;

        nextCell.set(f, e, m);
    }

    private static void printCellArr(final Cell[] cellArr) {
        System.out.print("|");
        for (final Cell cell : cellArr) {
            System.out.printf("%2d / %2d | ", cell.e, cell.m);
        }
        System.out.print("\n");
    }
}
