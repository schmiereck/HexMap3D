package de.schmiereck.hexMap3D.service;

import java.util.Arrays;
import java.util.Objects;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

public class WaveMoveCalc {
    private int dirCalcPos;
    private WaveMoveDir waveMoveDir;
    /**
     * Counter between 0 and {@link WaveMoveDirProp#getDirMoveProp()}.
     * The dir number of {@link Cell.Dir} is the index.
     */
    private int[] dirCalcPropSumArr = new int[Cell.Dir.values().length];

    public WaveMoveCalc() {
        this.dirCalcPos = 0;
        this.waveMoveDir = new WaveMoveDir();
    }

    public WaveMoveCalc(final int dirCalcPos, final WaveMoveDir waveMoveDir) {
        this.dirCalcPos = dirCalcPos;
        this.waveMoveDir = waveMoveDir;
    }

    public WaveMoveCalc(final int dirCalcPos, final WaveMoveDir waveMoveDir, final int[] dirCalcPropSumArr) {
        this.dirCalcPos = dirCalcPos;
        this.waveMoveDir = waveMoveDir;
        this.dirCalcPropSumArr = Arrays.copyOf(dirCalcPropSumArr, dirCalcPropSumArr.length);
    }

    public WaveMoveDirProp getActualWaveMoveCalcDir() {
        return this.waveMoveDir.getWaveMoveDirProp(this.dirCalcPos);
    }

    public WaveMoveDir getWaveMoveDir() {
        return this.waveMoveDir;
    }

    public int getDirCalcPos() {
        return this.dirCalcPos;
    }

    public void setDirCalcProp(final Cell.Dir dir, final int dirCalcProp) {
        this.waveMoveDir.setDirMoveProp(dir, dirCalcProp);
    }

    public int getDirCalcPropSum(final Cell.Dir dir) {
        return this.getDirCalcPropSum(dir.dir());
    }

    public int getDirCalcPropSum(final int dirPos) {
        return this.dirCalcPropSumArr[dirPos];
    }

    public void setDirCalcPropSum(final Cell.Dir dir, final int dirCalcPropSum) {
        this.dirCalcPropSumArr[dir.dir()] = dirCalcPropSum;
    }

    public void addDirCalcPropSum(final Cell.Dir dir, final int dirCalcPropSum) {
        this.addDirCalcPropSum(dir.dir(), dirCalcPropSum);
    }

    public void addDirCalcPropSum(final int dirPos, final int dirCalcPropSum) {
        this.dirCalcPropSumArr[dirPos] += dirCalcPropSum;
    }

    public int nextDirCalcPos() {
        return wrap(this.dirCalcPos + 1, Cell.Dir.values().length);
    }

    public Cell.Dir getActualMoveDir() {
        return Cell.Dir.values()[this.dirCalcPos];
    }

    public void calcActualDirMoved() {
        final Cell.Dir actualMoveDir = this.getActualMoveDir();
        this.setDirCalcPropSum(actualMoveDir, this.getDirCalcPropSum(actualMoveDir) - waveMoveDir.getMaxProp());
    }

    public void calcActualWaveMoveCalcDir() {
        int startDirCalcCount = 0;
        do {
            this.dirCalcPos = nextDirCalcPos();
            final WaveMoveDirProp waveMoveDirProp = this.waveMoveDir.getDirMoveProp(this.dirCalcPos);
            this.addDirCalcPropSum(this.dirCalcPos, waveMoveDirProp.getDirMoveProp());
            if (startDirCalcCount >= Cell.Dir.values().length) {
                throw new RuntimeException("Do not found next dirCalcPos: " + this.waveMoveDir.toString());
            }
            startDirCalcCount++;
        } while (this.getDirCalcPropSum(this.dirCalcPos) < this.waveMoveDir.getMaxProp());
    }

    public int getMaxProp() {
        return this.waveMoveDir.getMaxProp();
    }

    public WaveMoveDirProp[] getMoveCalcDirArr() {
        return this.waveMoveDir.getMoveDirPropArr();
    }

    public void adjustMaxProp() {
        this.waveMoveDir.adjustMaxProp();
    }

    public int[] getDirCalcPropSumArr() {
        return this.dirCalcPropSumArr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.waveMoveDir);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveCalc entry = (WaveMoveCalc) obj;
        return (this.waveMoveDir.equals(entry.waveMoveDir));
    }
}
