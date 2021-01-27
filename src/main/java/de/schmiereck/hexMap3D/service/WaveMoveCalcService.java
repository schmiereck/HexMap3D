package de.schmiereck.hexMap3D.service;

/**
 * Cache für WaveMoveCalc aufbauen und zentral die Berechnung für den nächsten Schritt dort machen.
 * Dann kann der Zustand aus dem equals() raus und es wird nur noch die WaveMoveDir verglichen.
 */
public class WaveMoveCalcService {

    public static WaveMoveCalc createNewInitalWaveMoveCalc(final int dirCalcPos, final WaveMoveDir waveMoveDir) {
        return new WaveMoveCalc(dirCalcPos, waveMoveDir);
    }

    public static WaveMoveCalc createRotatedWaveMoveCalc(final WaveMoveCalc sourceWaveMoveCalc, final WaveMoveDir newWaveMoveDir) {
        final int[] dirCalcPropSumArr = sourceWaveMoveCalc.getDirCalcProbSumArr();
        final int actualDirCalcPos = sourceWaveMoveCalc.getActualDirCalcPos();
        final int nextDirCalcPos = sourceWaveMoveCalc.nextDirCalcPos();
        final WaveMoveCalc newWaveMoveCalc = new WaveMoveCalc(nextDirCalcPos, newWaveMoveDir, dirCalcPropSumArr);
        adjustDirCalcPropSum(newWaveMoveCalc);
        newWaveMoveCalc.calcDirMoved(actualDirCalcPos);
        return newWaveMoveCalc;
    }

    public static WaveMoveCalc createRotatedWaveMoveCalc(final WaveMoveCalc sourceWaveMoveCalc) {
        final WaveMoveDir newWaveMoveDir = WaveMoveDirService.createWaveMoveDir(sourceWaveMoveCalc.getMoveCalcDirArr());
        final int actualDirCalcPos = sourceWaveMoveCalc.getDirCalcPos();
        final WaveMoveCalc newWaveMoveCalc = new WaveMoveCalc(sourceWaveMoveCalc.nextDirCalcPos(), newWaveMoveDir, sourceWaveMoveCalc.getDirCalcProbSumArr());
        newWaveMoveCalc.calcDirMoved(actualDirCalcPos);
        return newWaveMoveCalc;
    }

    public static void adjustDirCalcPropSum(WaveMoveCalc waveMoveCalc) {
        final WaveMoveDir waveMoveDir = waveMoveCalc.getWaveMoveDir();
        final int[] dirCalcPropSumArr = waveMoveCalc.getDirCalcProbSumArr();
        for (int dirPos = 0; dirPos < dirCalcPropSumArr.length; dirPos++) {
            final WaveMoveDirProb waveMoveDirProb = waveMoveDir.getWaveMoveDirProb(dirPos);
            final int dirCalcPropSum = dirCalcPropSumArr[dirPos];
            if ((waveMoveDirProb.getDirMoveProb() == 0) && (dirCalcPropSum > 0)) {
                dirCalcPropSumArr[dirPos] = 0;
            }
        }
     }
}
