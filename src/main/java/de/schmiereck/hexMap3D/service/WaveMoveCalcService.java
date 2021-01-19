package de.schmiereck.hexMap3D.service;

public class WaveMoveCalcService {

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

    public static void adjustDirCalcPropSum(WaveMoveCalc waveMoveCalc) {
        final WaveMoveDir waveMoveDir = waveMoveCalc.getWaveMoveDir();
        final int[] dirCalcPropSumArr = waveMoveCalc.getDirCalcPropSumArr();
        for (int dirPos = 0; dirPos < dirCalcPropSumArr.length; dirPos++) {
            final WaveMoveDirProp waveMoveDirProp = waveMoveDir.getWaveMoveDirProp(dirPos);
            final int dirCalcPropSum = dirCalcPropSumArr[dirPos];
            if ((waveMoveDirProp.getDirMoveProp() == 0) && (dirCalcPropSum > 0)) {
                dirCalcPropSumArr[dirPos] = 0;
            }
        }
     }
}
