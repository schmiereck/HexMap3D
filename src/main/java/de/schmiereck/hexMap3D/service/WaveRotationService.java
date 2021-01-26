package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap;
import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap2;
import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

public class WaveRotationService {

    // Rotation-Matrix X, Y, Z:
    public static final int[][] rotationMatrixXYZ =
            {
              // X   Y   Z
              {  1,  0,  0 },
              {  1,  1,  0 },
              {  0,  1,  0 },
              {  0,  1,  1 },
              {  0,  0,  1 },
              {  1,  0,  1 },

              { -1,  0,  0 },
              { -1, -1,  0 },
              {  0, -1,  0 },
              {  0, -1, -1 },
              {  0,  0, -1 },
              { -1,  0, -1 },

              /*
              { -1,  0,  1 },
              {  1,  0, -1 }
              */
            };

    public static boolean useRotateMoveDirCache = true;

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
            return this.xRotPercent == entry.xRotPercent &&
                    this.yRotPercent == entry.yRotPercent &&
                    this.zRotPercent == entry.zRotPercent &&
                    (this.inWaveMoveDir.equals(entry.inWaveMoveDir));
        }
    }

    private static final Map<RotateMoveDirCacheEntry, RotateMoveDirCacheEntry> rotateMoveDirCacheSet = new HashMap<>();

    protected static Wave createMoveRotatedWave(final Wave sourceWave,
                                                final int xRotPercent,
                                                final int yRotPercent,
                                                final int zRotPercent,
                                                final int newWaveProb) {
        final Wave newWave;

        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveDir sourceWaveMoveDir = sourceWaveMoveCalc.getWaveMoveDir();
        final WaveMoveDir newWaveMoveDir;

        if (useRotateMoveDirCache) {
            final RotateMoveDirCacheEntry newRotateMoveDirCacheEntry = new RotateMoveDirCacheEntry(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
            final RotateMoveDirCacheEntry rotateMoveDirCacheEntry = rotateMoveDirCacheSet.get(newRotateMoveDirCacheEntry);
            if (rotateMoveDirCacheEntry != null) {
                newWaveMoveDir = rotateMoveDirCacheEntry.outWaveMoveDir;
            } else {
                newWaveMoveDir = createMoveRotatedWaveMoveDir(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
                newRotateMoveDirCacheEntry.outWaveMoveDir = newWaveMoveDir;
                rotateMoveDirCacheSet.put(newRotateMoveDirCacheEntry, newRotateMoveDirCacheEntry);
            }
        } else {
            newWaveMoveDir = createMoveRotatedWaveMoveDir(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
        }

        final int actualDirCalcPos = sourceWaveMoveCalc.getActualDirCalcPos();
        //final int nextDirCalcPos = 0;
        final int nextDirCalcPos = sourceWaveMoveCalc.nextDirCalcPos();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createNextWaveMoveCalc(actualDirCalcPos, nextDirCalcPos, newWaveMoveDir, sourceWaveMoveCalc.getDirCalcProbSumArr());

        WaveMoveCalcService.adjustDirCalcPropSum(newWaveMoveCalc);

        newWave = WaveService.createNextRotatedWave(sourceWave.getEvent(), newWaveMoveCalc,
                sourceWave.nextRotationCalcPos(),
                newWaveProb);//sourceWave.getWaveProb());
                //sourceWave.getRotationCalcPos());

        return newWave;
    }

    private static WaveMoveDir createMoveRotatedWaveMoveDir(final WaveMoveDir waveMoveDir, final int xRotPercent, final int yRotPercent, final int zRotPercent) {
        // Rotate all move outputs in their rotation planes in the given direction.
        // Move the output from cross node or to cross node.
        // If a rotation plane contains only one node create a new node in the given direction.
        // If the cross node is empty create a new cross node in the given direction.

        final WaveMoveDir newWaveMoveDir = WaveMoveDirService.createWaveMoveDir(waveMoveDir);

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

        return newWaveMoveDir;
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
            final int b = (a / 100);
            if (b == 0) {
                final int c = (a % 100);
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
