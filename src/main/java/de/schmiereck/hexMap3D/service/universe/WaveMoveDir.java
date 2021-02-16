package de.schmiereck.hexMap3D.service.universe;

import java.util.Arrays;
import java.util.Objects;

/**
 * Direction of a move in a Kuboktaeder
 * representet by the probability of movin from his middle in the directions to his edges.
 */
public class WaveMoveDir {
    public static final int MAX_DIR_PROB = 100;
    public static final int MAX_DIR_PROB1_2 = MAX_DIR_PROB / 2;
    public static final int MAX_DIR_PROB1_4 = MAX_DIR_PROB1_2 / 2;
    public static final int MAX_DIR_PROB1_8 = MAX_DIR_PROB1_4 / 2;
    public static final int MAX_DIR_PROB3_4 = MAX_DIR_PROB - MAX_DIR_PROB1_4;
    public static final int MAX_DIR_PROB7_8 = MAX_DIR_PROB - MAX_DIR_PROB1_8;

    /**
     * Possible Directions.
     * The dir number of {@link Cell.Dir} is the index.
     */
    private int[] moveDirProbArr;
    private int maxProb = 0;

    public WaveMoveDir() {
        this.moveDirProbArr = new int[Cell.Dir.values().length];
        //Arrays.stream(Cell.Dir.values()).forEach(dir -> this.moveDirProbArr[dir.dir()] = 0);
        this.maxProb = 0;
    }

    public WaveMoveDir(final int[] moveDirProbArr, final int maxProb) {
        this.moveDirProbArr = moveDirProbArr;
        this.maxProb = maxProb;
    }

    public void setDirMoveProb(final Cell.Dir dir, final int dirCalcProb) {
        //this.moveCalcDirArr[dirNo].setDir(dir);
        this.moveDirProbArr[dir.dir()] = dirCalcProb;
    }

    public void addDirMoveProb(final Cell.Dir dir, final int dirCalcProb) {
        //this.moveCalcDirArr[dirNo].setDir(dir);
        this.moveDirProbArr[dir.dir()] += dirCalcProb;
    }

    public int[] getMoveDirProbArr() {
        return this.moveDirProbArr;
    }

    public int getDirMoveProb(final Cell.Dir dir) {
        return this.moveDirProbArr[dir.dir()];
    }

    public int getDirMoveProb(final int dirPos) {
        return this.moveDirProbArr[dirPos];
    }

    public int getWaveMoveDirProb(final int dirCalcPos) {
        return this.moveDirProbArr[dirCalcPos];
    }

    public int getMaxProb() {
        return this.maxProb;
    }

    public void setMaxProb(final int maxProb) {
        this.maxProb = maxProb;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +  Arrays.toString(this.moveDirProbArr) + "}";
    }

    private int hashCode = 0;
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = Objects.hash(this.maxProb, Arrays.hashCode(this.moveDirProbArr));
        }
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveDir entry = (WaveMoveDir) obj;
        return (this.maxProb == entry.maxProb) &&
                Arrays.equals(this.moveDirProbArr, entry.moveDirProbArr);
    }
}
