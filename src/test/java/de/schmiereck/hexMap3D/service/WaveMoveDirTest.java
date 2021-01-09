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
        final int stat[] = new int[12];
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 100; c++) {
            printWaveMoveDir(c, waveMoveDir, 6);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
            stat[waveMoveDir.getDirCalcPos()]++;
        }

        System.out.println("\nstat: " + Arrays.toString(stat));
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

    @org.junit.jupiter.api.Test
    public void test5() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[OR_P.dir()].setDirCalcProp(99);
        moveCalcDirArr[LG_N.dir()].setDirCalcProp(1);
        final int stat[] = new int[12];
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 100; c++) {
            printWaveMoveDir(c, waveMoveDir, 10);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
            stat[waveMoveDir.getDirCalcPos()]++;
        }

        System.out.println("\nstat: " + Arrays.toString(stat));
    }

    @org.junit.jupiter.api.Test
    public void test6() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        //Caused by: java.lang.RuntimeException: Do not found next dirCalcPos:
        // [{prop:0, sum:0},
        // {prop:27, sum:34},
        // {prop:40, sum:40},
        // {prop:0, sum:0}, {prop:0, sum:0},
        // {prop:5, sum:10},
        // {prop:10, sum:20},
        // {prop:0, sum:0}, {prop:0, sum:0},
        // {prop:16, sum:32},
        // {prop:0, sum:0},
        // {prop:2, sum:4}]

        //Caused by: java.lang.RuntimeException: Do not found next dirCalcPos:
        // [{prop:0, sum:0},
        // {prop:26, sum:33},
        // {prop:48, sum:48},
        // {prop:0, sum:0}, {prop:0, sum:0},
        // {prop:4, sum:11},
        // {prop:12, sum:21},
        // {prop:0, sum:0}, {prop:0, sum:40},
        // {prop:8, sum:34},
        // {prop:0, sum:0},
        // {prop:2, sum:4}]

        moveCalcDirArr[OR_P.dir()].setDirCalcProp(99);
        moveCalcDirArr[LG_N.dir()].setDirCalcProp(1);
        final int stat[] = new int[12];
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 100; c++) {
            printWaveMoveDir(c, waveMoveDir, 10);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
            stat[waveMoveDir.getDirCalcPos()]++;
        }

        System.out.println("\nstat: " + Arrays.toString(stat));
    }

    private void printWaveMoveDir(int c, WaveMoveDir waveMoveDir, int i) {
        final Cell.Dir actualMoveDir = waveMoveDir.getActualMoveDir();
        final WaveMoveCalcDir actualWaveMoveCalcDir = waveMoveDir.getActualWaveMoveCalcDir();
        if (c % i == 0) System.out.println();
        System.out.printf("%s(%d), ", actualMoveDir, actualWaveMoveCalcDir.getDirCalcPropSum());
    }

    @org.junit.jupiter.api.Test
    public void testAlgo1() {
        int[] p = { 40, 60 };
        int[] s = { 0, 0 };
        int[] stat = { 0, 0 };
        int maxP = 60;
        int pPos = 0;

        for (int cnt = 0; cnt < 100; cnt++) {
            do {
                pPos++;
                if (pPos >= p.length) {
                    pPos = 0;
                }
                s[pPos] += p[pPos];
            } while (s[pPos] < maxP);

            System.out.println(pPos + "|" + p[pPos] + ": " + s[pPos]);
            stat[pPos]++;
            s[pPos] -= maxP;
        }
        // assert:
        System.out.println("stat: " + Arrays.toString(stat));
    }

    @org.junit.jupiter.api.Test
    public void testAlgo2() {
        int[] p = { 38, 12, 37, 13 };
        int[] s = { 0, 0, 0, 0 };
        int[] stat = { 0, 0, 0, 0 };
        int maxP = 38;
        int pPos = 0;

        for (int cnt = 0; cnt < 100; cnt++) {
            do {
                pPos++;
                if (pPos >= p.length) {
                    pPos = 0;
                }
                s[pPos] += p[pPos];
            } while (s[pPos] < maxP);

            System.out.println(pPos + "|" + p[pPos] + ": " + s[pPos]);
            stat[pPos]++;
            s[pPos] -= maxP;
        }
        // assert:
        System.out.println("stat: " + Arrays.toString(stat));
    }
}
