package de.schmiereck.hexMap3D.service;

public class WaveMoveDirPropService {

    public static WaveMoveDirProp createWaveMoveCalcDir(final WaveMoveDirProp waveMoveDirProp) {
        return new WaveMoveDirProp(waveMoveDirProp.getDirMoveProp());
    }
}
