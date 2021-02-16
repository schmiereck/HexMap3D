package de.schmiereck.hexMap3D.service.universe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

/**
 * Cache für WaveMoveCalc aufbauen und zentral die Berechnung für den nächsten Schritt dort machen.
 * Dann kann der Zustand aus dem equals() raus und es wird nur noch die WaveMoveDir verglichen.
 */
public class WaveMoveCalcService {

    private static final Map<WaveMoveDir, WaveMoveCalc> waveMoveCalcCacheMap = new HashMap<>();
    private static int waveMoveCalcCacheHitCount = 0;

    /**
     * {@link WaveMoveDirService#getUseWaveMoveDirCache()}
     */
    private static boolean useWaveMoveCalcCache = true;

    @FunctionalInterface
    private interface CreateWaveMoveCalcInterface {
        WaveMoveCalc createWaveMoveCalc();
    }

    public static void calcActualWaveMoveCalcDir(final WaveMoveCalc waveMoveCalc) {
        final int maxProb = waveMoveCalc.getMaxProb();
        final int[] moveDirProbArr = waveMoveCalc.getMoveDirProbArr();
        final int[] dirCalcProbSumArr = waveMoveCalc.getDirCalcProbSumArr();
        final int startDirCalcPos = waveMoveCalc.getDirCalcPos();
        final int dirCalcPos = calcWaveMoveCalcDir(moveDirProbArr, maxProb, dirCalcProbSumArr, startDirCalcPos);
        waveMoveCalc.setDirCalcPos(dirCalcPos);
    }

    public static int calcWaveMoveCalcDir(final int[] moveDirProbArr, final int maxProb, final int[] dirCalcProbSumArr, final int startDirCalcPos) {
        int startDirCalcCount = 0;
        int dirCalcPos = startDirCalcPos;
        for (int pos = 0; pos < moveDirProbArr.length; pos++) {
            dirCalcProbSumArr[pos] += moveDirProbArr[pos];
        }
        do {
            dirCalcPos = nextDirCalcPos(dirCalcPos, moveDirProbArr.length);
            final int waveMoveDirProb = moveDirProbArr[dirCalcPos];
            //dirCalcProbSumArr[dirCalcPos] += waveMoveDirProb;
            if (startDirCalcCount >= moveDirProbArr.length) {
                throw new RuntimeException("Do not found next dirCalcPos: " + Arrays.toString(moveDirProbArr));
            }
            startDirCalcCount++;
        } while (dirCalcProbSumArr[dirCalcPos] < maxProb);
            /*
            int startDirCalcCount = 0;
            do {
                dirCalcPos = wrap(dirCalcPos + 1, moveDirProbArr.length);
                moveDirProbSumArr[dirCalcPos] += moveDirProbArr[dirCalcPos];
                if (startDirCalcCount >= moveDirProbArr.length) {
                    throw new RuntimeException("Do not found next dirCalcPos: " + Arrays.toString(moveDirProbArr));
                }
                startDirCalcCount++;
            } while (moveDirProbSumArr[dirCalcPos] < maxProb);
            */
        return dirCalcPos;
    }

    public static void adjustMaxProb(final WaveMoveCalc waveMoveCalc) {
        WaveMoveDirService.adjustMaxProb(waveMoveCalc.getWaveMoveDir());
    }

    public static int nextDirCalcPos(final WaveMoveCalc waveMoveCalc) {
        return nextDirCalcPos(waveMoveCalc.getDirCalcPos(), Cell.Dir.values().length);
    }

    public static int nextDirCalcPos(final int dirCalcPos, final int dirCalcSize) {
        return wrap(dirCalcPos + 1, dirCalcSize);
    }

    public static void calcDirMoved(final WaveMoveCalc waveMoveCalc, final int dirMovePos) {
        final int maxProb = waveMoveCalc.getMaxProb();
        final int[] dirCalcProbSumArr = waveMoveCalc.getDirCalcProbSumArr();
        //waveMoveCalc.setDirCalcPropSum(dirMovePos, waveMoveCalc.getDirCalcProbSum(dirMovePos) - waveMoveCalc.getWaveMoveDir().getMaxProb());
        calcDirMoved(dirCalcProbSumArr, dirMovePos, maxProb);
    }

