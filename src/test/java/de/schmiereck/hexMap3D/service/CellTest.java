package de.schmiereck.hexMap3D.service;

import java.util.HashMap;

import static de.schmiereck.hexMap3D.service.Cell.Dir.*;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @org.junit.jupiter.api.Test
    public void test1() {
        Universe universe = new Universe(1, 1, 1);
        Cell cell = new Cell(universe, 0, 0, 0);
        final Engine engine = null;
        final Event particleEvent = new Event(engine, 1);
        final Wave wave1;
        {
            final WaveMoveDir waveMoveDir = new WaveMoveDir();
            waveMoveDir.setDirMoveProb(LB_P, 100);
            wave1 = WaveService.createNewWave(particleEvent, waveMoveDir);

            //CellService.addWave(cell, wave1);
            cell.addWave(wave1);
        }
        final Wave wave2;
        {
            final WaveMoveDir waveMoveDir = new WaveMoveDir();
            waveMoveDir.setDirMoveProb(LB_P, 100);
            wave2 = WaveService.createNewWave(particleEvent, waveMoveDir);

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
