package de.schmiereck.hexMap3D.service;

import java.util.Arrays;
import java.util.Objects;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

public class WaveMoveCalc {
    private int dirCalcPos;
    private WaveMoveDir waveMoveDir;
    /**
     * Counter between 0 and {@link WaveMoveDirProb#getDirMoveProb()}.
     * The dir number of {@link Cell.Dir} is the index.
     */
    private int[] dirCalcProbSumArr = new int[Cell.Dir.values().length];

    public WaveMoveCalc() {
        this.dirCalcPos = 0;
        this.waveMoveDir = new WaveMoveDir();
    }

    public WaveMoveCalc(final int dirCalcPos, final WaveMoveDir waveMoveDir) {
        this.dirCalcPos = dirCalcPos;
        this.waveMoveDir = waveMoveDir;
    }

    public WaveMoveCalc(final int dirCalcPos, final WaveMoveDir waveMoveDir, final int[] dirCalcProbSumArr) {
        this.dirCalcPos = dirCalcPos;
        this.waveMoveDir = waveMoveDir;
        this.dirCalcProbSumArr = Arrays.copyOf(dirCalcProbSumArr, dirCalcProbSumArr.length);
    }

    public WaveMoveDirProb getActualWaveMoveCalcDir() {
        return this.waveMoveDir.getWaveMoveDirProb(this.dirCalcPos);
    }

    public WaveMoveDir getWaveMoveDir() {
        return this.waveMoveDir;
    }

    public void setDirCalcPos(final int dirCalcPos) {
        this.dirCalcPos = dirCalcPos;
    }

    public int getDirCalcPos() {
        return this.dirCalcPos;
    }

    public void setDirCalcProb(final Cell.Dir dir, final int dirCalcProb) {
        this.waveMoveDir.setDirMoveProb(dir, dirCalcProb);
    }

    public int getDirCalcProbSum(final Cell.Dir dir) {
        return this.getDirCalcProbSum(dir.dir());
    }

    public int getDirCalcProbSum(final int dirPos) {
        return this.dirCalcProbSumArr[dirPos];
    }

    public void setDirCalcPropSum(final Cell.Dir dir, final int dirCalcPropSum) {
        this.setDirCalcPropSum(dir.dir(), dirCalcPropSum);
    }

    public void setDirCalcPropSum(final int dirPos, final int dirCalcPropSum) {
        this.dirCalcProbSumArr[dirPos] = dirCalcPropSum;
    }

    public void addDirCalcProbSum(final Cell.Dir dir, final int dirCalcPropSum) {
        this.addDirCalcProbSum(dir.dir(), dirCalcPropSum);
    }

    public void addDirCalcProbSum(final int dirPos, final int dirCalcPropSum) {
        this.dirCalcProbSumArr[dirPos] += dirCalcPropSum;
    }

    public int nextDirCalcPos() {
        return wrap(this.dirCalcPos + 1, Cell.Dir.values().length);
    }

    public int getActualDirCalcPos() {
        return this.dirCalcPos;
    }

    public Cell.Dir getActualMoveDir() {
        return Cell.Dir.values()[this.dirCalcPos];
    }

    public void calcDirMoved(final int dirMovePos) {
        //final Cell.Dir actualMoveDir = this.getActualMoveDir();
        this.setDirCalcPropSum(dirMovePos, this.getDirCalcProbSum(dirMovePos) - waveMoveDir.getMaxProb());
    }

    public void calcActualWaveMoveCalcDir() {
        int startDirCalcCount = 0;
        do {
            this.dirCalcPos = nextDirCalcPos();
            final WaveMoveDirProb waveMoveDirProb = this.waveMoveDir.getDirMoveProb(this.dirCalcPos);
            this.addDirCalcProbSum(this.dirCalcPos, waveMoveDirProb.getDirMoveProb());
            if (startDirCalcCount >= Cell.Dir.values().length) {
                throw new RuntimeException("Do not found next dirCalcPos: " + this.waveMoveDir.toString());
            }
            startDirCalcCount++;
        } while (this.getDirCalcProbSum(this.dirCalcPos) < this.waveMoveDir.getMaxProb());
    }

    public int getMaxProb() {
        return this.waveMoveDir.getMaxProb();
    }

    public WaveMoveDirProb[] getMoveCalcDirArr() {
        return this.waveMoveDir.getMoveDirProbArr();
    }

    public void adjustMaxProb() {
        this.waveMoveDir.adjustMaxProb();
    }

    public int[] getDirCalcProbSumArr() {
        return this.dirCalcProbSumArr;
    }

    private int hashCode = 0;
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            //hashCode = Objects.hash(this.dirCalcPos, this.waveMoveDir, Arrays.hashCode(this.dirCalcProbSumArr));
            hashCode = Objects.hash(this.waveMoveDir);
        }
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveCalc entry = (WaveMoveCalc) obj;
        return //(this.dirCalcPos == entry.dirCalcPos) &&
                //Arrays.equals(this.dirCalcProbSumArr, entry.dirCalcProbSumArr) &&
                this.waveMoveDir.equals(entry.waveMoveDir);
    }
}
