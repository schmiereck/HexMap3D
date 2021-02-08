package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.GridUtils;
import de.schmiereck.hexMap3D.service.Engine;
import de.schmiereck.hexMap3D.service.reality.Reality;
import de.schmiereck.hexMap3D.service.universe.*;

import java.util.List;
import java.util.stream.Collectors;

import static de.schmiereck.hexMap3D.Main.INITIAL_WAVE_PROB;
import static de.schmiereck.hexMap3D.service.universe.Cell.Dir.DB_P;
import static de.schmiereck.hexMap3D.service.TestUtils.assertWaveProps;
import static de.schmiereck.hexMap3D.service.universe.WaveMoveDir.MAX_DIR_PROB;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellStateServiceTest {

    @org.junit.jupiter.api.Test
    public void testCalcNewStateForTargetCell0() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);

        final CellState[] inCellStateArr = new CellState[Cell.Dir.values().length];

        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final CellState sourceCellState = CellStateService.createInitialCellState();
            inCellStateArr[calcDir.dir()] = sourceCellState;
        }

        CellStateService.useCellStateCache = false;

        // Act
        final CellState cellState = CellStateService.calcNewStateForTargetCell(inCellStateArr);

        // Assert
        assertEquals(0, cellState.getWaveListStream().count());
    }

    @org.junit.jupiter.api.Test
    public void testCalcNewStateForTargetCell1() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        universe.addEvent(particleEvent);

        final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(DB_P);
        final CellState[] inCellStateArr = new CellState[Cell.Dir.values().length];

        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final CellState cellState;
             if (oppositeCalcDir == calcDir) {
                 final WaveMoveDir waveMoveDir = new WaveMoveDir();
                 waveMoveDir.setDirMoveProb(DB_P, MAX_DIR_PROB);
                 final Wave wave = WaveService.createNewInitialWave(particleEvent, waveMoveDir, INITIAL_WAVE_PROB);
                 WaveMoveDirService.adjustMaxProb(wave.getWaveMoveDir());
                 WaveService.calcActualWaveMoveCalcDir(wave);
                 cellState = CellStateService.createCellStateWithNewWave(particleEvent, wave);
             } else {
                 cellState = CellStateService.createInitialCellState();
            }
            inCellStateArr[calcDir.dir()] = cellState;
        }

        CellStateService.useCellStateCache = false;

        // Act
        final CellState newCellState = CellStateService.calcNewStateForTargetCell(inCellStateArr);

        // Assert
        assertEquals(13, newCellState.getWaveListStream().count());
        final int expectedProps[][][] = {
                { // 0
                        {       0,    1,    0,   98 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                        {   0,     0,    1,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                        {       0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
                },
                { // 1
                        {       0,    0,    0,   98 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                        {   1,     0,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                        {       0,    0,    0,    1 }  //  Right:      RE_P    DB_N    LB_N    LG_P
                },
                { // 2
                        {       0,    0,    0,   99 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                        {   1,     0,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                        {       0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
                },
                { // 3
                        {       0,    0,    0,   98 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                        {   0,     0,    0,    1    }, // Middle: OR_P     GR_P    OR_N    GR_N
                        {       0,    0,    1,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
                },
        };
        final List<Wave> waveList =
                newCellState.getWaveListStream().
                        sorted((aWave, bWave) -> {
                            return aWave.getWaveMoveDir().hashCode() - bWave.getWaveMoveDir().hashCode();
                        }).
                        collect(Collectors.toList());
        //for (int pos = 0; pos < expectedProps.length; pos++) {
        for (int pos = 0; pos < waveList.size(); pos++) {
           assertWaveProps(waveList.get(pos), expectedProps[pos], "pos:" + pos + " - ");
        }
    }
}
