package de.schmiereck.hexMap3D.service;

import java.util.stream.IntStream;

public class WaveService {

    public static Wave createWave(final Event event, final WaveMoveCalcDir[] moveCalcDirArr) {
        final Wave newWave = new Wave(event, 0, moveCalcDirArr, 0);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createWave(final Wave wave) {
        final Event event = wave.getEvent();
        final Wave newWave = new Wave(event, wave.nextDirCalcPos(), wave.getMoveCalcDirArr(), wave.nextPropCalcPos());
        event.addWave(newWave);
        return newWave;
    }

    public static void adjustDirCalcPropSum(final Wave wave) {
        final WaveMoveDir waveMoveDir = wave.getWaveMoveDir();

        waveMoveDir.adjustDirCalcPropSum();
    }
}
