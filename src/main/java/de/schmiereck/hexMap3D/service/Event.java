package de.schmiereck.hexMap3D.service;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private final List<Wave> waveList[] = new List[2];
    private final Engine engine;
    private final int eventType;

    public Event(final Engine engine, final int eventType) {
        this.engine = engine;
        this.eventType = eventType;
        this.waveList[0] = new ArrayList<>();
        this.waveList[1] = new ArrayList<>();
    }

    public void addWave(final Wave wave) {
        this.waveList[this.engine.getNextCalcPos()].add(wave);
    }

    public List<Wave> getWaveList() {
        return this.waveList[this.engine.getNextCalcPos()];
    }

    public int getEventType() {
        return this.eventType;
    }

    public void removeWave(final Wave wave) {
        this.waveList[this.engine.getNextCalcPos()].remove(wave);
    }

    public void clearWaveList() {
        this.waveList[this.engine.getNextCalcPos()].clear();
    }
}
