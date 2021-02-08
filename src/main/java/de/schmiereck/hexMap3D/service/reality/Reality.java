package de.schmiereck.hexMap3D.service.reality;

import de.schmiereck.hexMap3D.service.universe.UniverseService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

public class Reality {
    private final int xUniverseSize;
    private final int yUniverseSize;
    private final int zUniverseSize;
    private final RealityCell[][][] realityCellGrid;

    private int statisticWaveCount = 0;
    private long statisticCalcStepCount = 0;
    private long statisticCalcRunTime = 0;
    private final List<Detector> detectorList = new ArrayList<>();

    public enum ShowWaveMoveCalc {
        ShowNoWaveMoveDir,
        ShowActualWaveMoveCalcDirSum,
        ShowAllWaveMoveCalcDirSum,
        ShowAllWaveMoveCalcDirProb
    }
    public ShowWaveMoveCalc showWaveMoveCalc = ShowWaveMoveCalc.ShowActualWaveMoveCalcDirSum;
    public boolean showGrid = false;

    public Reality(final int xUniverseSize, final int yUniverseSize, final int zUniverseSize) {
        this.xUniverseSize = xUniverseSize;
        this.yUniverseSize = yUniverseSize;
        this.zUniverseSize = zUniverseSize;
        this.realityCellGrid = new RealityCell[this.zUniverseSize][this.yUniverseSize][this.xUniverseSize];

        UniverseService.forEachCell(this.xUniverseSize, this.yUniverseSize, this.zUniverseSize,
                (final int xPos, final int yPos, final int zPos) -> this.realityCellGrid[zPos][yPos][xPos] = new RealityCell());
    }

    public RealityCell getRealityCell(final int xPos, final int yPos, final int zPos) {
        return this.realityCellGrid[wrap(zPos, this.zUniverseSize)][wrap(yPos, this.yUniverseSize)][wrap(xPos, this.xUniverseSize)];
    }

    public int getXUniverseSize() {
        return this.xUniverseSize;
    }

    public int getYUniverseSize() {
        return this.yUniverseSize;
    }

    public int getZUniverseSize() {
        return this.zUniverseSize;
    }

    public RealityCell[][][] getRealityCellGrid() {
        return this.realityCellGrid;
    }

    public int getStatisticWaveCount() {
        return this.statisticWaveCount;
    }

    public void setStatisticWaveCount(final int statisticWaveCount) {
        this.statisticWaveCount = statisticWaveCount;
    }

    public void incStatisticWaveCount() {
        this.statisticWaveCount++;
    }

    public long getStatisticCalcStepCount() {
        return this.statisticCalcStepCount;
    }

    public void setStatisticCalcStepCount(final long statisticCalcStepCount) {
        this.statisticCalcStepCount = statisticCalcStepCount;
    }

    public long getStatisticCalcRunTime() {
        return this.statisticCalcRunTime;
    }

    public void setStatisticCalcRunTime(final long statisticCalcRunTime) {
        this.statisticCalcRunTime = statisticCalcRunTime;
    }

    public ShowWaveMoveCalc getShowWaveMoveCalc() {
        return this.showWaveMoveCalc;
    }

    public void setShowWaveMoveCalc(final ShowWaveMoveCalc showWaveMoveCalc) {
        this.showWaveMoveCalc = showWaveMoveCalc;
    }

    public boolean isShowGrid() {
        return this.showGrid;
    }

    public void setShowGrid(final boolean showGrid) {
        this.showGrid = showGrid;
    }

    public void addDetector(final Detector detector) {
        this.detectorList.add(detector);
    }

    public Stream<Detector> getDetectorStream() {
        return this.detectorList.stream();
    }
}
