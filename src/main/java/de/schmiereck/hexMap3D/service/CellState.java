package de.schmiereck.hexMap3D.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class CellState {
    private final HashMap<Wave, Wave> waveHashArr = new HashMap();

    public CellState() {
    }

    public void addWave(final Wave wave) {
        this.waveHashArr.put(wave, wave);
    }

    public Wave searchWave(final Wave wave) {
        return this.waveHashArr.get(wave);
    }

    public void clearWaveList() {
        //this.waveHashArr[this.universe.getActCalcPos()].values().stream().forEach(wave -> wave.getEvent().removeWave(wave));
        this.waveHashArr.clear();
    }

    public Stream<Wave> getWaveListStream() {
        return this.waveHashArr.values().stream();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.waveHashArr.hashCode());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final CellState entry = (CellState) obj;
        return (this.waveHashArr.equals(entry.waveHashArr));
    }
}
