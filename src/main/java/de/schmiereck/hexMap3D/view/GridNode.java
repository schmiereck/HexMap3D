package de.schmiereck.hexMap3D.view;

import java.util.ArrayList;
import java.util.List;

import de.schmiereck.hexMap3D.service.Cell;
import de.schmiereck.hexMap3D.service.RealityCell;
import de.schmiereck.hexMap3D.GridUtils;
import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;

public class GridNode {
    private final RealityCell realityCell;
    private Box gridBox;
    private List<Cylinder> connectionList = new ArrayList<>();
    private Box[] outputArr = new Box[Cell.Dir.values().length];

    public GridNode(final RealityCell realityCell) {
        this.realityCell = realityCell;
    }

    public RealityCell getRealityCell() {
        return this.realityCell;
    }

    public List<Cylinder> getConnectionList() {
        return this.connectionList;
    }

    public Box[] getOutputArr() {
        return this.outputArr;
    }

    public void setGridBox(final Box gridBox) {
        this.gridBox = gridBox;
    }

    public Box getGridBox() {
        return this.gridBox;
    }

    public Node addConnection(final Cylinder connection) {
        this.connectionList.add(connection);
        return connection;
    }

    public Node addOutput(final GridUtils.OutputDir con, final Box output) {
        this.outputArr[con.dir.dir()] = output;
        return output;
    }
}
