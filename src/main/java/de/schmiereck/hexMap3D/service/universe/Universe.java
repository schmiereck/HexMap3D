package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.service.reality.RealityCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

public class Universe {
    private final int xUniverseSize;
    private final int yUniverseSize;
    private final int zUniverseSize;
    private final Cell[][][] grid;
    private int calcPos = 0;
    private ArrayList<Event> eventList = new ArrayList<>();

    public Universe(final int xUniverseSize, final int yUniverseSize, final int zUniverseSize) {
        this.xUniverseSize = xUniverseSize;
        this.yUniverseSize = yUniverseSize;
        this.zUniverseSize = zUniverseSize;
        this.grid = new Cell[this.zUniverseSize][this.yUniverseSize][this.xUniverseSize];

        // Initialize grid with Cells.
        UniverseService.forEachCell(this.xUniverseSize, this.yUniverseSize, this.zUniverseSize,
                (final int xPos, final int yPos, final int zPos) -> this.grid[zPos][yPos][xPos] = new Cell(this, xPos, yPos, zPos));
    }

    public void incCalcPos() {
        this.calcPos++;
    }

    public ArrayList<Event> getEventList() {
        return this.eventList;
    }

    @FunctionalInterface
    public interface EachCellCallback {
        void call(final int xPos, final int yPos, final int zPos);
    }

    public int getXUniverseSize() {
        return this.xUniverseSize;
    }

    public int getYUniverseSize() {
        return this.yUniverseSize;
    }

    public int getZUniverseSize() {
        return this.zUniverseSize;
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

    public Cell getCell(final int xPos, final int yPos, final int zPos) {
        return this.grid[wrap(zPos, this.zUniverseSize)][wrap(yPos, this.yUniverseSize)][wrap(xPos, this.xUniverseSize)];
    }

    public CellState getCellState(final int xPos, final int yPos, final int zPos) {
        return this.getCell(xPos, yPos, zPos).getCellState();
    }

    public void addEvent(final Event event) {
        this.eventList.add(event);
    }

    public void addBariere(final Event event, final int x1Pos, final int y1Pos, final int z1Pos, final int x2Pos, final int y2Pos, final int z2Pos) {
        IntStream.rangeClosed(z1Pos, z2Pos).forEach(zPos -> {
            IntStream.rangeClosed(y1Pos, y2Pos).forEach(yPos -> {
                IntStream.rangeClosed(x1Pos, x2Pos).forEach(xPos -> {
                    final Cell cell = this.getCell(xPos, yPos, zPos);
                    final int[] moveCalcDirArr = new int[Cell.Dir.values().length];
                    final WaveMoveDir waveMoveDir = WaveMoveDirService.createWaveMoveDir(moveCalcDirArr);
                    final Wave wave = WaveService.createNewInitialWave(event, waveMoveDir, 1);
                    event.addWave(wave);
                    cell.addWave(wave);
                });
            });
        });
    }
}
