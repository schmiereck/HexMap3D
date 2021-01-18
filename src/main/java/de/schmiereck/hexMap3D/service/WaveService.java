package de.schmiereck.hexMap3D.service;

public class WaveService {

    public static Wave createNextMovedWave(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNextMovedWave(final Event event, final WaveMoveCalcDir[] moveCalcDirArr) {
        final Wave newWave = new Wave(event, 0, moveCalcDirArr, 0);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNextMovedWave(final Wave wave) {
        final Event event = wave.getEvent();
        final WaveMoveCalc waveMoveCalc = wave.getWaveMoveCalc();
        final Wave newWave = new Wave(event, waveMoveCalc.nextDirCalcPos(), waveMoveCalc.getMoveCalcDirArr(), wave.nextRotationCalcPos());
        event.addWave(newWave);
        return newWave;
    }
}
