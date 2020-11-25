package de.schmiereck.hexMap3D.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

    public static int wrap(final int pos, final int range) {
        final int ret;
        if (pos < 0) {
            ret = range + pos;
        } else {
            if (pos >= range) {
                ret = pos - range;
            } else {
                ret = pos;
            }
        }
        return ret;
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
        forEachCell((final int xPos, final int yPos, final int zPos) -> {
            final RealityCell realityCell = this.getRealityCell(xPos, yPos, zPos);
            final Cell cell = this.getCell(xPos, yPos, zPos);
            final int particleCount = (int) cell.getWaveListStream().filter((wave) -> wave.getEvent().getEventType() == 1).count();
            final int barrierCount = (int) cell.getWaveListStream().filter((wave) -> wave.getEvent().getEventType() == 0).count();
            realityCell.addWaveCount(particleCount);
            if (barrierCount > 0) {
                realityCell.setBarrier(true);
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
}
