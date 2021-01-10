package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.MapMathUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;
import static de.schmiereck.hexMap3D.service.Engine.DIR_CALC_MAX_PROP;

public class WaveMoveDir {
    private int dirCalcPos;
    private int maxProp = 0;
    private WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];

    public WaveMoveDir(final int dirCalcPos, final WaveMoveCalcDir[] moveCalcDirArr) {
        this.dirCalcPos = dirCalcPos;
        IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
            this.moveCalcDirArr[pos] = new WaveMoveCalcDir(moveCalcDirArr[pos]);
//            if (this.moveCalcDirArr[pos].getDirCalcPropSum() > this.moveCalcDirArr[pos].getDirCalcProp()) {
//                this.moveCalcDirArr[pos].setDirCalcPropSum(this.moveCalcDirArr[pos].getDirCalcProp());
//            }
            //this.moveCalcDirArr[pos].setDirCalcPropSum(this.moveCalcDirArr[pos].getDirCalcPropSum() + this.moveCalcDirArr[pos].getDirCalcProp());
            if (this.moveCalcDirArr[pos].getDirCalcProp() > maxProp) {
                this.maxProp = this.moveCalcDirArr[pos].getDirCalcProp();
            }
        });
    }

    public void setDir(final Cell.Dir dir, final int dirCalcProp) {
        //this.moveCalcDirArr[dirNo].setDir(dir);
        this.moveCalcDirArr[dir.dir()].setDirCalcProp(dirCalcProp);
    }

    public WaveMoveCalcDir[] getMoveCalcDirArr() {
        return this.moveCalcDirArr;
    }

    public WaveMoveCalcDir getMoveCalcDir(final Cell.Dir dir) {
        return this.moveCalcDirArr[dir.dir()];
    }

    public WaveMoveCalcDir getActualWaveMoveCalcDir() {
        return this.moveCalcDirArr[this.dirCalcPos];
    }

    public void calcActualWaveMoveCalcDir() {
        int startDirCalcCount = 0;
        do {
            this.dirCalcPos = nextDirCalcPos();
            final WaveMoveCalcDir waveMoveCalcDir = this.moveCalcDirArr[this.dirCalcPos];
            waveMoveCalcDir.addDirCalcPropSum(waveMoveCalcDir.getDirCalcProp());
            if (startDirCalcCount >= this.moveCalcDirArr.length) {
                throw new RuntimeException("Do not found next dirCalcPos: " + Arrays.toString(this.moveCalcDirArr));
            }
            startDirCalcCount++;
        } while (this.moveCalcDirArr[this.dirCalcPos].getDirCalcPropSum() < this.maxProp);
    }

    public int nextDirCalcPos() {
        return wrap(this.dirCalcPos + 1, this.moveCalcDirArr.length);
    }

    public Cell.Dir getActualMoveDir() {
        return Cell.Dir.values()[this.dirCalcPos];
    }

    public void calcActualDirMoved() {
        final WaveMoveCalcDir actualWaveMoveCalcDir = this.getActualWaveMoveCalcDir();
        actualWaveMoveCalcDir.setDirCalcPropSum(actualWaveMoveCalcDir.getDirCalcPropSum() - this.maxProp);
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

    public int getDirCalcPos() {
        return this.dirCalcPos;
    }

    public int getMaxProp() {
        return this.maxProp;
    }

    public void setMaxProp(final int maxProp) {
        this.maxProp = maxProp;
    }
}
