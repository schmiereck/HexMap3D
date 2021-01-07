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
        final WaveMoveCalcDir[] moveCalcDirArr = wave.getMoveCalcDirArr();

        int maxPropPos = 0;
        int maxPropSum = 0;
        int maxProp = 0;

        for (int pos = 0; pos < moveCalcDirArr.length; pos++) {
            final int propSum = moveCalcDirArr[pos].getDirCalcPropSum();
            final int prop = moveCalcDirArr[pos].getDirCalcProp();
            if (propSum > maxPropSum) {
                maxPropSum = propSum;
            }
            if (prop > maxProp) {
                maxPropPos = pos;
                maxProp = prop;
            }
        }

        if (maxPropSum < Engine.DIR_CALC_MAX_PROP) {
            moveCalcDirArr[maxPropPos].setDirCalcPropSum(Engine.DIR_CALC_MAX_PROP);
        }
    }
}
