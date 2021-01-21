package de.schmiereck.hexMap3D.service;

import java.util.HashMap;
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

    private final HashMap<Wave, Wave>[] waveHashArr = new HashMap[2];
    private final Universe universe;

    public Cell(final Universe universe, final int xPos, final int yPos, final int zPos) {
        this.universe = universe;
        this.waveHashArr[0] = new HashMap<>();
        this.waveHashArr[1] = new HashMap<>();
    }

    public void addWave(final Wave wave) {
        this.waveHashArr[this.universe.getNextCalcPos()].put(wave, wave);
    }

    public Wave searchWave(final Wave wave) {
        return this.waveHashArr[this.universe.getNextCalcPos()].get(wave);
    }

    public void clearWaveList() {
        //this.waveHashArr[this.universe.getActCalcPos()].values().stream().forEach(wave -> wave.getEvent().removeWave(wave));
        this.waveHashArr[this.universe.getActCalcPos()].clear();
    }

    public Stream<Wave> getWaveListStream() {
        return this.waveHashArr[this.universe.getActCalcPos()].values().stream();
    }

}
