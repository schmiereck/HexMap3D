package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap;
import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap2;
import static de.schmiereck.hexMap3D.MapMathUtils.wrap;
import static de.schmiereck.hexMap3D.service.WaveMoveDir.MAX_DIR_PROB;

public class WaveMoveDirService {

    public static boolean useRotateMoveDirCache = false;
    public static boolean useWaveMoveDirCache = false;

    @FunctionalInterface
    private interface CreateWaveMoveDirInterface {
        WaveMoveDir createWaveMoveDir();
    }

    private static class RotateMoveDirCacheEntry {
        final WaveMoveDir inWaveMoveDir;
        WaveMoveDir outWaveMoveDir;
        final int xRotPercent;
        final int yRotPercent;
        final int zRotPercent;

        private RotateMoveDirCacheEntry(final WaveMoveDir inWaveMoveDir, final int xRotPercent, final int yRotPercent, final int zRotPercent) {
            this.inWaveMoveDir = inWaveMoveDir;
            this.xRotPercent = xRotPercent;
            this.yRotPercent = yRotPercent;
            this.zRotPercent = zRotPercent;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.inWaveMoveDir, this.xRotPercent, this.yRotPercent, this.zRotPercent);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            final RotateMoveDirCacheEntry entry = (RotateMoveDirCacheEntry) obj;
            return (this.xRotPercent == entry.xRotPercent) &&
                   (this.yRotPercent == entry.yRotPercent) &&
                   (this.zRotPercent == entry.zRotPercent) &&
                   this.inWaveMoveDir.equals(entry.inWaveMoveDir);
        }
    }

    private static final Map<WaveMoveDir, WaveMoveDir> waveMoveDirCacheMap = new HashMap<>();
    private static final Map<RotateMoveDirCacheEntry, RotateMoveDirCacheEntry> rotateMoveDirCacheMap = new HashMap<>();

    public static WaveMoveDir createNewWaveMoveDir(final WaveMoveDir givenWaveMoveDir) {
        final WaveMoveDir waveMoveDir;
        if (useWaveMoveDirCache) {
            final WaveMoveDir cachedWaveMoveDir = waveMoveDirCacheMap.get(givenWaveMoveDir);
            if (cachedWaveMoveDir != null) {
                waveMoveDir = cachedWaveMoveDir;
            } else {
                waveMoveDir = givenWaveMoveDir;//createWaveMoveDirInterface.createWaveMoveDir();
                waveMoveDirCacheMap.put(waveMoveDir, waveMoveDir);
            }
        } else {
            waveMoveDir = givenWaveMoveDir;//createWaveMoveDirInterface.createWaveMoveDir();
        }
        return waveMoveDir;
    }

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
        return createNewWaveMoveDir(new WaveMoveDir(moveCalcDirArr, maxProp));
    }

    public static WaveMoveDir createRotatedWaveMoveDir(final WaveMoveDir sourceWaveMoveDir,
                                                       final int xRotPercent, final int yRotPercent, final int zRotPercent) {
        final WaveMoveDir newWaveMoveDir;

        if (useRotateMoveDirCache) {
            final RotateMoveDirCacheEntry newRotateMoveDirCacheEntry = new RotateMoveDirCacheEntry(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
            final RotateMoveDirCacheEntry rotateMoveDirCacheEntry = rotateMoveDirCacheMap.get(newRotateMoveDirCacheEntry);
            if (rotateMoveDirCacheEntry != null) {
                newWaveMoveDir = rotateMoveDirCacheEntry.outWaveMoveDir;
            } else {
                newWaveMoveDir = createNewRotatedWaveMoveDir(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
                newRotateMoveDirCacheEntry.outWaveMoveDir = newWaveMoveDir;
                rotateMoveDirCacheMap.put(newRotateMoveDirCacheEntry, newRotateMoveDirCacheEntry);
            }
        } else {
            newWaveMoveDir = createNewRotatedWaveMoveDir(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
        }
        return newWaveMoveDir;
    }

    private static WaveMoveDir createNewRotatedWaveMoveDir(final WaveMoveDir waveMoveDir, final int xRotPercent, final int yRotPercent, final int zRotPercent) {
        // Rotate all move outputs in their rotation planes in the given direction.
        // Move the output from cross node or to cross node.
        // If a rotation plane contains only one node create a new node in the given direction.
        // If the cross node is empty create a new cross node in the given direction.
        final WaveMoveDir newWaveMoveDir;
        {
            final WaveMoveDirProb[] givenMoveDirProbArr = waveMoveDir.getMoveDirProbArr();
            final WaveMoveDirProb[] moveDirProbArr = new WaveMoveDirProb[Cell.Dir.values().length];
            final int maxProp = waveMoveDir.getMaxProb();

            //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
            for (int pos = 0; pos < moveDirProbArr.length; pos++) {
                moveDirProbArr[pos] = WaveMoveDirPropService.createWaveMoveCalcDir(givenMoveDirProbArr[pos]);
            }
            newWaveMoveDir = new WaveMoveDir(moveDirProbArr, maxProp);
        }
        if (xRotPercent != 0) {
            for (int axisPos = 0; axisPos < GridUtils.xRotArr.length; axisPos++) {
                final Cell.Dir[] rotArr = GridUtils.xRotArr[axisPos];
                calcRotationOnAxis(xRotPercent, newWaveMoveDir, rotArr);
            }
        }
        if (yRotPercent != 0) {
            for (int axisPos = 0; axisPos < GridUtils.yRotArr.length; axisPos++) {
                final Cell.Dir[] rotArr = GridUtils.yRotArr[axisPos];
                calcRotationOnAxis(yRotPercent, newWaveMoveDir, rotArr);
            }
        }
        if (zRotPercent != 0) {
            for (int axisPos = 0; axisPos < GridUtils.zRotArr.length; axisPos++) {
                final Cell.Dir[] rotArr = GridUtils.zRotArr[axisPos];
                calcRotationOnAxis(zRotPercent, newWaveMoveDir, rotArr);
            }
        }

        newWaveMoveDir.adjustMaxProb();

        return createNewWaveMoveDir(newWaveMoveDir);
    }

    private static void calcRotationOnAxis(final int signedRotPercent, final WaveMoveDir newWaveMoveDir, final Cell.Dir[] rotArr) {
        final int rotDir;
        final int rotStartPos;
        final int rotEndPos;
        final int rotPercent;
        if (signedRotPercent > 0) {
            rotPercent = signedRotPercent;
            rotDir = +1;
            rotStartPos = 0;
            rotEndPos = rotArr.length - 1;
        } else {
            rotPercent = -signedRotPercent;
            rotDir = -1;
            rotStartPos = rotArr.length - 1;
            rotEndPos = 0;
        }
        final int probSum = calcProbSum(newWaveMoveDir, rotArr);
        if (probSum > 0) {
            // Search last zero:
            final int notZeroPos =
                    calcBreakLoopWrap(rotStartPos, rotEndPos, rotDir, pos -> {
                        final WaveMoveDirProb moveCalcDir = newWaveMoveDir.getDirMoveProb(rotArr[pos]);
                        final WaveMoveDirProb beforMoveCalcDir = newWaveMoveDir.getDirMoveProb(rotArr[wrap(pos - rotDir, rotArr.length)]);
                        return ((moveCalcDir.getDirMoveProb() > 0) && (beforMoveCalcDir.getDirMoveProb() == 0));
                    });
            final int moveAmount = getMoveAmount(rotPercent, probSum);
            final AtomicInteger actMoveAmount = new AtomicInteger(moveAmount);
            // Move propability in given direction until "moveAmount" is zero.
            calcBreakLoopWrap2(notZeroPos, rotArr.length, rotDir, pos -> {
                final WaveMoveDirProb moveCalcDir = newWaveMoveDir.getDirMoveProb(rotArr[pos]);
                final WaveMoveDirProb nextMoveCalcDir = newWaveMoveDir.getDirMoveProb(rotArr[wrap(pos + rotDir, rotArr.length)]);
                final int prob = moveCalcDir.getDirMoveProb();
                final int newProb, probDif;
                if (prob <= actMoveAmount.get()) {
                    probDif = prob;
                    newProb = 0;
                    actMoveAmount.addAndGet(-probDif);
                } else {
                    probDif = actMoveAmount.get();
                    newProb = prob - probDif;
                    actMoveAmount.set(0);
                }
                moveCalcDir.setDirMoveProb(newProb);
                nextMoveCalcDir.addDirMoveProb(probDif);
                return actMoveAmount.get() == 0;
            });
        } else {
            // No outputs to rotate.
        }
    }

    public static int getMoveAmount(int rotPercent, int probSum) {
        final int a = (probSum * rotPercent);
        final int ret;
        if (a != 0) {
            final int b = (a / MAX_DIR_PROB);
            if (b == 0) {
                final int c = (a % MAX_DIR_PROB);
                if (c == 0) {
                    ret = 0;
                } else {
                    if (c > 0) {
                        ret = +1;
                    } else {
                        ret = -1;
                    }
                }
            } else {
                ret = b;
            }
        } else {
            ret = 0;
        }
        return ret;
    }

    private static int calcProbSum(final WaveMoveDir waveMoveDir, final Cell.Dir[] rotArr) {
        int probSum = 0;
        for (int pos = 0; pos < rotArr.length; pos++) {
            final WaveMoveDirProb moveCalcDir = waveMoveDir.getDirMoveProb(rotArr[pos]);
            probSum += moveCalcDir.getDirMoveProb();
        }
        return probSum;
    }
}
