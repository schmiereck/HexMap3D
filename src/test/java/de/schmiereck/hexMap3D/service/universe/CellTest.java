package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.service.Engine;
import de.schmiereck.hexMap3D.service.TestUtils;
import de.schmiereck.hexMap3D.service.reality.Reality;
import de.schmiereck.hexMap3D.service.universe.*;

import java.util.HashMap;

import static de.schmiereck.hexMap3D.service.universe.Cell.Dir.*;
import static de.schmiereck.hexMap3D.service.universe.WaveMoveDir.MAX_DIR_PROB;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @org.junit.jupiter.api.Test
    public void test1() {
        final Universe universe = new Universe(1, 1, 1);
        final Reality reality = new Reality(1, 1, 1);
        final Engine engine = new Engine(universe, reality);
        Cell cell = new Cell(universe, 0, 0, 0);
        final Event particleEvent = new Event(engine, 1);
        final Wave wave1;
        {
            final WaveMoveDir waveMoveDir = new WaveMoveDir();
            waveMoveDir.setDirMoveProb(LB_P, MAX_DIR_PROB);
            wave1 = TestUtils.createNewTestWave(particleEvent, waveMoveDir);

            //CellService.addWave(cell, wave1);
            cell.addWave(wave1);
        }
        final Wave wave2;
        {
            final WaveMoveDir waveMoveDir = new WaveMoveDir();
            waveMoveDir.setDirMoveProb(LB_P, MAX_DIR_PROB);
            wave2 = TestUtils.createNewTestWave(particleEvent, waveMoveDir);

            //CellService.addWave(cell, wave2);
        }

        assertEquals(wave1.hashCode(), wave2.hashCode());
        assertTrue(wave1.equals(wave2));
        assertTrue(wave2.equals(wave1));
        {
            HashMap<Wave, Wave> hashMap = new HashMap<>();
            hashMap.put(wave1, wave1);
            assertNotNull(hashMap.get(wave1));
            assertNotNull(hashMap.get(wave2));
        }
        final Wave wave = cell.searchWave(wave2);
        assertNotNull(wave);
    }
}
