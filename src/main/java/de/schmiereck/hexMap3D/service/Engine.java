package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import static de.schmiereck.hexMap3D.GridUtils.calcXDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcYDirOffset;
import static de.schmiereck.hexMap3D.GridUtils.calcZDirOffset;

public class Engine {
    public static final int DIR_CALC_MAX_PROP = 100;

    public boolean showActualWaveMoveCalcDir = false;

    private final Universe universe;
    private long runNr = 0;

    public Engine(final Universe universe) {
        this.universe = universe;
    }

    public void runInit() {
        this.universe.calcNext();
        this.universe.calcReality(showActualWaveMoveCalcDir);
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
        this.universe.calcReality(showActualWaveMoveCalcDir);
        this.runNr++;
    }

    private void calcNewState(final int xPos, final int yPos, final int zPos, final Cell targetCell) {
        for (final Cell.Dir calcDir : Cell.Dir.values()) {
            final Cell sourceCell = this.universe.getCell(calcXDirOffset(xPos, yPos, zPos, calcDir), calcYDirOffset(xPos, yPos, zPos, calcDir), calcZDirOffset(xPos, yPos, zPos, calcDir));
            final Cell.Dir oppositeCalcDir = GridUtils.calcOppositeDir(calcDir);
            sourceCell.getWaveListStream()
                .filter(sourceWave -> checkSourceWaveHasOutput(sourceWave, oppositeCalcDir))
                .forEach((sourceWave) -> {
                final Event sourceEvent = sourceWave.getEvent();
                // Source-Cell-Wave is a Particle?
                if (sourceEvent.getEventType() == 1) {
                    final WaveMoveCalcDir actualWaveMoveCalcDir = sourceWave.getActualWaveMoveCalcDir();
                    actualWaveMoveCalcDir.setDirCalcPropSum(actualWaveMoveCalcDir.getDirCalcPropSum() - DIR_CALC_MAX_PROP);
                    targetCell.addWave(sourceWave.createWave());
                 }
            });
        }
    }

    private boolean checkIsBarrier(final Cell cell) {
        return cell.getWaveListStream().anyMatch(wave -> wave.getEvent().getEventType() == 0);
    }

    private boolean checkSourceWaveHasOutput(final Wave sourceWave, final Cell.Dir calcDir) {
        final WaveMoveCalcDir actualWaveMoveCalcDir = sourceWave.getActualWaveMoveCalcDir();
        return (actualWaveMoveCalcDir.getDirCalcPropSum() >= DIR_CALC_MAX_PROP) &&
                calcDir.equals(actualWaveMoveCalcDir.getDir());
    }
}
