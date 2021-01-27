package de.schmiereck.hexMap3D.service;

public class TestUtils {

    public static Wave createNewTestWave(final Event event, final WaveMoveDir waveMoveDir) {
        final Wave testWave = WaveService.createNewInitialWave(event, waveMoveDir, 1);
        event.addWave(testWave);
        return testWave;
    }
}
