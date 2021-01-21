package de.schmiereck.hexMap3D.service;

public class WaveMoveDirPropService {

    public static WaveMoveDirProb createWaveMoveCalcDir(final WaveMoveDirProb waveMoveDirProb) {
        return new WaveMoveDirProb(waveMoveDirProb.getDirMoveProb());
    }
}
