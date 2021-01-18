package de.schmiereck.hexMap3D.service;

public class WaveMoveCalcDirService {

    public static WaveMoveCalcDir createWaveMoveCalcDir(final int dirCalcProp) {
        return new WaveMoveCalcDir(dirCalcProp);
    }

    public static WaveMoveCalcDir createWaveMoveCalcDir(final WaveMoveCalcDir waveMoveCalcDir) {
        return createWaveMoveCalcDir(waveMoveCalcDir.getDirCalcProp());
    }
}
