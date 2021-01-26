package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static de.schmiereck.hexMap3D.GridUtils.*;

public class CellStateService {

    private static final int ROT_PERCENT = 1;

    private static class CellStateCacheEntry {
        final CellState inCellState;
        final CellState[] inCellStateArr;
        CellState outCellState;

        public CellStateCacheEntry(CellState inCellState, CellState[] inCellStateArr) {
            this.inCellState = inCellState;
            this.inCellStateArr = inCellStateArr;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.inCellState, Arrays.hashCode(this.inCellStateArr));
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            final CellStateCacheEntry entry = (CellStateCacheEntry) obj;
            return Arrays.equals(this.inCellStateArr, entry.inCellStateArr) &&
                    this.inCellState.equals(entry.inCellState);
        }
    }

    private static final Map<CellStateCacheEntry, CellStateCacheEntry> cellStateCacheSet = new HashMap<>();

    public static boolean useCellStateCache = true;

    public static CellState createInitialCellState() {
        return new CellState();
    }

    public static CellState calcNewStateForTargetCell(final Universe universe, int xPos, int yPos, int zPos, final Cell targetCell) {
        final CellState cellState;

        final CellState[] inCellStateArr = new CellState[Cell.Dir.values().length];

        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final int xDirOffset = calcXDirOffset(xPos, yPos, zPos, calcDir);
            final int yDirOffset = calcYDirOffset(xPos, yPos, zPos, calcDir);
            final int zDirOffset = calcZDirOffset(xPos, yPos, zPos, calcDir);
            final CellState sourceCellState = universe.getCellState(xDirOffset, yDirOffset, zDirOffset);
            inCellStateArr[calcDir.dir()] = sourceCellState;
        }

        if (useCellStateCache) {
            final CellStateCacheEntry newCellStateCacheEntry = new CellStateCacheEntry(targetCell.getCellState(), inCellStateArr);
            final CellStateCacheEntry cellStateCacheEntry = cellStateCacheSet.get(newCellStateCacheEntry);
            if (cellStateCacheEntry != null) {
                cellState = cellStateCacheEntry.outCellState;
            } else {
                cellState = calcNewStateForTargetCell(inCellStateArr);
                newCellStateCacheEntry.outCellState = cellState;
                cellStateCacheSet.put(newCellStateCacheEntry, newCellStateCacheEntry);
            }
        } else {
            cellState = calcNewStateForTargetCell(inCellStateArr);
        }
        return cellState;
    }

    public static CellState calcNewStateForTargetCell(final CellState[] inCellStateArr) {
        final CellState cellState = new CellState();
        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final CellState sourceCellState = inCellStateArr[calcDir.dir()];
            final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(calcDir);
            sourceCellState.getWaveListStream().
                    //.filter(sourceWave -> checkSourceWaveHasOutput(sourceWave, oppositeCalcDir))
                            forEach((sourceWave) -> {
                        final Event sourceEvent = sourceWave.getEvent();
                        final WaveMoveDir waveMoveDir = sourceWave.getWaveMoveDir();
                        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
                        // Source-Cell-Wave is a Particle and is moving in this direction?
                        if ((sourceEvent.getEventType() == 1) &&
                                checkSourceWaveHasOutput(sourceWaveMoveCalc, oppositeCalcDir))
                        {
                            sourceWaveMoveCalc.calcActualDirMoved();
                            final int sourceWaveProb = sourceWave.getWaveProb();
                            //final int probDivider = 1 + 1;
                            final int probDivider = 1 + WaveRotationService.rotationMatrixXYZ.length;
                            final int waveProbDivided1;
                            final int waveProbDivided2;
                            if (sourceWaveProb >= probDivider) {
                                waveProbDivided2 = (sourceWaveProb) / (probDivider);
                                waveProbDivided1 = (sourceWaveProb) - (waveProbDivided2 * (probDivider - 1));
                            } else {
                                waveProbDivided1 = sourceWaveProb;
                                waveProbDivided2 = 0;
                            }
                            {
                                final Wave newTargetWave = WaveService.createNextMovedWave(sourceWave, waveProbDivided1);
                                newTargetWave.calcActualWaveMoveCalcDir();
                                CellService.addWave(cellState, newTargetWave);
                            }
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
                                    newTargetWave.calcActualWaveMoveCalcDir();
                                    CellService.addWave(cellState, newTargetWave);
                                }
                        }
                    });
        }
        return cellState;
    }

    private static boolean checkSourceWaveHasOutput(final WaveMoveCalc waveMoveCalc, final Cell.Dir calcDir) {
        final boolean ret;
        if (calcDir == waveMoveCalc.getActualMoveDir()) {
            ret = (waveMoveCalc.getDirCalcProbSum(calcDir) >= waveMoveCalc.getMaxProb());
        } else {
            ret = false;
        }
        return ret;
    }

    public static int getCellStateCacheSize() {
        return cellStateCacheSet.size();
    }
}
