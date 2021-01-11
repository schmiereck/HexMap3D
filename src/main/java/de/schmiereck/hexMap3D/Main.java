package de.schmiereck.hexMap3D;

import de.schmiereck.hexMap3D.service.*;
import de.schmiereck.hexMap3D.view.GridViewApplication;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Arrays;

import static java.lang.String.format;

public class Main {

    public static void main(String[] args) {
        //-----------------------------------------------------------------------------
        final int xSizeGrid = 16;
        final int ySizeGrid = 16;
        final int zSizeGrid = 16;

        //-----------------------------------------------------------------------------
        final Universe universe = new Universe(xSizeGrid, ySizeGrid, zSizeGrid);
        final Engine engine = new Engine(universe);

        //-----------------------------------------------------------------------------
        // Extend Test:
        {
            final Event particleEvent = new Event(engine, 1);
            final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
            Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));

            moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(100);

            //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.LB_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.GR_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.RE_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.LG_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.DB_P.dir()].setDirCalcProp(100);

            //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(75);
            //moveCalcDirArr[Cell.Dir.LG_N.dir()].setDirCalcProp(25);
            //moveCalcDirArr[Cell.Dir.DB_P.dir()].setDirCalcProp(0);
            final Wave wave = WaveService.createWave(particleEvent, moveCalcDirArr);
            wave.getWaveMoveDir().adjustMaxProp();
            wave.calcActualWaveMoveCalcDir();

            universe.addEvent(2, 2, 8, particleEvent);

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

        gridViewApplication.init(() -> engine.run(), universe, xSizeGrid, ySizeGrid, zSizeGrid);

        Platform.startup(() -> {
            // create primary stage
            final Stage primaryStage = new Stage();

            try {
                gridViewApplication.start(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException("JavaFX startup.", e);
            }
        });
         //-----------------------------------------------------------------------------
        engine.runInit();

         //-----------------------------------------------------------------------------
    }
}
