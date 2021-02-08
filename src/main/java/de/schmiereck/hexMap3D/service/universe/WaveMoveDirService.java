package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap;
import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap2;
import static de.schmiereck.hexMap3D.MapMathUtils.wrap;
import static de.schmiereck.hexMap3D.service.universe.WaveMoveDir.MAX_DIR_PROB;

public class WaveMoveDirService {

    public static boolean useRotateMoveDirCache = true;
    public static boolean useWaveMoveDirCache = true;

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
    private static int waveMoveDirCacheHitCount = 0;
    private static final Map<RotateMoveDirCacheEntry, RotateMoveDirCacheEntry> rotateMoveDirCacheMap = new HashMap<>();
    private static int rotateMoveDirCacheHitCount = 0;

    public static WaveMoveDir createNewWaveMoveDir(final WaveMoveDir givenWaveMoveDir) {
        final WaveMoveDir waveMoveDir;
        if (useWaveMoveDirCache) {
            final WaveMoveDir cachedWaveMoveDir = waveMoveDirCacheMap.get(givenWaveMoveDir);
            if (cachedWaveMoveDir != null) {
                waveMoveDir = cachedWaveMoveDir;
                waveMoveDirCacheHitCount++;
            } else {
                waveMoveDir = givenWaveMoveDir;//createWaveMoveDirInterface.createWaveMoveDir();
                waveMoveDirCacheMap.put(waveMoveDir, waveMoveDir);
            }
        } else {
            waveMoveDir = givenWaveMoveDir;//createWaveMoveDirInterface.createWaveMoveDir();
        }
        return waveMoveDir;
    }

    public static WaveMoveDir createWaveMoveDir(final int[] givenMoveCalcDirArr) {
        final int[] moveCalcDirArr = new int[Cell.Dir.values().length];
        int maxProp = 0;

        //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            moveCalcDirArr[pos] = givenMoveCalcDirArr[pos];
            if (moveCalcDirArr[pos] > maxProp) {
                maxProp = moveCalcDirArr[pos];
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
                rotateMoveDirCacheHitCount++;
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
            final int[] givenMoveDirProbArr = waveMoveDir.getMoveDirProbArr();
            final int[] moveDirProbArr = new int[Cell.Dir.values().length];
            final int maxProp = waveMoveDir.getMaxProb();

            //IntStream.range(0, moveCalcDirArr.length).forEach(pos -> {
            for (int pos = 0; pos < moveDirProbArr.length; pos++) {
                moveDirProbArr[pos] = givenMoveDirProbArr[pos];
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

        adjustMaxProb(newWaveMoveDir);

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
                        final Cell.Dir dir = rotArr[pos];
                        final Cell.Dir beforDir = rotArr[wrap(pos - rotDir, rotArr.length)];
                        final int dirMoveProb = newWaveMoveDir.getDirMoveProb(dir);
                        final int beforDirMoveProb = newWaveMoveDir.getDirMoveProb(beforDir);
                        return ((dirMoveProb > 0) && (beforDirMoveProb == 0));
                    });
            final int moveAmount = getMoveAmount(rotPercent, probSum);
            final AtomicInteger actMoveAmount = new AtomicInteger(moveAmount);
            // Move propability in given direction until "moveAmount" is zero.
            calcBreakLoopWrap2(notZeroPos, rotArr.length, rotDir, pos -> {
                final Cell.Dir dir = rotArr[pos];
                final Cell.Dir nextDir = rotArr[wrap(pos + rotDir, rotArr.length)];
                final int dirMoveProb = newWaveMoveDir.getDirMoveProb(dir);
                final int prob = dirMoveProb;
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
                newWaveMoveDir.setDirMoveProb(dir, newProb);
                newWaveMoveDir.addDirMoveProb(nextDir, probDif);
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
            final int dirMoveProb = waveMoveDir.getDirMoveProb(rotArr[pos]);
            probSum += dirMoveProb;
        }
        return probSum;
    }

    public static void adjustMaxProb(final WaveMoveDir waveMoveDir) {
        final int[] moveDirProbArr = waveMoveDir.getMoveDirProbArr();
        final int maxProb = calcMaxProb(moveDirProbArr);
        waveMoveDir.setMaxProb(maxProb);
    }

    protected static int calcMaxProb(final int[] moveDirProbArr) {
        int maxProb = 0;
        for (int pos = 0; pos < moveDirProbArr.length; pos++) {
            if (moveDirProbArr[pos] > maxProb) {
                maxProb = moveDirProbArr[pos];
            }
//            if (this.moveCalcDirArr[pos].getDirCalcPropSum() > this.moveCalcDirArr[pos].getDirCalcProp()) {
//                this.moveCalcDirArr[pos].setDirCalcPropSum(this.moveCalcDirArr[pos].getDirCalcProp());
//            }
//            if (this.moveCalcDirArr[pos].getDirCalcProp() == 0) {
//                this.moveCalcDirArr[pos].setDirCalcPropSum(0);
//            }
        }
        /*
        int propPos = -1;
        for (int pos = 0; pos < this.moveCalcDirArr.length; pos++) {
            final int dirPos = wrap(this.dirCalcPos + pos, this.moveCalcDirArr.length);
            final int prop = moveCalcDirArr[dirPos].getDirCalcProp();
            if (prop > 0) {
                final int propSum = DIR_CALC_MAX_PROP - (propPos * prop);
                if (propSum > prop) {
                    this.moveCalcDirArr[dirPos].setDirCalcPropSum(propSum + prop);
                } else {
                    this.moveCalcDirArr[dirPos].setDirCalcPropSum(prop);
                }
                propPos++;
            }
        }
        */
        return maxProb;
    }

    public static void resetCacheHitCounts() {
        waveMoveDirCacheHitCount = 0;
        rotateMoveDirCacheHitCount = 0;
    }

    public static int getWaveMoveDirCacheMapSize() {
        return waveMoveDirCacheMap.size();
    }

    public static int getRotateMoveDirCacheMapSize() {
        return rotateMoveDirCacheMap.size();
    }

    public static int getWaveMoveDirCacheHitCount() {
        return waveMoveDirCacheHitCount;
    }

    public static int getRotateMoveDirCacheHitCount() {
        return rotateMoveDirCacheHitCount;
    }

}
