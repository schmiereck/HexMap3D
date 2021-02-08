package de.schmiereck.hexMap3D.view;

import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GridViewController {
    private GridViewApplication gridViewApplication;
    private GridViewApplication.RunStepCallback runStepCallback;
    private GridViewModel gridViewModel;

    @FXML
    private Label statisticWavesCountLabel;

    @FXML
    private Label statisticCalcRunTimeLabel;

    @FXML
    private Label statisticCalcStepCountLabel;
    
    @FXML
    private ToggleGroup showWaveMoveCalcGroup;

    public void init(final GridViewApplication.RunStepCallback runStepCallback, final GridViewApplication gridViewApplication,
                     final GridViewModel gridViewModel) {
        this.runStepCallback = runStepCallback;
        this.gridViewApplication = gridViewApplication;

        this.gridViewModel = gridViewModel;

        this.statisticWavesCountLabel.textProperty().bind(this.gridViewModel.statisticWavesCountProperty());
        this.statisticCalcRunTimeLabel.textProperty().bind(this.gridViewModel.statisticCalcRunTimeProperty());
        this.statisticCalcStepCountLabel.textProperty().bind(this.gridViewModel.statisticCalcStepCountProperty());
        this.showWaveMoveCalcGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            //RadioButton chk = (RadioButton)newToggle.getToggleGroup().getSelectedToggle();
            final String toggleId = ((RadioButton)newToggle).getId();
            this.gridViewModel.showWaveMoveCalcGroupProperty().set(toggleId);
        });
        this.gridViewModel.showWaveMoveCalcGroupProperty().addListener((observable, oldValue, newValue) -> {
            for (final Toggle toggle : this.showWaveMoveCalcGroup.getToggles()) {
                final String toggleId = ((RadioButton)toggle).getId();
                final boolean selected = toggleId.equals(newValue);
                toggle.setSelected(selected);
            }
        });
    }

    public void handleRunStepAction(final ActionEvent actionEvent) {
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
