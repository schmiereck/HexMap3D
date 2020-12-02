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

    public void calcReality(final boolean showActualWaveMoveCalcDir) {
        forEachCell((final int xPos, final int yPos, final int zPos) -> {
            final RealityCell realityCell = this.getRealityCell(xPos, yPos, zPos);
            final Cell cell = this.getCell(xPos, yPos, zPos);

            final int particleCount = (int) cell.getWaveListStream().filter(wave -> wave.getEvent().getEventType() == 1).count();
            realityCell.addWaveCount(particleCount);

            final int barrierCount = (int) cell.getWaveListStream().filter(wave -> wave.getEvent().getEventType() == 0).count();
            if (barrierCount > 0) {
                realityCell.setBarrier(true);
            }

            final int[] outputs = realityCell.getOutputs();
            if (showActualWaveMoveCalcDir) {
                cell.getWaveListStream().forEach(wave -> {
                    final WaveMoveCalcDir waveMoveCalcDir = wave.getActualWaveMoveCalcDir();
                    outputs[waveMoveCalcDir.getDir().dir()] = waveMoveCalcDir.getDirCalcPropSum();
                });
            } else {
                cell.getWaveListStream().forEach(wave -> {
                    Arrays.stream(wave.getMoveCalcDirArr()).forEach(moveCalcDir -> {
                        outputs[moveCalcDir.getDir().dir()] = moveCalcDir.getDirCalcPropSum();
                    });
                });
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
                    cell.addWave(event.createWave(0, moveCalcDirArr));
                });
            });
        });
    }
}