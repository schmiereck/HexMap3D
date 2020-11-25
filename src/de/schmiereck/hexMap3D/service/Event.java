package de.schmiereck.hexMap3D.service;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private final List<Wave> waveList = new ArrayList<>();
    private final Engine engine;
    private final int eventType;

    public Event(final Engine engine, final int eventType) {
        this.engine = engine;
        this.eventType = eventType;
    }

    public Wave createWave(final int placePulseProb) {
        final Wave wave = new Wave(this, placePulseProb);
        this.addWave(wave);
        return wave;
    }

    public void addWave(final Wave wave) {
        this.waveList.add(wave);
        this.engine.addWave(wave);
    }

    public List<Wave> getWaveList() {
        return this.waveList;
    }

    public int getEventType() {
        return this.eventType;
    }
}
