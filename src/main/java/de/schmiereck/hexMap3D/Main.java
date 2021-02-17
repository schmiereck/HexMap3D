package de.schmiereck.hexMap3D;

import de.schmiereck.hexMap3D.service.*;
import de.schmiereck.hexMap3D.service.reality.Detector;
import de.schmiereck.hexMap3D.service.reality.Reality;
import de.schmiereck.hexMap3D.service.universe.*;
import de.schmiereck.hexMap3D.service.universe.Event.EventType;
import de.schmiereck.hexMap3D.view.GridViewApplication;
import javafx.application.Platform;
import javafx.stage.Stage;

import static de.schmiereck.hexMap3D.service.universe.Cell.Dir.*;
import static de.schmiereck.hexMap3D.service.universe.WaveMoveDir.*;
import static java.lang.String.format;

public class Main {

    public static final int xSizeGrid = 3*2 * 4;//16;
    public static final int ySizeGrid = 3*2 * 4;//16;
    public static final int zSizeGrid = 3*2 * 4;//16;
    //public static final int INITIAL_WAVE_PROB = WaveRotationService.rotationMatrixXYZ.length * (32000);
    public static final int INITIAL_WAVE_PROB = WaveRotationService.rotationMatrixXYZ.length * (16000);

    public static void main(String[] args) {
        //-----------------------------------------------------------------------------
        UniverseService.useParallel = true;
        BasicService.setUseCache(true);
        WaveMoveDirService.setUseWaveMoveDirCache(true);
        WaveMoveDirService.setUseRotateMoveDirCache(true);
        WaveMoveCalcService.setUseWaveMoveCalcCache(true);
        CellStateService.useCellStateCache = false;
        CellStateService.useRotationDivider = true;
        Engine.useClassicParticle = true;

        //-----------------------------------------------------------------------------
        final Universe universe = new Universe(xSizeGrid, ySizeGrid, zSizeGrid);
        final Reality reality = new Reality(xSizeGrid, ySizeGrid, zSizeGrid);
        final Engine engine = new Engine(universe, reality);

        //-----------------------------------------------------------------------------
        // Extend Test:
        {
            final Event particleEvent = new Event(engine, EventType.ClassicParticle);

            universe.addEvent(particleEvent);

            final WaveMoveDir waveMoveDir = new WaveMoveDir();
            int dirCalcPos = 0;
            final int x, y, z;

            //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(MAX_DIR_PROB1_2);
            //moveCalcDirArr[Cell.Dir.GR_P.dir()].setDirCalcProp(MAX_DIR_PROB1_2);
            //x=0; y=0; z=8;

            //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(MAX_DIR_PROB);
            //moveCalcDirArr[Cell.Dir.LB_P.dir()].setDirCalcProp(MAX_DIR_PROB);
            //moveCalcDirArr[Cell.Dir.GR_P.dir()].setDirCalcProp(MAX_DIR_PROB);
            //moveCalcDirArr[Cell.Dir.RE_P.dir()].setDirCalcProp(MAX_DIR_PROB);
            //moveCalcDirArr[Cell.Dir.LG_P.dir()].setDirCalcProp(MAX_DIR_PROB);
            //moveCalcDirArr[Cell.Dir.DB_P.dir()].setDirCalcProp(MAX_DIR_PROB);
            //x=8; y=8; z=8;

            //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(MAX_DIR_PROB3_4);
            //moveCalcDirArr[Cell.Dir.LG_N.dir()].setDirCalcProp(MAX_DIR_PROB1_4);
            //moveCalcDirArr[Cell.Dir.DB_P.dir()].setDirCalcProp(0);
            //x=8; y=8; z=8;

            //waveMoveDir.setDirMoveProb(DB_P, MAX_DIR_PROB3_4);
            //waveMoveDir.setDirMoveProb(OR_P, MAX_DIR_PROB1_4);
            //x=0; y=8; z=zSizeGrid/2;

            waveMoveDir.setDirMoveProb(DB_P, MAX_DIR_PROB7_8);
            waveMoveDir.setDirMoveProb(OR_P, MAX_DIR_PROB1_8);
            x=0; y=ySizeGrid/3; z=zSizeGrid/2;
            /*
            waveMoveDir.setDirMoveProb(DB_P, MAX_DIR_PROB);
            x=0; y=ySizeGrid/2; z=zSizeGrid/2;
            */

            final Wave wave = WaveService.createNewInitialWave(particleEvent, waveMoveDir, INITIAL_WAVE_PROB);
            WaveMoveDirService.adjustMaxProb(wave.getWaveMoveDir());
            WaveService.calcActualWaveMoveCalcDir(wave);
            final CellState cellState = CellStateService.createCellStateWithNewWave(particleEvent, wave);
            final Cell cell = universe.getCell(x, y, z);
            //cell.addWave(wave);

            // For Testing:
            // Add a sharp Barrier...
            //final Event barrierEvent = new Event(engine, 0);
            // ...to the right...
            //universe.addBariere(barrierEvent, 10, 0, 0, 11, 15, 15);
            // and to the left.
            //universe.addBariere(barrierEvent, 1, 0, 0, 2, 15, 15);
        }
        //-----------------------------------------------------------------------------
        final GridViewApplication gridViewApplication = new GridViewApplication();

        gridViewApplication.init(() -> engine.run(), universe, reality, xSizeGrid, ySizeGrid, zSizeGrid);

        Platform.startup(() -> {
            // create primary stage
            final Stage primaryStage = new Stage();

            try {
                gridViewApplication.start(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException("JavaFX startup.", e);
            }

            {
                final Detector detector = new Detector(10, 0, 0, 1, ySizeGrid, zSizeGrid);

                reality.addDetector(detector);
                gridViewApplication.addDetector(detector);
            }
        });
         //-----------------------------------------------------------------------------
        engine.runInit();

         //-----------------------------------------------------------------------------
    }
}
