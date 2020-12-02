package de.schmiereck.hexMap3D.view;

import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;

public class GridViewController {
    private GridViewApplication gridViewApplication;
    private GridViewApplication.RunStepCallback runStepCallback;

    public void init(final GridViewApplication.RunStepCallback runStepCallback, final GridViewApplication gridViewApplication) {
        this.runStepCallback = runStepCallback;
        this.gridViewApplication = gridViewApplication;
    }

    public void handleSubmitButtonAction(final ActionEvent actionEvent) {
        if (Objects.nonNull(this.gridViewApplication)) {
            this.runStepCallback.run();
            this.gridViewApplication.updateReality();
        }
    }

    public void handleShowGridAction(final ActionEvent actionEvent) {
        if (Objects.nonNull(this.gridViewApplication)) {
            final CheckBox checkBox = (CheckBox)actionEvent.getSource();
            this.gridViewApplication.setShowGrid(checkBox.isSelected());
            this.gridViewApplication.calcReality();
            this.gridViewApplication.updateReality();
        }
    }
}
