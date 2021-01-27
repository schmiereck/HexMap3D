package de.schmiereck.hexMap3D.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Cache für WaveMoveCalc aufbauen und zentral die Berechnung für den nächsten Schritt dort machen.
 * Dann kann der Zustand aus dem equals() raus und es wird nur noch die WaveMoveDir verglichen.
 */
public class WaveMoveCalcService {

    private static final Map<WaveMoveDir, WaveMoveCalc> waveMoveCalcCacheMap = new HashMap<>();

    public static boolean useWaveMoveCalcCache = true;

    @FunctionalInterface
    private interface CreateWaveMoveCalcInterface {
        WaveMoveCalc createWaveMoveCalc();
    }
    /*
    public static WaveMoveCalc createNewInitalWaveMoveCalc(final int dirCalcPos, final WaveMoveDir waveMoveDir) {
        final WaveMoveCalc waveMoveCalc;
        if (useWaveMoveCalcCache) {
            final WaveMoveCalc cachedWaveMoveCalc = waveMoveCalcCacheMap.get(waveMoveDir);
            if (cachedWaveMoveCalc != null) {
                waveMoveCalc = cachedWaveMoveCalc;
            } else {
                waveMoveCalc = new WaveMoveCalc(dirCalcPos, waveMoveDir);
                waveMoveCalcCacheMap.put(waveMoveDir, waveMoveCalc);
            }
        } else {
            waveMoveCalc = new WaveMoveCalc(dirCalcPos, waveMoveDir);
        }
        return waveMoveCalc;
    }
    */
    public static WaveMoveCalc createNewInitalWaveMoveCalc(final int dirCalcPos, final WaveMoveDir waveMoveDir) {
        final WaveMoveCalc newWaveMoveCalc =
                createNewWaveMoveCalc(waveMoveDir,
                        () -> {
                            final WaveMoveCalc waveMoveCalc = new WaveMoveCalc(dirCalcPos, waveMoveDir);
                            waveMoveCalc.calcActualWaveMoveCalcDir();
                            return waveMoveCalc;
                        });
        return newWaveMoveCalc;
    }

    public static WaveMoveCalc createRotatedWaveMoveCalc(final WaveMoveCalc sourceWaveMoveCalc, final WaveMoveDir newWaveMoveDir) {
        final int[] dirCalcPropSumArr = sourceWaveMoveCalc.getDirCalcProbSumArr();
        final int actualDirCalcPos = sourceWaveMoveCalc.getActualDirCalcPos();
        final int nextDirCalcPos = sourceWaveMoveCalc.nextDirCalcPos();
        final WaveMoveCalc newWaveMoveCalc =
                createNewWaveMoveCalc(newWaveMoveDir,
                        () -> {
                            final WaveMoveCalc waveMoveCalc = new WaveMoveCalc(actualDirCalcPos, newWaveMoveDir, dirCalcPropSumArr);
                            adjustDirCalcPropSum(waveMoveCalc);
                            return waveMoveCalc;
                        });
        //newWaveMoveCalc.calcDirMoved(actualDirCalcPos);
        return newWaveMoveCalc;
    }

    public static WaveMoveCalc createRotatedWaveMoveCalc(final WaveMoveCalc sourceWaveMoveCalc) {
        final WaveMoveDir newWaveMoveDir = WaveMoveDirService.createWaveMoveDir(sourceWaveMoveCalc.getMoveCalcDirArr());
        final int[] dirCalcPropSumArr = sourceWaveMoveCalc.getDirCalcProbSumArr();
        final int actualDirCalcPos = sourceWaveMoveCalc.getDirCalcPos();
        final int nextDirCalcPos = sourceWaveMoveCalc.nextDirCalcPos();
        final WaveMoveCalc newWaveMoveCalc =
                createNewWaveMoveCalc(newWaveMoveDir,
                        () -> {
                            final WaveMoveCalc waveMoveCalc = new WaveMoveCalc(actualDirCalcPos, newWaveMoveDir, dirCalcPropSumArr);
                            adjustDirCalcPropSum(waveMoveCalc);
                            return waveMoveCalc;
                        });
        //newWaveMoveCalc.calcDirMoved(actualDirCalcPos);
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

    public static WaveMoveCalc createNewWaveMoveCalc(final WaveMoveDir waveMoveDir, final CreateWaveMoveCalcInterface createWaveMoveCalcInterface) {
        final WaveMoveCalc waveMoveCalc;
        if (useWaveMoveCalcCache) {
            final WaveMoveCalc cachedWaveMoveCalc = waveMoveCalcCacheMap.get(waveMoveDir);
            if (cachedWaveMoveCalc != null) {
                waveMoveCalc = cachedWaveMoveCalc;
            } else {
                waveMoveCalc = createWaveMoveCalcInterface.createWaveMoveCalc();
                waveMoveCalcCacheMap.put(waveMoveDir, waveMoveCalc);
            }
        } else {
            waveMoveCalc = createWaveMoveCalcInterface.createWaveMoveCalc();
            final int actualDirCalcPos = waveMoveCalc.getDirCalcPos();
            //final int nextDirCalcPos = waveMoveCalc.nextDirCalcPos();
            waveMoveCalc.calcDirMoved(actualDirCalcPos);
            //waveMoveCalc.setDirCalcPos(nextDirCalcPos);
            waveMoveCalc.calcActualWaveMoveCalcDir();
        }
        return waveMoveCalc;
    }

    public static void calcAllDirMoved() {
        waveMoveCalcCacheMap.values().forEach((waveMoveCalc) -> {
            final int actualDirCalcPos = waveMoveCalc.getDirCalcPos();
            //final int nextDirCalcPos = waveMoveCalc.nextDirCalcPos();
            waveMoveCalc.calcDirMoved(actualDirCalcPos);
            //waveMoveCalc.setDirCalcPos(nextDirCalcPos);
            waveMoveCalc.calcActualWaveMoveCalcDir();
        });
    }
}
