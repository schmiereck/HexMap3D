package de.schmiereck.hexMap3D.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import de.schmiereck.hexMap3D.GridUtils;

import static de.schmiereck.hexMap3D.GridUtils.calcXDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcYDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcZDirOffset;

public class Engine {
    private final Universe universe;
    private final List<Wave> waveList = new ArrayList<>();
    private long runNr = 0;

    public Engine(final Universe universe) {
        this.universe = universe;
    }

    public void addWave(final Wave wave) {
        this.waveList.add(wave);
    }

    public List<Wave> getWaveList() {
        return this.waveList;
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
            calcNewState(xPos, yPos, zPos, targetCell);
        });
        this.universe.calcNext();
        this.universe.calcReality();
        this.runNr++;
    }

    private void calcNewState(final int xPos, final int yPos, final int zPos, final Cell targetCell) {
        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final Cell sourceCell = this.universe.getCell(calcXDirOffset(xPos, yPos, zPos, calcDir), calcYDirOffset(xPos, yPos, zPos, calcDir), calcZDirOffset(xPos, yPos, zPos, calcDir));
            final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(calcDir);
            sourceCell.getWaveListStream()
                .filter(sourceWave -> checkDirIncluded(sourceWave, oppositeCalcDir))
                .forEach((sourceWave) -> {
                final Event sourceEvent = sourceWave.getEvent();
                // Source-Cell-Wave is a Particle?
                if (sourceEvent.getEventType() == 1) {
                    targetCell.addWave(sourceWave.createWave());
                 }
            });
        }
    }

    /**
     * All.
     */
    private void calcNewState1(final int xPos, final int yPos, final int zPos, final Cell targetCell) {
        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final Cell sourceCell = this.universe.getCell(calcXDirOffset(xPos, yPos, zPos, calcDir), calcYDirOffset(xPos, yPos, zPos, calcDir), calcZDirOffset(xPos, yPos, zPos, calcDir));
            final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(calcDir);
            sourceCell.getWaveListStream()
                    .forEach((sourceWave) -> {
                        final Event sourceEvent = sourceWave.getEvent();
                        // Source-Cell-Wave is a Particle?
                        if (sourceEvent.getEventType() == 1) {
                            targetCell.addWave(sourceWave.createWave());
                        }
                    });
        }
    }

    /**
     * One Dir.
     */
    private void calcNewState2(final int xPos, final int yPos, final int zPos, final Cell targetCell) {
        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final Cell sourceCell = this.universe.getCell(calcXDirOffset(xPos, yPos, zPos, calcDir), calcYDirOffset(xPos, yPos, zPos, calcDir), calcZDirOffset(xPos, yPos, zPos, calcDir));
            final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(calcDir);
            sourceCell.getWaveListStream()
                    .filter(sourceWave -> checkDirIncluded(sourceWave, oppositeCalcDir))
                    .forEach((sourceWave) -> {
                        final Event sourceEvent = sourceWave.getEvent();
                        // Source-Cell-Wave is a Particle?
                        if (sourceEvent.getEventType() == 1) {
                            targetCell.addWave(sourceWave.createWave());
                        }
                    });
        }
     }

    private boolean checkIsBarrier(final Cell cell) {
        return cell.getWaveListStream().anyMatch(wave -> wave.getEvent().getEventType() == 0);
    }

    private boolean checkDirIncluded(final Wave sourceWave, final Cell.Dir calcDir) {
        return Arrays.stream(sourceWave.getDirArr()).anyMatch((dir) -> calcDir.equals(dir));
    }
}
