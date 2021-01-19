package de.schmiereck.hexMap3D.service;

public class WaveMoveDirService {

    public static WaveMoveDir createWaveMoveDir(final WaveMoveDirProp[] givenMoveCalcDirArr) {
        final WaveMoveDirProp[] moveCalcDirArr = new WaveMoveDirProp[Cell.Dir.values().length];
        int maxProp = 0;

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = WaveMoveDirPropService.createWaveMoveCalcDir(givenMoveCalcDirArr[pos]);
            if (moveCalcDirArr[pos].getDirCalcProp() > maxProp) {
                maxProp = moveCalcDirArr[pos].getDirCalcProp();
            }
        }
        return new WaveMoveDir(moveCalcDirArr, maxProp);
    }

    public static WaveMoveDir createWaveMoveDir(final WaveMoveDir waveMoveDir) {
        final WaveMoveDirProp[] givenMoveCalcDirArr = waveMoveDir.getMoveCalcDirArr();
        final WaveMoveDirProp[] moveCalcDirArr = new WaveMoveDirProp[Cell.Dir.values().length];
        final int maxProp = waveMoveDir.getMaxProp();

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = WaveMoveDirPropService.createWaveMoveCalcDir(givenMoveCalcDirArr[pos]);
        }
        return new WaveMoveDir(moveCalcDirArr, maxProp);
    }
}
