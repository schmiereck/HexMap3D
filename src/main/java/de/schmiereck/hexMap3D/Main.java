package de.schmiereck.hexMap3D;

import de.schmiereck.hexMap3D.service.Cell;
import de.schmiereck.hexMap3D.service.Engine;
import de.schmiereck.hexMap3D.service.Universe;
import de.schmiereck.hexMap3D.service.WaveMoveCalcDir;
import de.schmiereck.hexMap3D.view.GridViewApplication;
import de.schmiereck.hexMap3D.service.Event;
import de.schmiereck.hexMap3D.service.Wave;
import javafx.application.Platform;
import javafx.stage.Stage;

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
            final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[3];
            moveCalcDirArr[0] = new WaveMoveCalcDir(Cell.Dir.OR_P, 75, 100);
            moveCalcDirArr[1] = new WaveMoveCalcDir(Cell.Dir.LG_N, 25, 0);
            moveCalcDirArr[2] = new WaveMoveCalcDir(Cell.Dir.DB_P, 0, 0);
            final Wave wave = particleEvent.createWave(0, moveCalcDirArr);

            universe.addEvent(8, 8, 8, particleEvent);

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