    public static void calcDirMoved(final int[] dirCalcProbSumArr, final int dirMovePos, final int maxProb) {
        dirCalcProbSumArr[dirMovePos] -= maxProb;
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
                            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
                            return waveMoveCalc;
                        });
        return newWaveMoveCalc;
    }

    public static WaveMoveCalc createRotatedWaveMoveCalc(final WaveMoveCalc sourceWaveMoveCalc, final WaveMoveDir newWaveMoveDir) {
        final int[] dirCalcPropSumArr = sourceWaveMoveCalc.getDirCalcProbSumArr();
        final int actualDirCalcPos = sourceWaveMoveCalc.getActualDirCalcPos();
        final int nextDirCalcPos = nextDirCalcPos(sourceWaveMoveCalc);
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
        final WaveMoveDir newWaveMoveDir = WaveMoveDirService.createWaveMoveDir(sourceWaveMoveCalc.getMoveDirProbArr());
        final int[] dirCalcPropSumArr = sourceWaveMoveCalc.getDirCalcProbSumArr();
        final int actualDirCalcPos = sourceWaveMoveCalc.getDirCalcPos();
        final int nextDirCalcPos = nextDirCalcPos(sourceWaveMoveCalc);
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
            final int waveMoveDirProb = waveMoveDir.getWaveMoveDirProb(dirPos);
            final int dirCalcPropSum = dirCalcPropSumArr[dirPos];
            if ((waveMoveDirProb == 0) && (dirCalcPropSum > 0)) {
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
                waveMoveCalcCacheHitCount++;
            } else {
                waveMoveCalc = createWaveMoveCalcInterface.createWaveMoveCalc();
                waveMoveCalcCacheMap.put(waveMoveDir, waveMoveCalc);
            }
        } else {
            waveMoveCalc = createWaveMoveCalcInterface.createWaveMoveCalc();
            final int actualDirCalcPos = waveMoveCalc.getDirCalcPos();
            //final int nextDirCalcPos = waveMoveCalc.nextDirCalcPos();
            calcDirMoved(waveMoveCalc, actualDirCalcPos);
            //waveMoveCalc.setDirCalcPos(nextDirCalcPos);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
        }
        return waveMoveCalc;
    }

    public static void calcAllDirMoved() {
        waveMoveCalcCacheMap.values().forEach((waveMoveCalc) -> {
            final int actualDirCalcPos = waveMoveCalc.getDirCalcPos();
            //final int nextDirCalcPos = waveMoveCalc.nextDirCalcPos();
            calcDirMoved(waveMoveCalc, actualDirCalcPos);
            //waveMoveCalc.setDirCalcPos(nextDirCalcPos);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
        });
    }

    public static void setUseWaveMoveCalcCache(final boolean useWaveMoveCalcCache) {
        if (useWaveMoveCalcCache == true) {
            if (BasicService.getUseCache() == false) {
                throw new RuntimeException("BasicService.getUseCache() == false");
            }
            if (WaveMoveDirService.getUseWaveMoveDirCache() == false) {
                throw new RuntimeException("WaveMoveDirService.getUseWaveMoveDirCache() == false");
            }
        }
        WaveMoveCalcService.useWaveMoveCalcCache = useWaveMoveCalcCache;
    }

    public static boolean getUseWaveMoveCalcCache() {
        return WaveMoveCalcService.useWaveMoveCalcCache;
    }

    public static void resetCacheHitCounts() {
        waveMoveCalcCacheHitCount = 0;
    }

    public static int getWaveMoveCalcCacheSize() {
        return waveMoveCalcCacheMap.size();
    }

    public static int getWaveMoveCalcCacheHitCount() {
        return waveMoveCalcCacheHitCount;
    }

}
