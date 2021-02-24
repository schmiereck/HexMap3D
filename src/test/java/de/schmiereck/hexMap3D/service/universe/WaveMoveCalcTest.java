package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.service.Engine;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.service.universe.Cell.Dir.*;

public class WaveMoveCalcTest {

    @org.junit.jupiter.api.Test
    public void test1() {
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, 75);
        waveMoveDir.setDirMoveProb(LG_N, 25);
        final int stat[] = new int[12];
        int dirCalcPos = 0;
        Engine.engineWaveType = Engine.EngineWaveType.ClassicParticle;
        WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createNewInitalWaveMoveCalc(dirCalcPos, waveMoveDir);
        WaveMoveCalcService.adjustMaxProb(waveMoveCalc);
        WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);

        for (int c = 0; c < 100; c++) {
            printWaveMoveDir(c, waveMoveCalc, 6);

            //!!!waveMoveCalc.calcActualDirMoved();
            waveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(waveMoveCalc);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
            stat[waveMoveCalc.getDirCalcPos()]++;
        }

        System.out.println("\nstat: " + Arrays.toString(stat));
    }

    @org.junit.jupiter.api.Test
    public void test2_1() {
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, 40);
        waveMoveDir.setDirMoveProb(LG_N, 30);
        waveMoveDir.setDirMoveProb(DB_P, 30);
        int dirCalcPos = 0;
        Engine.engineWaveType = Engine.EngineWaveType.ClassicParticle;
        WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createNewInitalWaveMoveCalc(dirCalcPos, waveMoveDir);
        WaveMoveCalcService.adjustMaxProb(waveMoveCalc);
        WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveCalc, 4);

            //!!!waveMoveCalc.calcActualDirMoved();
            waveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(waveMoveCalc);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
        }
    }

    @org.junit.jupiter.api.Test
    public void test2_2() {
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(LG_N, 30);
        waveMoveDir.setDirMoveProb(OR_P, 40);
        waveMoveDir.setDirMoveProb(DB_P, 30);
        int dirCalcPos = 0;
        Engine.engineWaveType = Engine.EngineWaveType.ClassicParticle;
        WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createNewInitalWaveMoveCalc(dirCalcPos, waveMoveDir);
        WaveMoveCalcService.adjustMaxProb(waveMoveCalc);
        WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveCalc, 4);

            //!!!waveMoveCalc.calcActualDirMoved();
            waveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(waveMoveCalc);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
        }
    }

    @org.junit.jupiter.api.Test
    public void test3() {
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, 87);
        waveMoveDir.setDirMoveProb(LG_N, 13);
        int dirCalcPos = 0;
        Engine.engineWaveType = Engine.EngineWaveType.ClassicParticle;
        WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createNewInitalWaveMoveCalc(dirCalcPos, waveMoveDir);
        WaveMoveCalcService.adjustMaxProb(waveMoveCalc);
        WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveCalc, 7);

            //!!!waveMoveCalc.calcActualDirMoved();
            waveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(waveMoveCalc);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
        }
    }

    @org.junit.jupiter.api.Test
    public void test4() {
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        //waveMoveDir.setDirCalcProp(Cell.Dir.OR_P, 38);
        //waveMoveDir.setDirCalcProp(Cell.Dir.OR_N, 12);
        //waveMoveDir.setDirCalcProp(Cell.Dir.LG_P, 37);
        //waveMoveDir.setDirCalcProp(Cell.Dir.LG_N, 13);
        waveMoveDir.setDirMoveProb(OR_P, 40);
        waveMoveDir.setDirMoveProb(OR_N, 10);
        waveMoveDir.setDirMoveProb(LG_P, 29);//9 38 67 96
        waveMoveDir.setDirMoveProb(LG_N, 21);
        //int dirCalcPos = OR_N.dir(); // OR_N
        int dirCalcPos = OR_N.dir(); // OR_N
        Engine.engineWaveType = Engine.EngineWaveType.ClassicParticle;
        WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createNewInitalWaveMoveCalc(dirCalcPos, waveMoveDir);
        WaveMoveCalcService.adjustMaxProb(waveMoveCalc);
        WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveCalc, 7);

            //!!!waveMoveCalc.calcActualDirMoved();
            waveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(waveMoveCalc);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
        }
    }

    @org.junit.jupiter.api.Test
    public void test5() {
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, 99);
        waveMoveDir.setDirMoveProb(LG_N, 1);
        final int stat[] = new int[12];
        int dirCalcPos = 0;
        Engine.engineWaveType = Engine.EngineWaveType.ClassicParticle;
        WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createNewInitalWaveMoveCalc(dirCalcPos, waveMoveDir);
        WaveMoveCalcService.adjustMaxProb(waveMoveCalc);
        WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);

        for (int c = 0; c < 100; c++) {
            printWaveMoveDir(c, waveMoveCalc, 10);

            //!!!waveMoveCalc.calcActualDirMoved();
            waveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(waveMoveCalc);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
            stat[waveMoveCalc.getDirCalcPos()]++;
        }

        System.out.println("\nstat: " + Arrays.toString(stat));
    }

    @org.junit.jupiter.api.Test
    public void test6() {
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
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

        waveMoveDir.setDirMoveProb(OR_P, 99);
        waveMoveDir.setDirMoveProb(LG_N, 1);
        final int stat[] = new int[12];
        int dirCalcPos = 0;
        Engine.engineWaveType = Engine.EngineWaveType.ClassicParticle;
        WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createNewInitalWaveMoveCalc(dirCalcPos, waveMoveDir);
        WaveMoveCalcService.adjustMaxProb(waveMoveCalc);
        WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);

        for (int c = 0; c < 100; c++) {
            printWaveMoveDir(c, waveMoveCalc, 10);

            //!!!waveMoveCalc.calcActualDirMoved();
            waveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(waveMoveCalc);
            WaveMoveCalcService.calcActualWaveMoveCalcDir(waveMoveCalc);
            stat[waveMoveCalc.getDirCalcPos()]++;
        }

        System.out.println("\nstat: " + Arrays.toString(stat));
    }

    private void printWaveMoveDir(int c, WaveMoveCalc waveMoveCalc, int i) {
        final Cell.Dir actualMoveDir = waveMoveCalc.getActualMoveDir();
        final int actualWaveMoveDirProb = waveMoveCalc.getActualWaveMoveCalcDir();
        if (c % i == 0) System.out.println();
        System.out.printf("%s(%d), ", actualMoveDir, waveMoveCalc.getDirCalcProbSum(actualMoveDir));
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
