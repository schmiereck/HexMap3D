package de.schmiereck.hexMap3D.service.universe;

import java.util.stream.Stream;

public class Cell {
    /**
     *         (+)
     *     +---OR---+    (+)
     *     |   RE   |    LB
     *     |        |  +---GR---+
     * (+)LG    (+)DB  |   (+)  |
     *     |        |  |        |
     *     |   (-)  |  DB(-)    LG(-)
     *     +---GR---+  |        |
     *       LB(-)     |  RE    |
     *                 +---OR---+
     *                     (-)
     */
    public enum Dir {
        DB_P(0), // Dark-Blue 1
        DB_N(1), // Dark-Blue 2
        OR_P(2), // Orange 1
        OR_N(3), // Orange 2
        RE_P(4), // Red 1 (RD)
        RE_N(5), // Red 2 (RD)
        LB_P(6), // Light-Blue 1
        LB_N(7), // Light-Blue 2
        GR_P(8), // Green 1 (DG:Dark-Green)
        GR_N(9), // Green 2 (DG:Dark-Green)
        LG_P(10), // Light-Green 1
        LG_N(11); // Light-Green 2

        private final int dir;
        private Dir(final int dir) {
            this.dir = dir;
        }
        public int dir() {
            return this.dir;
        }
    };

    private CellState[] cellState = new CellState[2];
    private final Universe universe;

    public Cell(final Universe universe, final int xPos, final int yPos, final int zPos) {
        this.universe = universe;
        this.cellState[0] = CellStateService.createInitialCellState();
        this.cellState[1] = CellStateService.createInitialCellState();
    }

    public void setCellState(final CellState cellState) {
        this.cellState[this.universe.getNextCalcPos()] = cellState;
    }

    public CellState getCellState() {
        return this.cellState[this.universe.getActCalcPos()];
    }

    public void addWave(final Wave wave) {
        this.cellState[this.universe.getNextCalcPos()].addWave(wave);
    }

    public Wave searchWave(final Wave wave) {
        return this.cellState[this.universe.getNextCalcPos()].searchWave(wave);
    }

    public void clearWaveList() {
        //this.cellState[this.universe.getActCalcPos()].clearWaveList();
    }

    public Stream<Wave> getWaveListStream() {
        return this.cellState[this.universe.getActCalcPos()].getWaveListStream();
    }

}
