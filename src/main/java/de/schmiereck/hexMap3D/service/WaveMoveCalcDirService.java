package de.schmiereck.hexMap3D.service;

public class WaveMoveCalcDirService {

    public static WaveMoveCalcDir createWaveMoveCalcDir(final int dirCalcProp, final int dirCalcPropSum) {
        return new WaveMoveCalcDir(dirCalcProp, dirCalcPropSum);
    }

    public static WaveMoveCalcDir createWaveMoveCalcDir(final WaveMoveCalcDir waveMoveCalcDir) {
        return createWaveMoveCalcDir(waveMoveCalcDir.getDirCalcProp(), waveMoveCalcDir.getDirCalcPropSum());
    }
}
