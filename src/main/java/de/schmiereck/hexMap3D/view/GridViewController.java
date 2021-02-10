package de.schmiereck.hexMap3D.view;

import java.util.Objects;

import de.schmiereck.hexMap3D.service.reality.Detector;
import de.schmiereck.hexMap3D.service.reality.DetectorCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import static de.schmiereck.hexMap3D.Main.INITIAL_WAVE_PROB;

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

    @FXML
    private GridPane gridPane;

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

    private WritableImage detectorImage;
    private ImageView detectorImageView;

    public void addDetector(final Detector detector) {
        this.detectorImage = new WritableImage(detector.getXDetectorSize(), detector.getYDetectorSize());

        //Reading color from the loaded image
        //PixelReader pixelReader = this.detectorImage.getPixelReader();

        //getting the pixel writer
        final PixelWriter pixelWriter = this.detectorImage.getPixelWriter();

        for (int y = 0; y < detector.getYDetectorSize(); y++) {
            for (int x = 0; x < detector.getXDetectorSize(); x++) {
                pixelWriter.setColor(x, y, Color.BLACK);
            }
        }
        this.detectorImageView = new ImageView(this.detectorImage);

        //Setting the position of the image
        //this.detectorImageView.setX(50);
        //this.detectorImageView.setY(25);

        //setting the fit height and width of the image view
        this.detectorImageView.setFitHeight(120);
        this.detectorImageView.setFitWidth(120);

        //Setting the preserve ratio of the image view
        this.detectorImageView.setPreserveRatio(true);

        final HBox ab = new HBox(2);
        ab.getChildren().addAll(this.detectorImageView);

        this.gridPane.add(ab, 1, 14, 2, 1);
    }

    private boolean useMaxWaveProbSum = true;
    public void updateDetector(final Detector detector) {

        //Reading color from the loaded image
        //PixelReader pixelReader = this.detectorImage.getPixelReader();

        //getting the pixel writer
        final PixelWriter pixelWriter = this.detectorImage.getPixelWriter();

        for (int y = 0; y < detector.getYDetectorSize(); y++) {
            for (int x = 0; x < detector.getXDetectorSize(); x++) {
                final DetectorCell detectorCell = detector.getDetectorCell(x, y);
                final Color color;
                if (detectorCell != null) {
                    final double cc;
                    if (this.useMaxWaveProbSum) {
                        cc = detectorCell.getWaveProbSum() / ((double)detector.getMaxWaveProbSum());
                    } else {
                        cc = detectorCell.getWaveProbSum() / ((double)INITIAL_WAVE_PROB);
                    }
                    color = Color.color(cc, cc, cc);
                } else {
                    color = Color.BLACK;
                }
                pixelWriter.setColor(x, y, color);
            }
        }
    }
}
