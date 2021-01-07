package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.MapMathUtils;

import java.util.stream.IntStream;

import static de.schmiereck.hexMap3D.service.Engine.DIR_CALC_MAX_PROP;

/**
 *  40  30  30
 * -----------
 * 100   0   0
 *  40  30  30
 *  80  60  60
 * 120  90  90
 *  20 120 120
 *  60  20 150
 * 100  50  50
 *
 *
 */
public class WaveMoveDir {
    private int dirCalcPos;
    private WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];

    public WaveMoveDir(final int dirCalcPos, final WaveMoveCalcDir[] moveCalcDirArr) {
        this.dirCalcPos = dirCalcPos;
        IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
            this.moveCalcDirArr[pos] = new WaveMoveCalcDir(moveCalcDirArr[pos]);
            this.moveCalcDirArr[pos].setDirCalcPropSum(this.moveCalcDirArr[pos].getDirCalcPropSum() + this.moveCalcDirArr[pos].getDirCalcProp());
        });
    }

    public int nextDirCalcPos() {
        return MapMathUtils.wrap(this.dirCalcPos + 1, this.moveCalcDirArr.length);
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
        final int startDirCalcPos = this.dirCalcPos;
        while ((this.moveCalcDirArr[this.dirCalcPos] == null) ||
                (this.moveCalcDirArr[this.dirCalcPos].getDirCalcPropSum() < DIR_CALC_MAX_PROP)) {
            this.dirCalcPos = nextDirCalcPos();
            if (this.dirCalcPos == startDirCalcPos) {
                throw new RuntimeException("Do not found next dirCalcPos.");
            }
        }
    }

    public Cell.Dir getActualMoveDir() {
        return Cell.Dir.values()[this.dirCalcPos];
    }

    public void calcActualDirMoved() {
        final WaveMoveCalcDir actualWaveMoveCalcDir = this.getActualWaveMoveCalcDir();
        actualWaveMoveCalcDir.setDirCalcPropSum(actualWaveMoveCalcDir.getDirCalcPropSum() - DIR_CALC_MAX_PROP);
    }
}
