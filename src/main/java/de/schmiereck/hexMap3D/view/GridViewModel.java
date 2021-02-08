package de.schmiereck.hexMap3D.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GridViewModel {
    private final StringProperty statisticWavesCount = new SimpleStringProperty("None");
    private final StringProperty statisticCalcRunTime = new SimpleStringProperty("None");
    private final StringProperty statisticCalcStepCount = new SimpleStringProperty("None");
    private final StringProperty showWaveMoveCalcGroup = new SimpleStringProperty("None");

    public static final String SWM_showActualWaveMoveCalcDirSum = "showActualWaveMoveCalcDirSum";
    public static final String SWM_showAllWaveMoveCalcDirSum = "showAllWaveMoveCalcDirSum";
    public static final String SWM_showAllWaveMoveCalcDirProb = "showAllWaveMoveCalcDirProb";
    public static final String SWM_showNoWaveMoveDir = "showNoWaveMoveDir";

                    //---------------------------------------------------------------
    public StringProperty statisticWavesCountProperty() {
        return this.statisticWavesCount;
    }

    public final String getStatisticWavesCount() {
        return statisticWavesCountProperty().get();
    }

    public final void setStatisticWavesCount(final String statisticWavesCount) {
        statisticWavesCountProperty().set(statisticWavesCount);
    }

    //---------------------------------------------------------------
    public StringProperty statisticCalcRunTimeProperty() {
        return this.statisticCalcRunTime;
    }

    public final String getStatisticCalcRunTime() {
        return statisticCalcRunTimeProperty().get();
    }

    public final void setStatisticCalcRunTime(final String statisticCalcRunTime) {
        statisticCalcRunTimeProperty().set(statisticCalcRunTime);
    }

    //---------------------------------------------------------------
    public StringProperty statisticCalcStepCountProperty() {
        return this.statisticCalcStepCount;
    }

    public final String getStatisticCalcStepCount() {
        return statisticCalcStepCountProperty().get();
    }

    public final void setStatisticCalcStepCount(final String statisticCalcStepCount) {
        statisticCalcStepCountProperty().set(statisticCalcStepCount);
    }

    //---------------------------------------------------------------
    public StringProperty showWaveMoveCalcGroupProperty() {
        return this.showWaveMoveCalcGroup;
    }

    public final String getShowWaveMoveCalcGroup() {
        return showWaveMoveCalcGroupProperty().get();
    }

    public final void setShowWaveMoveCalcGroup(final String showWaveMoveCalcGroup) {
        showWaveMoveCalcGroupProperty().set(showWaveMoveCalcGroup);
    }
}
