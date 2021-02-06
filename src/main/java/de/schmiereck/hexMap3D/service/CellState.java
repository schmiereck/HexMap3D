package de.schmiereck.hexMap3D.service;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

public class CellState {
    private final AbstractMap<Wave, Wave> waveHashMap;

    public CellState() {
        if (CellStateService.useCellStateCache) {
            this.waveHashMap = new TreeMap();
        } else {
            this.waveHashMap = new HashMap();
        }
    }

    public void addWave(final Wave wave) {
        this.waveHashMap.put(wave, wave);
    }

    public Wave searchWave(final Wave wave) {
        return this.waveHashMap.get(wave);
    }

    public void clearWaveList() {
        //this.waveHashMap[this.universe.getActCalcPos()].values().stream().forEach(wave -> wave.getEvent().removeWave(wave));
        this.waveHashMap.clear();
    }

    public Stream<Wave> getWaveListStream() {
        return this.waveHashMap.values().stream();
    }

    private int hashCode = 0;
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = Objects.hash(this.waveHashMap);
        }
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final CellState entry = (CellState) obj;
        return (this.waveHashMap.equals(entry.waveHashMap));
    }
}
