package de.schmiereck.hexMap3D.service;

public class WaveMoveDirService {

    public static WaveMoveDir createWaveMoveDir(final WaveMoveDirProb[] givenMoveCalcDirArr) {
        final WaveMoveDirProb[] moveCalcDirArr = new WaveMoveDirProb[Cell.Dir.values().length];
        int maxProp = 0;

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = WaveMoveDirPropService.createWaveMoveCalcDir(givenMoveCalcDirArr[pos]);
            if (moveCalcDirArr[pos].getDirMoveProb() > maxProp) {
                maxProp = moveCalcDirArr[pos].getDirMoveProb();
            }
        }
        return new WaveMoveDir(moveCalcDirArr, maxProp);
    }

    public static WaveMoveDir createWaveMoveDir(final WaveMoveDir waveMoveDir) {
        final WaveMoveDirProb[] givenMoveCalcDirArr = waveMoveDir.getMoveDirProbArr();
        final WaveMoveDirProb[] moveCalcDirArr = new WaveMoveDirProb[Cell.Dir.values().length];
        final int maxProp = waveMoveDir.getMaxProb();

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = WaveMoveDirPropService.createWaveMoveCalcDir(givenMoveCalcDirArr[pos]);
        }
        return new WaveMoveDir(moveCalcDirArr, maxProp);
    }
}
