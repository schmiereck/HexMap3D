package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import static de.schmiereck.hexMap3D.GridUtils.calcXDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcYDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcZDirOffset;

public class Engine {
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
            final CellState cellState = CellStateService.calcNewStateForTargetCell(this.universe, xPos, yPos, zPos, targetCell);
            targetCell.setCellState(cellState);
        });
        WaveMoveCalcService.calcAllDirMoved();
        this.universe.calcNext();
        this.universe.calcReality();
        this.runNr++;

        final long endTime = System.currentTimeMillis();
        this.universe.setStatisticCalcRunTime(endTime - startTime);
        this.universe.setStatisticCalcStepCount(this.runNr);
    }

    private boolean checkIsBarrier(final Cell cell) {
        return cell.getWaveListStream().anyMatch(wave -> wave.getEvent().getEventType() == 0);
    }

    public int getNextCalcPos() {
        return this.universe.getNextCalcPos();
    }
}
