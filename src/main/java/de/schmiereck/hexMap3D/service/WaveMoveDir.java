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
    private WaveMoveDirProp[] moveDirPropArr;
    private int maxProp = 0;

    public WaveMoveDir() {
        this.moveDirPropArr = new WaveMoveDirProp[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> this.moveDirPropArr[dir.dir()] = new WaveMoveDirProp(0));
        this.maxProp = 0;
    }

    public WaveMoveDir(final WaveMoveDirProp[] moveDirPropArr, final int maxProp) {
        this.moveDirPropArr = moveDirPropArr;
        this.maxProp = maxProp;
    }

    public void setDirMoveProp(final Cell.Dir dir, final int dirCalcProp) {
        //this.moveCalcDirArr[dirNo].setDir(dir);
        this.moveDirPropArr[dir.dir()].setDirMoveProp(dirCalcProp);
    }

    public WaveMoveDirProp[] getMoveDirPropArr() {
        return this.moveDirPropArr;
    }

    public WaveMoveDirProp getDirMoveProp(final Cell.Dir dir) {
        return this.moveDirPropArr[dir.dir()];
    }

    public WaveMoveDirProp getDirMoveProp(final int dirPos) {
        return this.moveDirPropArr[dirPos];
    }

    public WaveMoveDirProp getWaveMoveDirProp(final int dirCalcPos) {
        return this.moveDirPropArr[dirCalcPos];
    }

    public void adjustMaxProp() {
        this.maxProp = 0;
        for (int pos = 0; pos < this.moveDirPropArr.length; pos++) {
            if (this.moveDirPropArr[pos].getDirMoveProp() > maxProp) {
                this.maxProp = this.moveDirPropArr[pos].getDirMoveProp();
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

    public int getMaxProp() {
        return this.maxProp;
    }

    public void setMaxProp(final int maxProp) {
        this.maxProp = maxProp;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +  Arrays.toString(this.moveDirPropArr) + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.maxProp, this.moveDirPropArr);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveDir entry = (WaveMoveDir) obj;
        return this.maxProp == entry.maxProp &&
                (Arrays.equals(this.moveDirPropArr, entry.moveDirPropArr));
    }
}
