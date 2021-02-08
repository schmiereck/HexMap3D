package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.service.Engine;
import de.schmiereck.hexMap3D.service.TestUtils;
import de.schmiereck.hexMap3D.service.reality.Reality;
import de.schmiereck.hexMap3D.service.universe.*;

import static de.schmiereck.hexMap3D.service.universe.Cell.Dir.*;
import static de.schmiereck.hexMap3D.service.TestUtils.assertWaveProps;
import static de.schmiereck.hexMap3D.service.universe.WaveMoveDir.MAX_DIR_PROB;
import static de.schmiereck.hexMap3D.service.universe.WaveMoveDir.MAX_DIR_PROB1_2;
import static org.junit.jupiter.api.Assertions.*;

class WaveRotationServiceTest {

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveMiddle0Positive() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 10;
        final int yRotPercent = 0;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps[][] = {
                {     0,    0,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 90,   10,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveMiddle0Positive2() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 1;
        final int yRotPercent = 0;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps[][] = {
                {     0,    0,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 99,    1,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveFirstMiddleNegative() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = -10;
        final int yRotPercent = 0;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps[][] = {
                {     0,    0,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 90,    0,    0,   10    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveSeccondMiddlePositive() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(GR_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 10;
        final int yRotPercent = 0;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps[][] = {
                {     0,    0,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                {  0,   90,   10,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveFirstMiddleYPositive() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 0;
        final int yRotPercent = 10;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps[][] = {
                {     0,    0,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 90,    0,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    0,   10 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveMiddle2YPositive() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_N, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 0;
        final int yRotPercent = 10;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps[][] = {
                {     0,   10,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                {  0,    0,   90,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveMiddle1XPositiveYPositive() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(GR_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 20;
        final int yRotPercent = 10;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedXProps[][] = {
                {     0,    0,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                {  0,   70,   20,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,   10,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedXProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveMiddle0ZPositive() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(OR_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 0;
        final int yRotPercent = 0;
        final int zRotPercent = 10;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps[][] = {
                {     0,    0,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 90,    0,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {    10,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveDB_PyPositive() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(DB_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 0;
        final int yRotPercent = 1;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps[][] = {
                {     0,    0,    0,   99 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                {  1,    0,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveDB_P_xNeg_zNeg() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(DB_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = -1;
        final int yRotPercent = 0;
        final int zRotPercent = -1;

        // Act
        final Wave newWave0 =
                WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);
        final Wave newWave1 =
                WaveRotationService.createMoveRotatedWave(newWave0, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps0[][] = {
                {     0,    1,    0,   98 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                {  0,    0,    1,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave0, expectedProps0, "expectedProps0 - ");
        final int expectedProps1[][] = {
                {     0,    2,    0,   96 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                {  0,    0,    1,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     1,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave1, expectedProps1, "expectedProps1 - ");
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWave25PositiveOneRound() {
        // Arrange
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveDir waveMoveDir = new WaveMoveDir();
        waveMoveDir.setDirMoveProb(LB_P, MAX_DIR_PROB);
        waveMoveDir.setDirMoveProb(OR_P, MAX_DIR_PROB);
        waveMoveDir.setDirMoveProb(RE_P, MAX_DIR_PROB);
        final Wave sourceWave = TestUtils.createNewTestWave(particleEvent, waveMoveDir);
        final int xRotPercent = 25;
        final int yRotPercent = 0;
        final int zRotPercent = 0;

        WaveMoveCalcService.useWaveMoveCalcCache = true;

        // Act
        final Wave newWave1 = WaveRotationService.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent, 2);
        final Wave newWave2 = WaveRotationService.createMoveRotatedWave(newWave1, xRotPercent, yRotPercent, zRotPercent, 2);
        final Wave newWave3 = WaveRotationService.createMoveRotatedWave(newWave2, xRotPercent, yRotPercent, zRotPercent, 2);
        final Wave newWave4 = WaveRotationService.createMoveRotatedWave(newWave3, xRotPercent, yRotPercent, zRotPercent, 2);
        final Wave newWave5 = WaveRotationService.createMoveRotatedWave(newWave4, xRotPercent, yRotPercent, zRotPercent, 2);
        final Wave newWave6 = WaveRotationService.createMoveRotatedWave(newWave5, xRotPercent, yRotPercent, zRotPercent, 2);
        final Wave newWave7 = WaveRotationService.createMoveRotatedWave(newWave6, xRotPercent, yRotPercent, zRotPercent, 2);
        final Wave newWave8 = WaveRotationService.createMoveRotatedWave(newWave7, xRotPercent, yRotPercent, zRotPercent, 2);

        // Assert
        final int expectedProps0[][] = {
                {     100,    0,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 100,     0,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     100,    0,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(sourceWave, expectedProps0);
        final int expectedProps1[][] = {
                {      75,   25,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                {  75,    25,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     75,    25,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave1, expectedProps1);
        final int expectedProps2[][] = {
                {     50,    50,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 50,   50,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     50,    50,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave2, expectedProps2);
        final int expectedProps3[][] = {
                {     25,    75,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 25,   75,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     25,    75,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave3, expectedProps3);
        final int expectedProps4[][] = {
                {     0,    100,    0,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 0,   100,    0,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    100,    0,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave4, expectedProps4);
        final int expectedProps5[][] = {
                {     0,    75,    25,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 0,   75,    25,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    75,    25,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave5, expectedProps5);
        final int expectedProps6[][] = {
                {     0,    50,    50,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 0,   50,    50,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    50,    50,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave6, expectedProps6);
        final int expectedProps7[][] = {
                {     0,    25,    75,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 0,   25,    75,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    25,    75,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave7, expectedProps7);
        final int expectedProps8[][] = {
                {     0,    0,    100,    0 }, //   Left:      LB_P    LG_N    RE_N    DB_P
                { 0,   0,    100,    0    }, // Middle: OR_P     GR_P    OR_N    GR_N
                {     0,    0,    100,    0 }  //  Right:      RE_P    DB_N    LB_N    LG_P
        };
        assertWaveProps(newWave8, expectedProps8);
    }

    @org.junit.jupiter.api.Test
    public void getMoveAmount() {
        // Assert
        assertEquals(100, WaveMoveDirService.getMoveAmount(100, MAX_DIR_PROB));
        assertEquals(50, WaveMoveDirService.getMoveAmount(50, MAX_DIR_PROB));
        assertEquals(10, WaveMoveDirService.getMoveAmount(10, MAX_DIR_PROB));
        assertEquals(1, WaveMoveDirService.getMoveAmount(1, MAX_DIR_PROB));
        assertEquals(0, WaveMoveDirService.getMoveAmount(0, MAX_DIR_PROB));

        assertEquals(-100, WaveMoveDirService.getMoveAmount(-100, MAX_DIR_PROB));
        assertEquals(-50, WaveMoveDirService.getMoveAmount(-50, MAX_DIR_PROB));
        assertEquals(-10, WaveMoveDirService.getMoveAmount(-10, MAX_DIR_PROB));
        assertEquals(-1, WaveMoveDirService.getMoveAmount(-1, MAX_DIR_PROB));

        assertEquals(50, WaveMoveDirService.getMoveAmount(100, MAX_DIR_PROB1_2));
        assertEquals(25, WaveMoveDirService.getMoveAmount(50, MAX_DIR_PROB1_2));
        assertEquals(5, WaveMoveDirService.getMoveAmount(10, MAX_DIR_PROB1_2));
        assertEquals(1, WaveMoveDirService.getMoveAmount(1, MAX_DIR_PROB1_2));
        assertEquals(0, WaveMoveDirService.getMoveAmount(0, MAX_DIR_PROB1_2));

        assertEquals(-50, WaveMoveDirService.getMoveAmount(-100, MAX_DIR_PROB1_2));
        assertEquals(-25, WaveMoveDirService.getMoveAmount(-50, MAX_DIR_PROB1_2));
        assertEquals(-5, WaveMoveDirService.getMoveAmount(-10, MAX_DIR_PROB1_2));
        assertEquals(-1, WaveMoveDirService.getMoveAmount(-1, MAX_DIR_PROB1_2));
    }
}