package de.schmiereck.hexMap3D.service;

import java.util.stream.IntStream;

import de.schmiereck.hexMap3D.MapMathUtils;

public class Wave {
    private final Event event;
    private Cell cell;
    //private Cell.Dir dir = null;
    private int dirCalcPos;
    private WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];

    public Wave(final Event event, final int dirCalcPos, final WaveMoveCalcDir[] moveCalcDirArr) {
        this.event = event;
        this.dirCalcPos = dirCalcPos;
        IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
            this.moveCalcDirArr[pos] = new WaveMoveCalcDir(moveCalcDirArr[pos]);
            this.moveCalcDirArr[pos].setDirCalcPropSum(this.moveCalcDirArr[pos].getDirCalcPropSum() + this.moveCalcDirArr[pos].getDirCalcProp());
        });
    }

    public void setCell(final Cell cell) {
        this.cell = cell;
    }

    public Event getEvent() {
        return this.event;
    }

    public Wave createWave() {
         final Wave wave = new Wave(this.event, nextDirCalcPos(), this.moveCalcDirArr);
        this.event.addWave(wave);
        return wave;
    }

    private int nextDirCalcPos() {
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
        while ((this.moveCalcDirArr[this.dirCalcPos] == null) ||
               (this.moveCalcDirArr[this.dirCalcPos].getDirCalcPropSum() < Engine.DIR_CALC_MAX_PROP)) {
            this.dirCalcPos = nextDirCalcPos();
        }
    }

    public Cell.Dir getActualDirCalcPos() {
        return Cell.Dir.values()[this.dirCalcPos];
    }
}
