package de.schmiereck.hexMap3D.service.reality;

import de.schmiereck.hexMap3D.service.universe.Cell;
import de.schmiereck.hexMap3D.service.universe.Event;
import de.schmiereck.hexMap3D.service.universe.Event.EventType;
import de.schmiereck.hexMap3D.service.universe.Universe;
import de.schmiereck.hexMap3D.service.universe.UniverseService;

import java.util.Arrays;

public class RealityService {

    public static void calcReality(final Universe universe, final Reality reality) {
        reality.setStatisticWaveCount(0);
        reality.getDetectorStream().forEach((detector) -> {
            detector.setMaxWaveProbSum(0);
                });
        UniverseService.forEachCell(reality.getXUniverseSize(), reality.getYUniverseSize(), reality.getZUniverseSize(),
                (final int xPos, final int yPos, final int zPos) -> {
            final RealityCell realityCell = reality.getRealityCell(xPos, yPos, zPos);
            final Cell cell = universe.getCell(xPos, yPos, zPos);

            final int waveCount = (int) cell.getWaveListStream().filter(wave ->
                    (wave.getEvent().getEventType() == EventType.ClassicParticle)// ||
                    //(wave.getEvent().getEventType() == EventType.EasyWave)
            ).count();
            realityCell.addWaveCount(waveCount);

            final int barrierCount = (int) cell.getWaveListStream().filter(wave -> wave.getEvent().getEventType() == EventType.Barrier).count();
            if (barrierCount > 0) {
                realityCell.setBarrier(true);
            }

            final int waveProbSum = cell.getWaveListStream().mapToInt(wave -> wave.getWaveProb()).sum();
            realityCell.setWaveProb(waveProbSum);

            realityCell.setShowGrid(reality.showGrid);

            final int[] outputs = realityCell.getOutputs();
            cell.getWaveListStream().forEach(wave -> {
                reality.incStatisticWaveCount();
                Arrays.stream(Cell.Dir.values()).forEach(dir -> {
                    outputs[dir.dir()] = 0;
                });
            });
            switch (reality.getShowWaveMoveCalc()) {
                case ShowNoWaveMoveDir -> {
                    // Nothing to do.
                }
                case ShowActualWaveMoveCalcDirSum -> {
                    cell.getWaveListStream().forEach(wave -> {
                        final Cell.Dir dir = wave.getActualDirCalcPos();
                        outputs[wave.getActualDirCalcPos().dir()] += wave.getDirCalcProbSum(dir);
                    });
                }
                case ShowAllWaveMoveCalcDirSum -> {
//                cell.getWaveListStream().forEach(wave -> {
//                    Arrays.stream(wave.getMoveCalcDirArr()).forEach(moveCalcDir -> {
//                        outputs[moveCalcDir.getDir().dir()] = moveCalcDir.getDirCalcPropSum();
//                    });
//                });
                    cell.getWaveListStream().forEach(wave -> {
                        Arrays.stream(Cell.Dir.values()).forEach(dir -> {
                            outputs[dir.dir()] += wave.getDirCalcProbSum(dir);
                        });
                    });
                }
                case ShowAllWaveMoveCalcDirProb -> {
                    cell.getWaveListStream().forEach(wave -> {
                        Arrays.stream(Cell.Dir.values()).forEach(dir -> {
                            outputs[dir.dir()] += wave.getDirCalcProb(dir);
                        });
                    });
                }
            }
            reality.getDetectorStream().forEach((detector) -> {
                if ((xPos >= detector.getXPos()) && (yPos >= detector.getYPos()) && (zPos >= detector.getZPos()) &&
                    (xPos <= detector.getXPos() + detector.getXSize()) && (yPos <= detector.getYPos() + detector.getYSize()) && (zPos <= detector.getZPos() + detector.getZSize())) {
                    final DetectorCell detectorCell;
                    if (waveProbSum > 0) {
                        detectorCell = new DetectorCell(waveProbSum);
                        if (waveProbSum > detector.getMaxWaveProbSum()) {
                            detector.setMaxWaveProbSum(waveProbSum);
                        }
                    } else {
                        detectorCell = null;
                    }
                    detector.setDetectorCell(xPos - detector.getXPos(), yPos - detector.getYPos(), zPos - detector.getZPos(),
                            detectorCell);
                }
            });
        });
    }

    public static void clearReality(final Reality reality) {
        UniverseService.forEachCell(reality.getXUniverseSize(), reality.getYUniverseSize(), reality.getZUniverseSize(),
                (final int xPos, final int yPos, final int zPos) -> reality.getRealityCell(xPos, yPos, zPos).clearReality());
    }

    public static void setShowWaveMoveCalc(final Reality reality, final Reality.ShowWaveMoveCalc showWaveMoveCalc) {
        reality.setShowWaveMoveCalc(showWaveMoveCalc);
    }

    public static void setShowGrid(final Reality reality, final boolean showGrid) {
        reality.setShowGrid(showGrid);
    }

    public static int getStatisticWaveCount(final Reality reality) {
        return reality.getStatisticWaveCount();
    }

    public static void setStatisticCalcStepCount(final Reality reality, final long calcStepCount) {
        reality.setStatisticCalcStepCount(calcStepCount);
    }

    public static long getStatisticCalcStepCount(final Reality reality) {
        return reality.getStatisticCalcStepCount();
    }

    public static void setStatisticCalcRunTime(final Reality reality, final long calcRunTime) {
        reality.setStatisticCalcRunTime(calcRunTime);
    }

    public static long getStatisticCalcRunTime(final Reality reality) {
        return reality.getStatisticCalcRunTime();
    }
}
