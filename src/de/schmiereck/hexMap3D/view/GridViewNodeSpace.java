package de.schmiereck.hexMap3D.view;

import java.util.Objects;
import java.util.stream.IntStream;

import de.schmiereck.hexMap3D.service.Cell;
import de.schmiereck.hexMap3D.service.RealityCell;
import de.schmiereck.hexMap3D.service.Universe;
import javafx.scene.shape.Box;

import static java.util.Objects.nonNull;

public class GridViewNodeSpace {
    private final int xSizeGrid;
    private final int ySizeGrid;
    private final int zSizeGrid;
    private final GridNode[][][] grid;

    public GridViewNodeSpace(final Universe universe, final int xSizeGrid, final int ySizeGrid, final int zSizeGrid) {
        this.xSizeGrid = xSizeGrid;
        this.ySizeGrid = ySizeGrid;
        this.zSizeGrid = zSizeGrid;
        this.grid = new GridNode[xSizeGrid][ySizeGrid][zSizeGrid];

        forEachNode((final int xPos, final int yPos, final int zPos) -> {
            final RealityCell realityCell = universe.getRealityCell(xPos, yPos, zPos);
            this.grid[zPos][yPos][xPos] = new GridNode(realityCell);
        });
    }

    public void updateReality() {
        forEachNode((final int xPos, final int yPos, final int zPos) -> {
            final GridNode gridNode = getGridNode(xPos, yPos, zPos);
            final RealityCell realityCell = gridNode.getRealityCell();
            final Box gridBox = gridNode.getGridBox();
            if (nonNull(gridBox)) {
                if (realityCell.getWaveCount() > 0) {
                    gridBox.setVisible(true);
                } else {
                    gridBox.setVisible(false);
                }
            }
            for (final Box outputBox : gridNode.getOutputArr()) {
                if (nonNull(outputBox)) {
                    outputBox.setVisible(false);
                }
            }
        });
    }

    @FunctionalInterface
    public interface EachNodeCallback {
        void call(final int xPos, final int yPos, final int zPos);
    }

    public void forEachNode(final EachNodeCallback eachNodeCallback) {
        IntStream.range(0, this.zSizeGrid).
                forEach((final int zPos) ->
                        IntStream.range(0, this.ySizeGrid).
                                forEach((final int yPos) ->
                                        IntStream.range(0, this.xSizeGrid).
                                                forEach((final int xPos) -> eachNodeCallback.call(xPos, yPos, zPos))));
    }

    public GridNode getGridNode(final int xPos, final int yPos, final int zPos) {
        return this.grid[zPos][yPos][xPos];
    }
}
