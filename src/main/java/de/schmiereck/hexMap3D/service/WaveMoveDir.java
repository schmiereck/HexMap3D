package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.MapMathUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;
import static de.schmiereck.hexMap3D.service.Engine.DIR_CALC_MAX_PROP;

/**
 * Direction of a move in a Kuboktaeder
 * representet by the probability of movin from his middle in the directions to his edges.
 */
public class WaveMoveDir {
    /**
     * Possible Directions.
     * The dir number of {@link Cell.Dir} is the index.
     */
    private WaveMoveCalcDir[] moveCalcDirArr;
    private int maxProp = 0;

    public WaveMoveDir() {
        this.moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> this.moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0));
        this.maxProp = 0;
    }

    public WaveMoveDir(final WaveMoveCalcDir[] moveCalcDirArr, final int maxProp) {
        this.moveCalcDirArr = moveCalcDirArr;
        this.maxProp = maxProp;
    }

    public void setDirCalcProp(final Cell.Dir dir, final int dirCalcProp) {
        //this.moveCalcDirArr[dirNo].setDir(dir);
        this.moveCalcDirArr[dir.dir()].setDirCalcProp(dirCalcProp);
    }

    public WaveMoveCalcDir[] getMoveCalcDirArr() {
        return this.moveCalcDirArr;
    }

    public WaveMoveCalcDir getMoveCalcDir(final Cell.Dir dir) {
        return this.moveCalcDirArr[dir.dir()];
    }

    public WaveMoveCalcDir getMoveCalcDir(final int dirPos) {
        return this.moveCalcDirArr[dirPos];
    }

    public WaveMoveCalcDir getWaveMoveCalcDir(final int dirCalcPos) {
        return this.moveCalcDirArr[dirCalcPos];
    }

    public void adjustMaxProp() {
        this.maxProp = 0;
        for (int pos = 0; pos < this.moveCalcDirArr.length; pos++) {
            if (this.moveCalcDirArr[pos].getDirCalcProp() > maxProp) {
                this.maxProp = this.moveCalcDirArr[pos].getDirCalcProp();
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
        return super.toString() + "{" +  Arrays.toString(this.moveCalcDirArr) + "}";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveDir entry = (WaveMoveDir) obj;
        return this.maxProp == entry.maxProp &&
                (Arrays.equals(this.moveCalcDirArr, entry.moveCalcDirArr));
    }
}
