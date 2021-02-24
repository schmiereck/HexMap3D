package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.GridUtils;
import de.schmiereck.hexMap3D.MapMathUtils;
import de.schmiereck.hexMap3D.service.Engine;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.service.universe.CellStateService.*;
import static de.schmiereck.hexMap3D.service.universe.WaveMoveCalcService.nextDirCalcPos;

/**
 * {@link de.schmiereck.hexMap3D.service.Engine.EngineWaveType#ClassicParticle}.
 */
public class CalcStateClassicService {

    public static int calcWaveMoveDirCalcPos(final int[] moveDirProbArr, final int maxProb, final int[] dirCalcProbSumArr, final int startDirCalcPos) {
        return WaveMoveCalcService.calcWaveMoveDirCalcPosClassic(moveDirProbArr, maxProb, dirCalcProbSumArr, startDirCalcPos);
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
                        if (checkSourceWaveHasMoveOutput(sourceWaveMoveCalc, oppositeCalcDir)) {
                            //!!!sourceWaveMoveCalc.calcActualDirMoved();
                            final int sourceWaveProb = sourceWave.getWaveProb();
                            final int waveProbDivided1;
                            final int waveProbDivided2;
                            if (useRotationDivider) {
                                final int probDivider = 1 + WaveRotationService.rotationMatrixXYZ.length;
                                if (sourceWaveProb >= probDivider) {
                                    waveProbDivided2 = (sourceWaveProb) / (probDivider);
                                    waveProbDivided1 = (sourceWaveProb) - (waveProbDivided2 * (probDivider - 1));
                                } else {
                                    waveProbDivided1 = sourceWaveProb;
                                    waveProbDivided2 = 0;
                                }
                            } else {
                                waveProbDivided1 = sourceWaveProb;
                                waveProbDivided2 = 0;
                            }
                            {
                                final Wave newTargetWave = WaveService.createNextMovedWave(sourceWave, waveProbDivided1);
                                //newTargetWave.calcActualWaveMoveCalcDir();
                                addWave(sourceEvent, newTargetCellState, newTargetWave);
                            }
                            if (useRotationDivider) {
                                for (int rotCalcPos = 0; rotCalcPos < WaveRotationService.rotationMatrixXYZ.length; rotCalcPos++)
                                    if (waveProbDivided2 > 0) {
                                        //final int rotCalcPos = sourceWave.getRotationCalcPos();
                                        final int[] rotationXYZ = WaveRotationService.rotationMatrixXYZ[rotCalcPos];
                                        final int xRotPercent = rotationXYZ[0] * ROT_PERCENT;
                                        final int yRotPercent = rotationXYZ[1] * ROT_PERCENT;
                                        final int zRotPercent = rotationXYZ[2] * ROT_PERCENT;
                                        final Wave newTargetWave =
                                                WaveRotationService.createMoveRotatedWave(sourceWave,
                                                        xRotPercent, yRotPercent, zRotPercent,
                                                        waveProbDivided2);
                                        //newTargetWave.calcActualWaveMoveCalcDir();
                                        addWave(sourceEvent, newTargetCellState, newTargetWave);
                                    }
                            }
                        }
                    });
        }
        return newTargetCellState;
    }
}
