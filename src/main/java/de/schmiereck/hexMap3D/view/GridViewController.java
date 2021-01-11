package de.schmiereck.hexMap3D.view;

import java.util.Objects;

import de.schmiereck.hexMap3D.service.Universe;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

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

    public void showActualWaveMoveCalcDir(ActionEvent actionEvent) {
        if (Objects.nonNull(this.gridViewApplication)) {
            final RadioButton radioButton = (RadioButton)actionEvent.getSource();
            final Universe.ShowWaveMoveCalc showWaveMoveCalc;
            if ("showActualWaveMoveCalcDirSum".equals(radioButton.getId())) {
                showWaveMoveCalc = Universe.ShowWaveMoveCalc.ShowActualWaveMoveCalcDirSum;
            } else {
                if ("showAllWaveMoveCalcDirSum".equals(radioButton.getId())) {
                    showWaveMoveCalc = Universe.ShowWaveMoveCalc.ShowAllWaveMoveCalcDirSum;
                } else {
                    if ("showAllWaveMoveCalcDirProp".equals(radioButton.getId())) {
                        showWaveMoveCalc = Universe.ShowWaveMoveCalc.ShowAllWaveMoveCalcDirProp;
                    } else {
                        throw new RuntimeException("Unexpected showWaveMoveCalc \"" + radioButton.getId() + "\".");
                    }
                }
            }
            this.gridViewApplication.setShowActualWaveMoveCalcDir(showWaveMoveCalc);
            this.gridViewApplication.calcReality();
            this.gridViewApplication.updateReality();
        }
    }
}
