package de.schmiereck.hexMap3D.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GridViewModel {
    private final StringProperty statisticWavesCount = new SimpleStringProperty("None");
    private final StringProperty statisticCalcRunTime = new SimpleStringProperty("None");

    public StringProperty statisticWavesCountProperty() {
        return this.statisticWavesCount;
    }

    public final String getStatisticWavesCount() {
        return statisticWavesCountProperty().get();
    }

    public final void setStatisticWavesCount(final String statisticWavesCount) {
        statisticWavesCountProperty().set(statisticWavesCount);
    }

    public StringProperty statisticCalcRunTimeProperty() {
        return this.statisticCalcRunTime;
    }

    public final String getStatisticCalcRunTime() {
        return statisticCalcRunTimeProperty().get();
    }

    public final void setStatisticCalcRunTime(final String statisticCalcRunTime) {
        statisticCalcRunTimeProperty().set(statisticCalcRunTime);
    }
}
