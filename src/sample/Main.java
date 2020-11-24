package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import static java.lang.String.format;
import static sample.GridViewUtils.viewGridStepA;
import static sample.GridViewUtils.viewGridStepA2;
import static sample.GridViewUtils.viewGridStepA4;
import static sample.GridViewUtils.viewGridStepH;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //-----------------------------------------------------------------------------
        primaryStage.setTitle("Hello World");

        //-----------------------------------------------------------------------------
        final Group rootGroup = new Group();
        //final Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        rootGroup.setTranslateX(viewGridStepA4);
        rootGroup.setTranslateY(viewGridStepA4);
        rootGroup.setTranslateZ(70);

        //-----------------------------------------------------------------------------
        final int xSizeGrid = 16;
        final int ySizeGrid = 16;
        final int zSizeGrid = 16;
        GridViewUtils.generateViewGrid(rootGroup, xSizeGrid, ySizeGrid, zSizeGrid);

        //-----------------------------------------------------------------------------
        final PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setTranslateX(xSizeGrid / 2 * viewGridStepA);
        camera.setTranslateY(ySizeGrid / 2 * viewGridStepH);
        camera.setTranslateZ(-900.0D);

        camera.setRotationAxis(Rotate.Y_AXIS);
        camera.setRotate(0.0D);

        camera.setFarClip(8000.0D);
        camera.setFieldOfView(50.0D);

        //-----------------------------------------------------------------------------
        final Scene scene = new Scene(rootGroup,
                xSizeGrid * viewGridStepA + viewGridStepA2, ySizeGrid * viewGridStepH,
                true, SceneAntialiasing.BALANCED);

        scene.setCamera(camera);
        primaryStage.setScene(scene);

        //-----------------------------------------------------------------------------
        primaryStage.show();

        //-----------------------------------------------------------------------------
        final MouseLook mouseLook = new MouseLook(camera);
        scene.setOnMousePressed((event) -> {
            event.setDragDetect(true);
        });
        scene.setOnMouseReleased((event) -> {
            //event.setDragDetect(false);
            mouseLook.movedFinished();
        });
        //scene.setOnMouseMoved(new MouseLook(camera));
        scene.setOnMouseDragged((event) -> {
            mouseLook.handle(event);
            event.setDragDetect(false);
        });

        scene.setOnScroll((event) -> {
            mouseLook.handleMouseScrolling(event);
        });
        //-----------------------------------------------------------------------------
    }
}
