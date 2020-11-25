package de.schmiereck.hexMap3D.view;

import java.util.Objects;

import javafx.event.ActionEvent;

public class GridViewController {
    private GridViewApplication gridViewApplication;

    public void handleSubmitButtonAction(final ActionEvent actionEvent) {
        if (Objects.nonNull(this.gridViewApplication)) {
            this.gridViewApplication.updateReality();
        }
    }

    public void setGridViewApplication(final GridViewApplication gridViewApplication) {
        this.gridViewApplication = gridViewApplication;
    }
}
