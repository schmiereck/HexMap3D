package de.schmiereck.hexMap3D.service;

import java.util.stream.IntStream;

public class WaveMoveDirService {

    public static WaveMoveDir createWaveMoveDir(final int givenDirCalcPos, final WaveMoveCalcDir[] givenMoveCalcDirArr) {
        final int dirCalcPos = givenDirCalcPos;
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        int maxProp = 0;

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = WaveMoveCalcDirService.createWaveMoveCalcDir(givenMoveCalcDirArr[pos]);
            if (moveCalcDirArr[pos].getDirCalcProp() > maxProp) {
                maxProp = moveCalcDirArr[pos].getDirCalcProp();
            }
        }
        return new WaveMoveDir(dirCalcPos, moveCalcDirArr, maxProp);
    }

    public static WaveMoveDir createWaveMoveDir(final WaveMoveDir waveMoveDir) {
        final WaveMoveCalcDir[] givenMoveCalcDirArr = waveMoveDir.getMoveCalcDirArr();
        final int dirCalcPos = waveMoveDir.getDirCalcPos();
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        final int maxProp = waveMoveDir.getMaxProp();

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = WaveMoveCalcDirService.createWaveMoveCalcDir(givenMoveCalcDirArr[pos]);
        }
        return new WaveMoveDir(dirCalcPos, moveCalcDirArr, maxProp);
    }
}
