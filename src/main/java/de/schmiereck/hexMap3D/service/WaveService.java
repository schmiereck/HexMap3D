package de.schmiereck.hexMap3D.service;

public class WaveService {

    public static Wave createWave(final Wave wave) {
        final Event event = wave.getEvent();
        final Wave newWave = new Wave(event, wave.nextDirCalcPos(), wave.getMoveCalcDirArr());
        event.addWave(newWave);
        return wave;
    }
}
