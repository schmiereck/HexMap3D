package de.schmiereck.hexMap3D.service;

public class WaveService {

    public static Wave createNextMovedWave(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNewWave(final Event event, final WaveMoveDir waveMoveDir) {
        final WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createWaveMoveCalc(0, WaveMoveDirService.createWaveMoveDir(waveMoveDir.getMoveDirPropArr()));
        final Wave newWave = new Wave(event, waveMoveCalc, 0);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNewWave(final Event event, final WaveMoveDirProp[] moveCalcDirArr) {
        final WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createWaveMoveCalc(0, WaveMoveDirService.createWaveMoveDir(moveCalcDirArr));
        final Wave newWave = new Wave(event, waveMoveCalc, 0);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNextMovedWave(final Wave sourceWave) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createNextWaveMoveCalc(sourceWaveMoveCalc);
        final Wave newWave = new Wave(event, newWaveMoveCalc, sourceWave.nextRotationCalcPos());
        event.addWave(newWave);
        return newWave;
    }
}
