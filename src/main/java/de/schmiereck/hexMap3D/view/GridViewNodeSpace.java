package de.schmiereck.hexMap3D.view;

import java.util.Arrays;
import java.util.stream.IntStream;

import de.schmiereck.hexMap3D.service.Cell;
import de.schmiereck.hexMap3D.service.RealityCell;
import de.schmiereck.hexMap3D.service.Universe;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;

import static de.schmiereck.hexMap3D.view.GridViewUtils.viewGridStepA2;
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
            final boolean visible = realityCell.getWaveCount() > 0;
            final Box gridBox = gridNode.getGridBox();
            if (nonNull(gridBox)) {
                gridBox.setVisible(visible);
            }
            {
                final int[] outputs = realityCell.getOutputs();
                final Box[] outputArr = gridNode.getOutputArr();
                Arrays.stream(Cell.Dir.values()).forEach(dir -> {
                    final Box outputBox = outputArr[dir.dir()];
                    final int output = outputs[dir.dir()];
                    if (output > 0) {
                        outputBox.setVisible(true);
                        outputBox.setHeight(((output > 100 ? 100 : output) * viewGridStepA2) / 100.0D);
                    } else {
                        outputBox.setVisible(false);
                    }
                });
            }
            for (final Cylinder connectionCylinder : gridNode.getConnectionList()) {
                connectionCylinder.setVisible(visible);
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
