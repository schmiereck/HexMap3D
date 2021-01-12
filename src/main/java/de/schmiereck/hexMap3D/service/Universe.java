package de.schmiereck.hexMap3D.service;

import java.util.Arrays;
import java.util.stream.IntStream;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

public class Universe {
    private final int xUniverseSize;
    private final int yUniverseSize;
    private final int zUniverseSize;
    private final Cell[][][] grid;
    private final RealityCell[][][] realityCellGrid;
    private int calcPos = 0;
    private int statisticWaveCount = 0;
    private long statisticCalcRunTime = 0;

    public enum ShowWaveMoveCalc {
        ShowActualWaveMoveCalcDirSum,
        ShowAllWaveMoveCalcDirSum,
        ShowAllWaveMoveCalcDirProp
    }
    public ShowWaveMoveCalc showWaveMoveCalc = ShowWaveMoveCalc.ShowActualWaveMoveCalcDirSum;
    public boolean showGrid = false;

    public Universe(final int xUniverseSize, final int yUniverseSize, final int zUniverseSize) {
        this.xUniverseSize = xUniverseSize;
        this.yUniverseSize = yUniverseSize;
        this.zUniverseSize = zUniverseSize;
        this.grid = new Cell[this.zUniverseSize][this.yUniverseSize][this.xUniverseSize];
        this.realityCellGrid = new RealityCell[this.zUniverseSize][this.yUniverseSize][this.xUniverseSize];

        // Initialize grid with Cells.
        forEachCell((final int xPos, final int yPos, final int zPos) -> this.grid[zPos][yPos][xPos] = new Cell(this, xPos, yPos, zPos));

        // Populate Cells with Neighbours
        forEachCell((final int xPos, final int yPos, final int zPos) -> this.grid[zPos][yPos][xPos].populateNeigbours());

        forEachCell((final int xPos, final int yPos, final int zPos) -> this.realityCellGrid[zPos][yPos][xPos] = new RealityCell());
    }

    public void setShowWaveMoveCalc(final ShowWaveMoveCalc showWaveMoveCalc) {
        this.showWaveMoveCalc = showWaveMoveCalc;
    }

    @FunctionalInterface
    public interface EachCellCallback {
        void call(final int xPos, final int yPos, final int zPos);
    }

    public void forEachCell(final EachCellCallback eachCellCallback) {
        IntStream.range(0, this.zUniverseSize).
            forEach((final int zPos) ->
                IntStream.range(0, this.yUniverseSize).
                    forEach((final int yPos) ->
                        IntStream.range(0, this.xUniverseSize).
                            forEach((final int xPos) -> eachCellCallback.call(xPos, yPos, zPos))));
    }

    public int getCalcPos() {
        return this.calcPos;
    }

    public int getActCalcPos() {
        return this.calcPos % 2;
    }

    public int getNextCalcPos() {
        return (this.calcPos + 1) % 2;
    }

    public void clearReality() {
        forEachCell((final int xPos, final int yPos, final int zPos) -> this.getRealityCell(xPos, yPos, zPos).clearReality());
    }

    public RealityCell getRealityCell(final int xPos, final int yPos, final int zPos) {
        return this.realityCellGrid[wrap(zPos, this.zUniverseSize)][wrap(yPos, this.yUniverseSize)][wrap(xPos, this.xUniverseSize)];
    }

    public Cell getCell(final int xPos, final int yPos, final int zPos) {
        return this.grid[wrap(zPos, this.zUniverseSize)][wrap(yPos, this.yUniverseSize)][wrap(xPos, this.xUniverseSize)];
    }

    public void calcReality() {
        this.statisticWaveCount = 0;
        forEachCell((final int xPos, final int yPos, final int zPos) -> {
            final RealityCell realityCell = this.getRealityCell(xPos, yPos, zPos);
            final Cell cell = this.getCell(xPos, yPos, zPos);

            final int particleCount = (int) cell.getWaveListStream().filter(wave -> wave.getEvent().getEventType() == 1).count();
            realityCell.addWaveCount(particleCount);

            final int barrierCount = (int) cell.getWaveListStream().filter(wave -> wave.getEvent().getEventType() == 0).count();
            if (barrierCount > 0) {
                realityCell.setBarrier(true);
            }

            realityCell.setShowGrid(this.showGrid);

            final int[] outputs = realityCell.getOutputs();
            cell.getWaveListStream().forEach(wave -> {
                this.statisticWaveCount++;
                Arrays.stream(Cell.Dir.values()).forEach(dir -> {
                    outputs[dir.dir()] = 0;
                });
            });
            switch (this.showWaveMoveCalc) {
                case ShowActualWaveMoveCalcDirSum -> {
                    cell.getWaveListStream().forEach(wave -> {
                        final WaveMoveCalcDir waveMoveCalcDir = wave.getActualWaveMoveCalcDir();
                        outputs[wave.getActualDirCalcPos().dir()] += waveMoveCalcDir.getDirCalcPropSum();
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
                            final WaveMoveCalcDir waveMoveCalcDir = wave.getMoveCalcDirArr()[dir.dir()];
                            outputs[dir.dir()] += waveMoveCalcDir.getDirCalcPropSum();
                        });
                    });
                }
                case ShowAllWaveMoveCalcDirProp -> {
                    cell.getWaveListStream().forEach(wave -> {
                        Arrays.stream(Cell.Dir.values()).forEach(dir -> {
                            final WaveMoveCalcDir waveMoveCalcDir = wave.getMoveCalcDirArr()[dir.dir()];
                            outputs[dir.dir()] += waveMoveCalcDir.getDirCalcProp();
                        });
                    });
                }
            }
        });
    }

    public void calcNext() {
        forEachCell((final int xPos, final int yPos, final int zPos) -> {
            final Cell cell = this.getCell(xPos, yPos, zPos);
            cell.clearWaveList();
        });
        this.calcPos++;
    }

    public void addEvent(final int xPos, final int yPos, final int zPos, final Event event) {
        final Cell cell = this.getCell(xPos, yPos, zPos);

        event.getWaveList().forEach(wave -> cell.addWave(wave));
    }

    public void addBariere(final Event event, final int x1Pos, final int y1Pos, final int z1Pos, final int x2Pos, final int y2Pos, final int z2Pos) {
        IntStream.rangeClosed(z1Pos, z2Pos).forEach(zPos -> {
            IntStream.rangeClosed(y1Pos, y2Pos).forEach(yPos -> {
                IntStream.rangeClosed(x1Pos, x2Pos).forEach(xPos -> {
                    final Cell cell = this.getCell(xPos, yPos, zPos);
                    final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[3];
                    final Wave wave = WaveService.createWave(event, moveCalcDirArr);
                    cell.addWave(wave);
                });
            });
        });
    }

    public void setShowGrid(final boolean showGrid) {
        this.showGrid = showGrid;
    }

    public int getStatisticWaveCount() {
        return this.statisticWaveCount;
    }

    public void setStatisticCalcRunTime(final long calcRunTime) {
        this.statisticCalcRunTime = calcRunTime;
    }

    public long getStatisticCalcRunTime() {
        return this.statisticCalcRunTime;
    }
}
