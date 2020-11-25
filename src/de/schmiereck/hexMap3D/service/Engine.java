package de.schmiereck.hexMap3D.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static de.schmiereck.hexMap3D.GridUtils.calcXDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcYDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcZDirOffset;

public class Engine {
    private final Universe universe;
    private final List<Wave> waveList = new ArrayList<>();

    public Engine(final Universe universe) {
        this.universe = universe;
    }

    public void addWave(final Wave wave) {
        this.waveList.add(wave);
    }

    public List<Wave> getWaveList() {
        return this.waveList;
    }

    public void run() {
        this.universe.clearReality();

        this.universe.forEachCell((final int xPos, final int yPos, final int zPos) -> {
            // Stay (Barrier):
            {
                final Cell sourceCell = this.universe.getCell(xPos, yPos, zPos);
                sourceCell.getWaveListStream().forEach((sourceWave) -> {
                    final Event sourceEvent = sourceWave.getEvent();
                    // Source-Cell-Wave is a Barrier?
                    if (sourceEvent.getEventType() == 0) {
                        sourceCell.addWave(sourceWave);
                    }
                });
            }
            final Cell targetCell = this.universe.getCell(xPos, yPos, zPos);
            for (final Cell.Dir calcDir : Cell.Dir.values()) {
                final Cell sourceCell = this.universe.getCell(calcXDirOffset(xPos, yPos, zPos, calcDir), calcYDirOffset(xPos, yPos, zPos, calcDir), zPos + calcZDirOffset(xPos, yPos, zPos, calcDir));
                sourceCell.getWaveListStream().forEach((sourceWave) -> {
                    final Event sourceEvent = sourceWave.getEvent();
                    // Source-Cell-Wave is a Particle?
                    if (sourceEvent.getEventType() == 1) {
                        calcNewState(targetCell, sourceCell, sourceWave, calcDir);
                     }
                });
            }
        });
        this.universe.calcNext();
        this.universe.calcReality();
    }

    private void calcNewState(final Cell targetCell, final Cell sourceCell, final Wave sourceWave, final Cell.Dir calcDir) {
        if (sourceWave.getExtendCalculated()) {
            // Source-Wave has no target but is already extend?
            // I want to extend 50% to this target cell.
            // Extend 50% to target cell.
            targetCell.addWave(sourceWave.createWave());
            // Have no last Target, than do this 50% also.
            targetCell.addWave(sourceWave.createWave());
        }
    }

    private boolean checkIsBarrier(final Cell cell) {
        return cell.getWaveListStream().anyMatch(wave -> wave.getEvent().getEventType() == 0);
    }
}
