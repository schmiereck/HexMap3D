package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.service.reality.Reality;
import de.schmiereck.hexMap3D.service.reality.RealityService;
import de.schmiereck.hexMap3D.service.universe.*;
import de.schmiereck.hexMap3D.service.universe.Event.EventType;

public class Engine {
    private final Universe universe;
    private final Reality reality;
    private long runNr = 0;
    public static boolean useClassicParticle = true;
    public static boolean useWaveDividerTwoWay = true;

    public Engine(final Universe universe, final Reality reality) {
        this.universe = universe;
        this.reality = reality;
    }

    public void runInit() {
        UniverseService.calcNext(this.universe);
        RealityService.calcReality(this.universe, this.reality);
    }

    public void run() {
        final long startTime = System.currentTimeMillis();

        RealityService.clearReality(this.reality);
        CellStateService.resetCacheHitCounts();
        WaveMoveCalcService.resetCacheHitCounts();
        WaveMoveDirService.resetCacheHitCounts();

        UniverseService.forEachCell(this.universe.getXUniverseSize(), this.universe.getYUniverseSize(), this.universe.getZUniverseSize(),
                (final int xPos, final int yPos, final int zPos) -> {
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
            final CellState cellState = CellStateService.calcNewStateForTargetCell(this.universe, xPos, yPos, zPos, targetCell);
            targetCell.setCellState(cellState);
        });
        WaveMoveCalcService.calcAllDirMoved();
        UniverseService.calcNext(this.universe);
        RealityService.calcReality(this.universe, this.reality);
        this.runNr++;

        final long endTime = System.currentTimeMillis();
        RealityService.setStatisticCalcRunTime(this.reality, endTime - startTime);
        RealityService.setStatisticCalcStepCount(this.reality, this.runNr);
    }

    private boolean checkIsBarrier(final Cell cell) {
        return cell.getWaveListStream().anyMatch(wave -> wave.getEvent().getEventType() == EventType.Barrier);
    }

    public int getNextCalcPos() {
        return this.universe.getNextCalcPos();
    }
}
