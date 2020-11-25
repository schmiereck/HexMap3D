package de.schmiereck.hexMap3D.view;

import java.util.Objects;

import de.schmiereck.hexMap3D.service.Engine;
import javafx.event.ActionEvent;

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
}
