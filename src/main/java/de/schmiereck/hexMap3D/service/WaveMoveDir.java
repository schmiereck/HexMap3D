package de.schmiereck.hexMap3D.service;

import java.util.Arrays;
import java.util.Objects;

/**
 * Direction of a move in a Kuboktaeder
 * representet by the probability of movin from his middle in the directions to his edges.
 */
public class WaveMoveDir {
    /**
     * Possible Directions.
     * The dir number of {@link Cell.Dir} is the index.
     */
    private WaveMoveDirProb[] moveDirProbArr;
    private int maxProb = 0;

    public WaveMoveDir() {
        this.moveDirProbArr = new WaveMoveDirProb[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> this.moveDirProbArr[dir.dir()] = new WaveMoveDirProb(0));
        this.maxProb = 0;
    }

    public WaveMoveDir(final WaveMoveDirProb[] moveDirProbArr, final int maxProb) {
        this.moveDirProbArr = moveDirProbArr;
        this.maxProb = maxProb;
    }

    public void setDirMoveProb(final Cell.Dir dir, final int dirCalcProb) {
        //this.moveCalcDirArr[dirNo].setDir(dir);
        this.moveDirProbArr[dir.dir()].setDirMoveProb(dirCalcProb);
    }

    public WaveMoveDirProb[] getMoveDirProbArr() {
        return this.moveDirProbArr;
    }

    public WaveMoveDirProb getDirMoveProb(final Cell.Dir dir) {
        return this.moveDirProbArr[dir.dir()];
    }

    public WaveMoveDirProb getDirMoveProb(final int dirPos) {
        return this.moveDirProbArr[dirPos];
    }

    public WaveMoveDirProb getWaveMoveDirProb(final int dirCalcPos) {
        return this.moveDirProbArr[dirCalcPos];
    }

    public void adjustMaxProb() {
        this.maxProb = 0;
        for (int pos = 0; pos < this.moveDirProbArr.length; pos++) {
            if (this.moveDirProbArr[pos].getDirMoveProb() > maxProb) {
                this.maxProb = this.moveDirProbArr[pos].getDirMoveProb();
            }
//            if (this.moveCalcDirArr[pos].getDirCalcPropSum() > this.moveCalcDirArr[pos].getDirCalcProp()) {
//                this.moveCalcDirArr[pos].setDirCalcPropSum(this.moveCalcDirArr[pos].getDirCalcProp());
//            }
//            if (this.moveCalcDirArr[pos].getDirCalcProp() == 0) {
//                this.moveCalcDirArr[pos].setDirCalcPropSum(0);
//            }
        }
        /*
        int propPos = -1;
        for (int pos = 0; pos < this.moveCalcDirArr.length; pos++) {
            final int dirPos = wrap(this.dirCalcPos + pos, this.moveCalcDirArr.length);
            final int prop = moveCalcDirArr[dirPos].getDirCalcProp();
            if (prop > 0) {
                final int propSum = DIR_CALC_MAX_PROP - (propPos * prop);
                if (propSum > prop) {
                    this.moveCalcDirArr[dirPos].setDirCalcPropSum(propSum + prop);
                } else {
                    this.moveCalcDirArr[dirPos].setDirCalcPropSum(prop);
                }
                propPos++;
            }
        }
        */
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

    @Override
    public int hashCode() {
        return Objects.hash(this.maxProb, Arrays.hashCode(this.moveDirProbArr));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveDir entry = (WaveMoveDir) obj;
        return this.maxProb == entry.maxProb &&
                (Arrays.equals(this.moveDirProbArr, entry.moveDirProbArr));
    }
}
