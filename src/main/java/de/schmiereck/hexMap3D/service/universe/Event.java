package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.service.Engine;

import java.util.ArrayList;
import java.util.List;

public class Event {

    public enum EventType {
        Barrier,
        ClassicParticle,
        //EasyWave
    }

    private final List<Wave> waveList[] = new List[2];
    private final Engine engine;
    private final EventType eventType;

    public Event(final Engine engine, final EventType eventType) {
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

    public EventType getEventType() {
        return this.eventType;
    }

    public void removeWave(final Wave wave) {
        this.waveList[this.engine.getNextCalcPos()].remove(wave);
    }

    public void clearWaveList() {
        this.waveList[this.engine.getNextCalcPos()].clear();
    }
}
