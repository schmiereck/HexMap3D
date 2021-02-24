package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.GridUtils;
import de.schmiereck.hexMap3D.MapMathUtils;
import de.schmiereck.hexMap3D.service.Engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static de.schmiereck.hexMap3D.GridUtils.*;

public class CellStateService {

    protected static final int ROT_PERCENT = 1;

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
    private static int nextCellStateCacheHitCount = 0;
    private static final Map<CellState, CellState> cellStateCacheSet = new HashMap<>();
    private static int cellStateCacheHitCount = 0;
    private static CellState initialCellStateCache;

    public static boolean useCellStateCache = true;

    public static CellState createInitialCellState() {
        final CellState cellState;
        if (useCellStateCache) {
            if (initialCellStateCache == null) {
                initialCellStateCache = createCellState();
                cellStateCacheSet.put(initialCellStateCache, initialCellStateCache);
                cellState = initialCellStateCache;
            } else {
                cellState = initialCellStateCache;
            }
        } else {
            cellState = createCellState();
        }
        return cellState;
    }

    private static CellState createCellState() {
        return new CellState();
    }

    public static CellState calcNewStateForTargetCell(final Universe universe, int xPos, int yPos, int zPos, final Cell targetCell) {
        final CellState cellState;

        final CellState[] inCellStateArr = new CellState[Cell.Dir.values().length];

        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final int xDirPos = calcXDirOffset(xPos, yPos, zPos, calcDir);
            final int yDirPos = calcYDirOffset(xPos, yPos, zPos, calcDir);
            final int zDirPos = calcZDirOffset(xPos, yPos, zPos, calcDir);
            final CellState inSourceCellState = universe.getCellState(xDirPos, yDirPos, zDirPos);
            inCellStateArr[calcDir.dir()] = inSourceCellState;
        }
        final CellState targetInCellState = targetCell.getCellState();

        if (useCellStateCache) {
            final NextCellStateCacheEntry newNextCellStateCacheEntry = new NextCellStateCacheEntry(targetInCellState, inCellStateArr);
            final NextCellStateCacheEntry nextCellStateCacheEntry = nextCellStateCacheSet.get(newNextCellStateCacheEntry);
            if (nextCellStateCacheEntry != null) {
                cellState = nextCellStateCacheEntry.outCellState;
                nextCellStateCacheHitCount++;
            } else {
                final CellState newCellState = calcNewStateForTargetCell(targetInCellState, inCellStateArr);
                final CellState cachedCellState = cellStateCacheSet.get(newCellState);
                if (cachedCellState != null) {
                    cellState = cachedCellState;
                    cellStateCacheHitCount++;
                } else {
                    cellState = newCellState;
                    cellStateCacheSet.put(cellState, cellState);
                }
                newNextCellStateCacheEntry.outCellState = cellState;
                nextCellStateCacheSet.put(newNextCellStateCacheEntry, newNextCellStateCacheEntry);
            }
        } else {
            cellState = calcNewStateForTargetCell(targetInCellState, inCellStateArr);
        }
        return cellState;
    }

    public static boolean useRotationDivider = false;

    public static CellState calcNewStateForTargetCell(final CellState targetInCellState, final CellState[] inCellStateArr) {
        final CellState newTargetCellState;

        switch (Engine.engineWaveType) {
            case ClassicParticle ->
                newTargetCellState = CalcStateClassicService.calcNewStateForTargetCell(targetInCellState, inCellStateArr);
            case WaveDividerTwoWay ->
                newTargetCellState = CalcStateWave1Service.calcNewStateForTargetCell(targetInCellState, inCellStateArr);
           case WaveDividerOneWay ->
                newTargetCellState = CalcStateWave2Service.calcNewStateForTargetCell(targetInCellState, inCellStateArr);
           case WaveDividerClassicMove ->
                newTargetCellState = CalcStateWave3Service.calcNewStateForTargetCell(targetInCellState, inCellStateArr);
            default ->
                throw new RuntimeException("Unexpected engineWaveType \"" + Engine.engineWaveType + "\".");
        }
        return newTargetCellState;
    }

    protected static boolean checkSourceWaveHasMoveOutput(final WaveMoveCalc waveMoveCalc, final Cell.Dir calcDir) {
        final boolean ret;
        if (calcDir == waveMoveCalc.getActualMoveDir()) {
            ret = (waveMoveCalc.getDirCalcProbSum(calcDir) >= waveMoveCalc.getMaxProb());
        } else {
            ret = false;
        }
        return ret;
    }

    protected static boolean checkSourceWaveHasWaveOutput(final WaveMoveCalc waveMoveCalc, final Cell.Dir calcDir, final Cell.Dir oppositeCalcDir) {
        final boolean ret;
        if ((calcDir == waveMoveCalc.getActualMoveDir()) || (oppositeCalcDir == waveMoveCalc.getActualMoveDir())) {
            ret = true;//(waveMoveCalc.getDirCalcProbSum(waveMoveCalc.getDirCalcPos()) >= waveMoveCalc.getMaxProb());
        } else {
            ret = false;
        }
        return ret;
    }

    protected static boolean checkSourceWaveHasWaveOutput(final WaveMoveCalc waveMoveCalc, final Cell.Dir calcDir) {
        final boolean ret;
        if (calcDir == waveMoveCalc.getActualMoveDir()) {
            ret = true;//(waveMoveCalc.getDirCalcProbSum(waveMoveCalc.getDirCalcPos()) >= waveMoveCalc.getMaxProb());
        } else {
            ret = false;
        }
        return ret;
    }

    protected static boolean checkSourceWaveHasWaveOutput(final Wave wave, final Cell.Dir calcDir) {
        final boolean ret;
        if (calcDir.dir() == wave.getWaveDirCalcPos()) {
            ret = true;//(waveMoveCalc.getDirCalcProbSum(waveMoveCalc.getDirCalcPos()) >= waveMoveCalc.getMaxProb());
        } else {
            ret = false;
        }
        return ret;
    }

    public static void resetCacheHitCounts() {
        nextCellStateCacheHitCount = 0;
        cellStateCacheHitCount = 0;
    }

    public static int getCellStateCacheSize() {
        return cellStateCacheSet.size();
    }

    public static int getCellStateCacheHitCount() {
        return cellStateCacheHitCount;
    }

    public static int getNextCellStateCacheSize() {
        return nextCellStateCacheSet.size();
    }

    public static int getNextCellStateCacheHitCount() {
        return nextCellStateCacheHitCount;
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
