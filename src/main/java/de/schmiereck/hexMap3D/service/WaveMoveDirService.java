package de.schmiereck.hexMap3D.service;

import java.util.stream.IntStream;

public class WaveMoveDirService {

    public static WaveMoveDir createWaveMoveDir(final WaveMoveCalcDir[] givenMoveCalcDirArr) {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        int maxProp = 0;

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = WaveMoveCalcDirService.createWaveMoveCalcDir(givenMoveCalcDirArr[pos]);
            if (moveCalcDirArr[pos].getDirCalcProp() > maxProp) {
                maxProp = moveCalcDirArr[pos].getDirCalcProp();
            }
        }
        return new WaveMoveDir(moveCalcDirArr, maxProp);
    }

    public static WaveMoveDir createWaveMoveDir(final WaveMoveDir waveMoveDir) {
        final WaveMoveCalcDir[] givenMoveCalcDirArr = waveMoveDir.getMoveCalcDirArr();
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        final int maxProp = waveMoveDir.getMaxProp();

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = WaveMoveCalcDirService.createWaveMoveCalcDir(givenMoveCalcDirArr[pos]);
        }
        return new WaveMoveDir(moveCalcDirArr, maxProp);
    }

    public static WaveMoveCalc createWaveMoveCalc(final int dirCalcPos, final WaveMoveDir waveMoveDir) {
        return new WaveMoveCalc(dirCalcPos, waveMoveDir);
    }

    public static WaveMoveCalc createNextWaveMoveCalc(final int nextDirCalcPos, final WaveMoveDir newWaveMoveDir, final int[] dirCalcPropSumArr) {
        return new WaveMoveCalc(nextDirCalcPos, newWaveMoveDir, dirCalcPropSumArr);
    }

    public static WaveMoveCalc createNextWaveMoveCalc(final WaveMoveCalc sourceWaveMoveCalc) {
        final WaveMoveDir newWaveMoveDir = WaveMoveDirService.createWaveMoveDir(sourceWaveMoveCalc.getMoveCalcDirArr());
        final WaveMoveCalc newWaveMoveCalc = new WaveMoveCalc(sourceWaveMoveCalc.nextDirCalcPos(), newWaveMoveDir, sourceWaveMoveCalc.getDirCalcPropSumArr());
        return newWaveMoveCalc;
    }
}
