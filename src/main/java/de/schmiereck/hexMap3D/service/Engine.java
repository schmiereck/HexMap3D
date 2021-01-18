package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import static de.schmiereck.hexMap3D.GridUtils.calcXDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcYDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcZDirOffset;

public class Engine {
    public static final int DIR_CALC_MAX_PROP = 100;
    private static final int ROT_PERCENT = 10;

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
        final long startTime = System.currentTimeMillis();

        this.universe.clearReality();

        this.universe.forEachCell((final int xPos, final int yPos, final int zPos) -> {
            final Cell targetCell = this.universe.getCell(xPos, yPos, zPos);
            // Stay (Barrier):
            //{
            //    targetCell.getWaveListStream().forEach((targetWave) -> {
            //        final Event targetEvent = targetWave.getEvent();
            //        // Target-Cell-Wave is a Barrier?
            //        if (targetEvent.getEventType() == 0) {
            //            targetCell.addWave(targetWave);
            //         }
            //    });
            //}
            calcNewStateForTargetCell(xPos, yPos, zPos, targetCell);
        });
        this.universe.calcNext();
        this.universe.calcReality();
        this.runNr++;

        final long endTime = System.currentTimeMillis();
        this.universe.setStatisticCalcRunTime(endTime - startTime);
        this.universe.setStatisticCalcStepCount(this.runNr);
    }

    public static boolean calcOnlyActualWaveMove = true;

    private void calcNewStateForTargetCell(final int xPos, final int yPos, final int zPos, final Cell targetCell) {
        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final Cell sourceCell = this.universe.getCell(calcXDirOffset(xPos, yPos, zPos, calcDir), calcYDirOffset(xPos, yPos, zPos, calcDir), calcZDirOffset(xPos, yPos, zPos, calcDir));
            final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(calcDir);
            sourceCell.getWaveListStream().
                //.filter(sourceWave -> checkSourceWaveHasOutput(sourceWave, oppositeCalcDir))
                forEach((sourceWave) -> {
                final Event sourceEvent = sourceWave.getEvent();
                    final WaveMoveDir waveMoveDir = sourceWave.getWaveMoveDir();
                    final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
                    // Source-Cell-Wave is a Particle and is moving in this direction?
                    if ((sourceEvent.getEventType() == 1) && checkSourceWaveHasOutput(sourceWaveMoveCalc, oppositeCalcDir)) {
                        sourceWaveMoveCalc.calcActualDirMoved();
                        {
                            final Wave newTargetWave = WaveService.createNextMovedWave(sourceWave);
                            targetCell.addWave(newTargetWave);
                        }
                        {
                            final int[] r = WaveRotationService.rotationMatrixXYZ[sourceWave.getRotationCalcPos()];
                            final int xRotPercent = r[0] * ROT_PERCENT;
                            final int yRotPercent = r[1] * ROT_PERCENT;
                            final int zRotPercent = r[2] * ROT_PERCENT;
                            final Wave newTargetWave = WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent);
                            targetCell.addWave(newTargetWave);
                        }
                    }
                });
        }
    }

    private boolean checkIsBarrier(final Cell cell) {
        return cell.getWaveListStream().anyMatch(wave -> wave.getEvent().getEventType() == 0);
    }

    private boolean checkSourceWaveHasOutput(final WaveMoveCalc waveMoveCalc, final Cell.Dir calcDir) {
        final boolean ret;
        final WaveMoveCalcDir sourceWaveActualWaveMoveCalcDir = waveMoveCalc.getActualWaveMoveCalcDir();
        if (calcDir == waveMoveCalc.getActualMoveDir()) {
            if (this.calcOnlyActualWaveMove) {
                ret = (waveMoveCalc.getDirCalcPropSum(calcDir) >= waveMoveCalc.getMaxProp());
            } else {
                ret = true;
            }
        } else {
            ret = false;
        }
        return ret;
    }
}
