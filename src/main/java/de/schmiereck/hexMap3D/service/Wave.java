package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.MapMathUtils;

public class Wave {
    private final Event event;
    private Cell cell;
    //private Cell.Dir dir = null;
    private WaveMoveDir waveMoveDir;
    /**
     * Pos in {@link WaveRotationService#rotationMatrixXYZ}.
     */
    private int propCalcPos;

    public Wave(final Event event, final int dirCalcPos, final WaveMoveCalcDir[] moveCalcDirArr, final int propCalcPos) {
        this.event = event;
        this.waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        this.propCalcPos = propCalcPos;
    }

    public void setCell(final Cell cell) {
        this.cell = cell;
    }

    public Event getEvent() {
        return this.event;
    }

    public int nextDirCalcPos() {
        return this.waveMoveDir.nextDirCalcPos();
    }

    public int nextPropCalcPos() {
        return MapMathUtils.wrap(this.propCalcPos + 1, WaveRotationService.rotationMatrixXYZ.length);
    }

    public void setDir(final Cell.Dir dir, final int dirCalcProp) {
        this.waveMoveDir.setDir(dir, dirCalcProp);
    }

    public WaveMoveCalcDir[] getMoveCalcDirArr() {
        return this.waveMoveDir.getMoveCalcDirArr();
    }

    public WaveMoveCalcDir getMoveCalcDir(final Cell.Dir dir) {
        return this.waveMoveDir.getMoveCalcDir(dir);
    }

    public WaveMoveCalcDir getActualWaveMoveCalcDir() {
        return this.waveMoveDir.getActualWaveMoveCalcDir();
    }

    public void calcActualWaveMoveCalcDir() {
        this.waveMoveDir.calcActualWaveMoveCalcDir();
    }

    public Cell.Dir getActualDirCalcPos() {
        return this.waveMoveDir.getActualMoveDir();
    }

    public int getPropCalcPos() {
        return this.propCalcPos;
    }

    public WaveMoveDir getWaveMoveDir() {
        return this.waveMoveDir;
    }
}
