package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

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
              { -1,  0,  1 },
              { -1,  0,  0 },
              { -1, -1,  0 },
              {  0, -1,  0 },
              {  0, -1, -1 },
              {  0,  0, -1 },
              {  1,  0, -1 },
            };

    protected static Wave createMoveRotatedWave(final Wave sourceWave,
                                                final int xRotPercent,
                                                final int yRotPercent,
                                                final int zRotPercent) {
        final Wave newWave;

        // Rotate all move outputs in their rotation planes in the given direction.
        // Move the output from cross node or to cross node.
        // If a rotation plane contains only one node create a new node in the given direction.
        // If the cross node is empty create a new cross node in the given direction.

        final WaveMoveDir waveMoveDir = sourceWave.getWaveMoveDir();
        //final WaveMoveDir newWaveMoveDir = newWave.getWaveMoveDir();
        WaveMoveDir newWaveMoveDir = waveMoveDir;

        if (xRotPercent != 0) {
            for (int axisPos = 0; axisPos < GridUtils.xRotArr.length; axisPos++) {
                final Cell.Dir[] rotArr = GridUtils.xRotArr[axisPos];
                newWaveMoveDir = calcRotationOnAxis(xRotPercent, newWaveMoveDir, rotArr);
            }
        }
        if (yRotPercent != 0) {
            for (int axisPos = 0; axisPos < GridUtils.yRotArr.length; axisPos++) {
                final Cell.Dir[] rotArr = GridUtils.yRotArr[axisPos];
                newWaveMoveDir = calcRotationOnAxis(yRotPercent, newWaveMoveDir, rotArr);
            }
        }
        if (zRotPercent != 0) {
            for (int axisPos = 0; axisPos < GridUtils.zRotArr.length; axisPos++) {
                final Cell.Dir[] rotArr = GridUtils.zRotArr[axisPos];
                newWaveMoveDir = calcRotationOnAxis(zRotPercent, newWaveMoveDir, rotArr);
            }
        }

        newWaveMoveDir.adjustMaxProp();

        newWave = WaveService.createWave(sourceWave.getEvent(), newWaveMoveDir, sourceWave.getPropCalcPos());

        return newWave;
    }

    private static WaveMoveDir calcRotationOnAxis(final int signedRotPercent, final WaveMoveDir waveMoveDir, final Cell.Dir[] rotArr) {
        final WaveMoveDir newWaveMoveDir;
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
        final int propSum = calcPropSum(waveMoveDir, rotArr);
        if (propSum > 0) {
            newWaveMoveDir = WaveMoveDirService.createWaveMoveDir(waveMoveDir);
            // Search last zero:
            final int notZeroPos =
                    calcBreakLoopWrap(rotStartPos, rotEndPos, rotDir, pos -> {
                        final WaveMoveCalcDir moveCalcDir = newWaveMoveDir.getMoveCalcDir(rotArr[pos]);
                        final WaveMoveCalcDir beforMoveCalcDir = newWaveMoveDir.getMoveCalcDir(rotArr[wrap(pos - rotDir, rotArr.length)]);
                        return ((moveCalcDir.getDirCalcProp() > 0) && (beforMoveCalcDir.getDirCalcProp() == 0));
                    });
            final int moveAmount = getMoveAmount(rotPercent, propSum);
            final AtomicInteger actMoveAmount = new AtomicInteger(moveAmount);
            // Move propability in given direction until "moveAmount" is zero.
            calcBreakLoopWrap2(notZeroPos, rotArr.length, rotDir, pos -> {
                final WaveMoveCalcDir moveCalcDir = newWaveMoveDir.getMoveCalcDir(rotArr[pos]);
                final WaveMoveCalcDir nextMoveCalcDir = newWaveMoveDir.getMoveCalcDir(rotArr[wrap(pos + rotDir, rotArr.length)]);
                final int prop = moveCalcDir.getDirCalcProp();
                final int newProp, propDif;
                if (prop <= actMoveAmount.get()) {
                    propDif = prop;
                    newProp = 0;
                    actMoveAmount.addAndGet(-prop);
                } else {
                    propDif = actMoveAmount.get();
                    newProp = prop - propDif;
                    actMoveAmount.set(0);
                }
                moveCalcDir.setDirCalcProp(newProp);
                nextMoveCalcDir.addDirCalcProp(propDif);
                if (newProp == 0) {
                    moveCalcDir.setDirCalcPropSum(0);
                }
                return actMoveAmount.get() == 0;
            });
        } else {
            // No outputs to rotate.
            newWaveMoveDir = waveMoveDir;
        }
        return newWaveMoveDir;
    }

    public static int getMoveAmount(int rotPercent, int propSum) {
        final int a = (propSum * rotPercent);
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

    private static int calcPropSum(final WaveMoveDir waveMoveDir, final Cell.Dir[] rotArr) {
        int propSum = 0;
        for (int pos = 0; pos < rotArr.length; pos++) {
            final WaveMoveCalcDir moveCalcDir = waveMoveDir.getMoveCalcDir(rotArr[pos]);
            propSum += moveCalcDir.getDirCalcProp();
        }
        return propSum;
    }
}
