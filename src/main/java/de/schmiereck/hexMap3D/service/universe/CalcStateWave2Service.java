package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.GridUtils;
import de.schmiereck.hexMap3D.MapMathUtils;
import de.schmiereck.hexMap3D.service.Engine;

import static de.schmiereck.hexMap3D.service.universe.CellStateService.*;
import static de.schmiereck.hexMap3D.service.universe.CellStateService.addWave;
import static de.schmiereck.hexMap3D.service.universe.WaveMoveCalcService.nextDirCalcPos;

/**
 * {@link de.schmiereck.hexMap3D.service.Engine.EngineWaveType#WaveDividerOneWay}
 */
public class CalcStateWave2Service {

    public static int calcWaveMoveDirCalcPos(final int[] moveDirProbArr, final int maxProb, final int[] dirCalcProbSumArr, final int startDirCalcPos) {
        return WaveMoveCalcService.calcWaveMoveDirCalcPosWave(moveDirProbArr, maxProb, dirCalcProbSumArr, startDirCalcPos);
    }

    public static CellState calcNewStateForTargetCell(final CellState targetInCellState, final CellState[] inCellStateArr) {
        final CellState newTargetCellState = new CellState();
        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final CellState sourceCellState = inCellStateArr[calcDir.dir()];
            final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(calcDir);
            sourceCellState.getWaveListStream()
                    //.filter(sourceWave -> checkSourceWaveHasOutput(sourceWave, oppositeCalcDir))
                    .forEach((sourceWave) -> {
                        final Event sourceEvent = sourceWave.getEvent();
                        final WaveMoveDir waveMoveDir = sourceWave.getWaveMoveDir();
                        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
                        // Source-Cell-Wave is a Particle and is moving in this direction?
                        // Has Move-Output in any direction?
                        if (sourceWaveMoveCalc.getDirCalcProbSum(sourceWaveMoveCalc.getDirCalcPos()) >= sourceWaveMoveCalc.getMaxProb()) {
                            // Has Move-Output in checked direction?
                            if (checkSourceWaveHasMoveOutput(sourceWaveMoveCalc, oppositeCalcDir)) {
                                // Then Move.
                                final int sourceWaveProb = sourceWave.getWaveProb();
                                {
                                    final Wave newTargetWave = WaveService.createNextMovedWave(sourceWave, sourceWaveProb);
                                    //newTargetWave.calcActualWaveMoveCalcDir();
                                    addWave(sourceEvent, newTargetCellState, newTargetWave);
                                }
                            }
                        } else {
                            // If no Output and ActualMoveDir than divide.
                            if (checkSourceWaveHasWaveOutput(sourceWaveMoveCalc, oppositeCalcDir)) {
                                final int sourceWaveProb = MapMathUtils.divide(sourceWave.getWaveProb(), 2,
                                        false);
                                if (sourceWaveProb > 0) {
                                    final Wave newTargetWave = WaveService.createNextDistributedWave(sourceWave, sourceWaveProb);
                                    //newTargetWave.calcActualWaveMoveCalcDir();
                                    addWave(sourceEvent, newTargetCellState, newTargetWave);
                                }
                            }
                        }
                    });
        }
        // Itselve
        targetInCellState.getWaveListStream()
                .forEach((sourceWave) -> {
                    final Event sourceEvent = sourceWave.getEvent();
                    final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();

                    // Has no Move-Output in any direction?
                    if (sourceWaveMoveCalc.getDirCalcProbSum(sourceWaveMoveCalc.getDirCalcPos()) < sourceWaveMoveCalc.getMaxProb()) {
                        final int sourceWaveProb = MapMathUtils.divide(sourceWave.getWaveProb(), 2,
                                true);
                        if (sourceWaveProb > 0) {
                            final Wave newTargetWave = WaveService.createNextMovedWave(sourceWave, sourceWaveProb);
                            //newTargetWave.calcActualWaveMoveCalcDir();
                            addWave(sourceEvent, newTargetCellState, newTargetWave);
                        }
                    }
                });
        return newTargetCellState;
    }
}
