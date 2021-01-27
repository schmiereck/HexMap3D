package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static de.schmiereck.hexMap3D.GridUtils.*;

public class CellStateService {

    private static final int ROT_PERCENT = 1;

    private static class NextCellStateCacheEntry {
        final CellState inCellState;
        final CellState[] inCellStateArr;
        CellState outCellState;

        public NextCellStateCacheEntry(CellState inCellState, CellState[] inCellStateArr) {
            this.inCellState = inCellState;
            this.inCellStateArr = inCellStateArr;
        }

        private int hashCode = 0;
        @Override
        public int hashCode() {
            if (hashCode == 0) {
                hashCode = Objects.hash(this.inCellState, Arrays.hashCode(this.inCellStateArr));
            }
            return hashCode;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            final NextCellStateCacheEntry entry = (NextCellStateCacheEntry) obj;
            return Arrays.equals(this.inCellStateArr, entry.inCellStateArr) &&
                    this.inCellState.equals(entry.inCellState);
        }
    }

    private static final Map<NextCellStateCacheEntry, NextCellStateCacheEntry> nextCellStateCacheSet = new HashMap<>();
    private static final Map<CellState, CellState> cellStateCacheSet = new HashMap<>();
    private static CellState initialCellStateCache;

    public static boolean useCellStateCache = true;

    public static CellState createInitialCellState() {
        final CellState cellState;
        if (useCellStateCache) {
            if (initialCellStateCache == null) {
                initialCellStateCache = new CellState();
                cellStateCacheSet.put(initialCellStateCache, initialCellStateCache);
                cellState = initialCellStateCache;
            } else {
                cellState = initialCellStateCache;
            }
        } else {
            cellState = new CellState();
        }
        return cellState;
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
            final NextCellStateCacheEntry newNextCellStateCacheEntry = new NextCellStateCacheEntry(targetCell.getCellState(), inCellStateArr);
            final NextCellStateCacheEntry nextCellStateCacheEntry = nextCellStateCacheSet.get(newNextCellStateCacheEntry);
            if (nextCellStateCacheEntry != null) {
                cellState = nextCellStateCacheEntry.outCellState;
            } else {
                final CellState newCellState = calcNewStateForTargetCell(inCellStateArr);
                final CellState cachedCellState = cellStateCacheSet.get(newCellState);
                if (cachedCellState != null) {
                    cellState = cachedCellState;
                } else {
                    cellState = newCellState;
                    cellStateCacheSet.put(cellState, cellState);
                }
                newNextCellStateCacheEntry.outCellState = cellState;
                nextCellStateCacheSet.put(newNextCellStateCacheEntry, newNextCellStateCacheEntry);
            }
        } else {
            cellState = calcNewStateForTargetCell(inCellStateArr);
        }
        return cellState;
    }

    public static boolean useRotationDivider = true;

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
                            //!!!sourceWaveMoveCalc.calcActualDirMoved();
                            final int sourceWaveProb = sourceWave.getWaveProb();
                            //final int probDivider = 1 + 1;
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
                                final Wave newTargetWave = WaveService.createNextRotatedWave(sourceWave, waveProbDivided1);
                                newTargetWave.calcActualWaveMoveCalcDir();
                                addWave(sourceEvent, cellState, newTargetWave);
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
                                        newTargetWave.calcActualWaveMoveCalcDir();
                                        addWave(sourceEvent, cellState, newTargetWave);
                                    }
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

    public static int getNextCellStateCacheSize() {
        return nextCellStateCacheSet.size();
    }

    public static CellState createCellStateWithNewWave(final Event event, final Wave wave) {
        final CellState cellState = new CellState();
        addWave(event, cellState, wave);
        if (useCellStateCache) {
            cellStateCacheSet.put(cellState, cellState);
        }
        return cellState;
    }

    public static void addWave(final Event event, final CellState cellState, final Wave wave) {
        final Wave cellWave = cellState.searchWave(wave);
        if (cellWave != null) {
            cellWave.setWaveProb(wave.getWaveProb() + cellWave.getWaveProb());
        } else {
            cellState.addWave(wave);
            event.addWave(wave);
        }
    }
}
