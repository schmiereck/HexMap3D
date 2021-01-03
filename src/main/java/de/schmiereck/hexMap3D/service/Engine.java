package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static de.schmiereck.hexMap3D.GridUtils.calcXDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcYDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcZDirOffset;
import static de.schmiereck.hexMap3D.MapLogicUtils.*;
import static de.schmiereck.hexMap3D.MapMathUtils.wrap;
import static de.schmiereck.hexMap3D.MapMathUtils.wrapInclusive;

public class Engine {
    public static final int DIR_CALC_MAX_PROP = 100;

    private final Universe universe;
    private long runNr = 0;

    public Engine(final Universe universe) {
        this.universe = universe;
    }

    public void runInit() {
        this.universe.calcNext();
        this.universe.calcReality();
    }

    public void run() {
        this.universe.clearReality();

        this.universe.forEachCell((final int xPos, final int yPos, final int zPos) -> {
            final Cell targetCell = this.universe.getCell(xPos, yPos, zPos);
            // Stay (Barrier):
            {
                targetCell.getWaveListStream().forEach((targetWave) -> {
                    final Event targetEvent = targetWave.getEvent();
                    // Target-Cell-Wave is a Barrier?
                    if (targetEvent.getEventType() == 0) {
                        targetCell.addWave(targetWave);
                    }
                });
            }
            calcNewStateForTargetCell(xPos, yPos, zPos, targetCell);
        });
        this.universe.calcNext();
        this.universe.calcReality();
        this.runNr++;
    }

    private boolean calcOnlyActualWaveMove = true;

    private void calcNewStateForTargetCell(final int xPos, final int yPos, final int zPos, final Cell targetCell) {
        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final Cell sourceCell = this.universe.getCell(calcXDirOffset(xPos, yPos, zPos, calcDir), calcYDirOffset(xPos, yPos, zPos, calcDir), calcZDirOffset(xPos, yPos, zPos, calcDir));
            final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(calcDir);
            sourceCell.getWaveListStream()
                //.filter(sourceWave -> checkSourceWaveHasOutput(sourceWave, oppositeCalcDir))
                .forEach((sourceWave) -> {
                final Event sourceEvent = sourceWave.getEvent();
                // Source-Cell-Wave is a Particle?
                if ((sourceEvent.getEventType() == 1) && checkSourceWaveHasOutput(sourceWave, oppositeCalcDir)) {
                    final WaveMoveCalcDir actualWaveMoveCalcDir = sourceWave.getActualWaveMoveCalcDir();
                    actualWaveMoveCalcDir.setDirCalcPropSum(actualWaveMoveCalcDir.getDirCalcPropSum() - DIR_CALC_MAX_PROP);
                    final Wave newTargetWave = sourceWave.createWave();
                    if (this.calcOnlyActualWaveMove == true) {
                        newTargetWave.calcActualWaveMoveCalcDir();
                    }
                    targetCell.addWave(newTargetWave);
                 }
            });
        }
    }

    private boolean checkIsBarrier(final Cell cell) {
        return cell.getWaveListStream().anyMatch(wave -> wave.getEvent().getEventType() == 0);
    }

    private boolean checkSourceWaveHasOutput(final Wave sourceWave, final Cell.Dir calcDir) {
        final boolean ret;
        final WaveMoveCalcDir sourceWaveActualWaveMoveCalcDir = sourceWave.getActualWaveMoveCalcDir();
        if (calcDir == sourceWave.getActualDirCalcPos()) {
            if (this.calcOnlyActualWaveMove) {
                ret = (sourceWaveActualWaveMoveCalcDir.getDirCalcPropSum() >= DIR_CALC_MAX_PROP);
            } else {
                ret = true;
            }
        } else {
            ret = false;
        }
        return ret;
    }

    protected static Wave createMoveRotatedWave(final Wave sourceWave,
                                                final int xRotPercent,
                                                final int yRotPercent,
                                                final int zRotPercent) {
        final Wave newWave;

        // Rotate all move outputs in their rotation planes in the given direction.
        // Move the output from cross node or to cross node.
        // If a rotation plane contains only one node create a new node in the given direction.
        // If the cross node is empty create a new cross node in the given direction.

        newWave = sourceWave.createWave();

        if (xRotPercent != 0) {
            for (int axisPos = 0; axisPos < GridUtils.xRotArr.length; axisPos++) {
                final Cell.Dir[] rotArr = GridUtils.xRotArr[axisPos];
                calcRotationOnAxis(xRotPercent, newWave, rotArr);
            }
        }
        if (yRotPercent != 0) {
            for (int axisPos = 0; axisPos < GridUtils.yRotArr.length; axisPos++) {
                final Cell.Dir[] rotArr = GridUtils.yRotArr[axisPos];
                calcRotationOnAxis(yRotPercent, newWave, rotArr);
            }
        }
        return newWave;
    }

    private static void calcRotationOnAxis(int xRotPercent, Wave newWave, Cell.Dir[] rotArr) {
        final int rotDir;
        final int rotStartPos;
        final int rotEndPos;
        final int rotPercent;
        if (xRotPercent > 0) {
            rotPercent = xRotPercent;
            rotDir = +1;
            rotStartPos = 0;
            rotEndPos = rotArr.length - 1;
        } else {
            rotPercent = -xRotPercent;
            rotDir = -1;
            rotStartPos = rotArr.length - 1;
            rotEndPos = 0;
        }
        final int propSum = calcPropSum(newWave, rotArr);
        // Search last zero:
        if (propSum > 0) {
            final int notZeroPos =
                calcBreakLoopWrap(rotStartPos, rotEndPos, rotDir, pos -> {
                    final WaveMoveCalcDir moveCalcDir = newWave.getMoveCalcDir(rotArr[pos]);
                    final WaveMoveCalcDir bevorMoveCalcDir = newWave.getMoveCalcDir(rotArr[wrap(pos - rotDir, rotArr.length)]);
                    return ((moveCalcDir.getDirCalcProp() > 0) && (bevorMoveCalcDir.getDirCalcProp() == 0));
                });
            final int moveAmount = getMoveAmount(rotPercent, propSum);
            final AtomicInteger actMoveAmount = new AtomicInteger(moveAmount);
            // Move propability in given direction until "moveAmount" is zero.
            calcBreakLoopWrap2(notZeroPos, rotArr.length, rotDir, pos -> {
                final WaveMoveCalcDir moveCalcDir = newWave.getMoveCalcDir(rotArr[pos]);
                final WaveMoveCalcDir nextMoveCalcDir = newWave.getMoveCalcDir(rotArr[wrap(pos + rotDir, rotArr.length)]);
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
                return actMoveAmount.get() == 0;
            });
        }
    }

    public static int getMoveAmount(int xRotPercent, int propSum) {
        return (propSum * xRotPercent) / 100;
    }

    private static int calcPropSum(final Wave wave, final Cell.Dir[] rotArr) {
        int propSum = 0;
        for (int pos = 0; pos < rotArr.length; pos++) {
            final WaveMoveCalcDir moveCalcDir = wave.getMoveCalcDir(rotArr[pos]);
            propSum += moveCalcDir.getDirCalcProp();
        }
        return propSum;
    }
}
