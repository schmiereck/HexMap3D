package de.schmiereck.hexMap3D.service;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.service.Cell.Dir.*;
import static de.schmiereck.hexMap3D.service.Engine.DIR_CALC_MAX_PROP;

public class WaveMoveDirTest {

    @org.junit.jupiter.api.Test
    public void test1() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[OR_P.dir()].setDirCalcProp(75);
        moveCalcDirArr[LG_N.dir()].setDirCalcProp(25);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 100; c++) {
            printWaveMoveDir(c, waveMoveDir, 6);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    @org.junit.jupiter.api.Test
    public void test2_1() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[OR_P.dir()].setDirCalcProp(40);
        moveCalcDirArr[LG_N.dir()].setDirCalcProp(30);
        moveCalcDirArr[DB_P.dir()].setDirCalcProp(30);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveDir, 4);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    @org.junit.jupiter.api.Test
    public void test2_2() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[LG_N.dir()].setDirCalcProp(30);
        moveCalcDirArr[OR_P.dir()].setDirCalcProp(40);
        moveCalcDirArr[DB_P.dir()].setDirCalcProp(30);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveDir, 4);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    @org.junit.jupiter.api.Test
    public void test3() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[OR_P.dir()].setDirCalcProp(87);
        moveCalcDirArr[LG_N.dir()].setDirCalcProp(13);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveDir, 7);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    @org.junit.jupiter.api.Test
    public void test4() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(38);
        //moveCalcDirArr[Cell.Dir.OR_N.dir()].setDirCalcProp(12);
        //moveCalcDirArr[Cell.Dir.LG_P.dir()].setDirCalcProp(37);
        //moveCalcDirArr[Cell.Dir.LG_N.dir()].setDirCalcProp(13);
        moveCalcDirArr[OR_P.dir()].setDirCalcProp(40);
        moveCalcDirArr[OR_N.dir()].setDirCalcProp(10);
        moveCalcDirArr[LG_P.dir()].setDirCalcProp(29);//9 38 67 96
        moveCalcDirArr[LG_N.dir()].setDirCalcProp(21);
        //int dirCalcPos = OR_N.dir(); // OR_N
        int dirCalcPos = OR_N.dir(); // OR_N
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveDir, 7);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    private void printWaveMoveDir(int c, WaveMoveDir waveMoveDir, int i) {
        final Cell.Dir actualMoveDir = waveMoveDir.getActualMoveDir();
        final WaveMoveCalcDir actualWaveMoveCalcDir = waveMoveDir.getActualWaveMoveCalcDir();
        if (c % i == 0) System.out.println();
        System.out.printf("%s(%d), ", actualMoveDir, actualWaveMoveCalcDir.getDirCalcPropSum());
    }
}
