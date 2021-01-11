package de.schmiereck.hexMap3D.service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell {
    /**        (+)
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

    private Cell[] nextCellArr;
    private final Queue<Wave>[] waveList;
    private final Universe universe;

    public Cell(final Universe universe, final int xPos, final int yPos, final int zPos) {
        this.universe = universe;
        this.waveList = IntStream.rangeClosed(0, 1).mapToObj(pos -> new LinkedList<Wave>()).toArray(LinkedList[]::new);
    }

    public void populateNeigbours() {
    }

    public Cell(final Universe universe) {
        this.universe = universe;
        this.waveList = IntStream.rangeClosed(0, 1).mapToObj(pos -> new LinkedList<Wave>()).toArray(LinkedList[]::new);
    }

    public void init(final Cell[] nextCellArr) {
        this.nextCellArr = nextCellArr;
    }

    public void addWave(final Wave wave) {
        this.waveList[this.universe.getNextCalcPos()].add(wave);
        wave.setCell(this);
    }

    public Cell getNextCell(final Dir dir) {
        return this.nextCellArr[dir.ordinal()];
    }

    public void clearWaveList() {
        this.waveList[this.universe.getActCalcPos()].stream().forEach(wave -> wave.getEvent().removeWave(wave));
        this.waveList[this.universe.getActCalcPos()].clear();
    }

    public int getWaveListSize() {
        return this.waveList[this.universe.getActCalcPos()].size();
    }

    public Stream<Wave> getWaveListStream() {
        return this.waveList[this.universe.getActCalcPos()].stream();
    }

    public boolean haveFirstWave() {
        return !this.waveList[this.universe.getActCalcPos()].isEmpty();
    }

    public Wave removeFirstWave() {
        return this.waveList[this.universe.getActCalcPos()].remove();
    }

    public Wave fetchFirstWave() {
        return this.waveList[this.universe.getActCalcPos()].peek();
    }
}
