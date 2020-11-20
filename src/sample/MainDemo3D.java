package sample;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainDemo3D extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //-----------------------------------------------------------------------------
        primaryStage.setTitle("Hello World");

        //-----------------------------------------------------------------------------
        Box box = new Box();

        box.setWidth(100.0);
        box.setHeight(150.0);
        box.setDepth(100.0);

        box.setTranslateX(125);
        box.setTranslateY(150);
        box.setTranslateZ(200);

        box.setDrawMode(DrawMode.FILL);
        {
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(Color.DARKSLATEBLUE);
            box.setMaterial(material);
        }
        //Setting the rotation animation to the box
        RotateTransition rotateTransition = new RotateTransition();

        //Setting the duration for the transition
        rotateTransition.setDuration(Duration.millis(8000));

        //Setting the node for the transition
        rotateTransition.setNode(box);

        //Setting the axis of the rotation
        rotateTransition.setAxis(Rotate.Y_AXIS);
        //Setting the angle of the rotation
        rotateTransition.setByAngle(360);
        //Setting the cycle count for the transition
        rotateTransition.setCycleCount(2);
        //Setting auto reverse value to false
        rotateTransition.setAutoReverse(false);

        //-----------------------------------------------------------------------------
        Sphere sphere1 = new Sphere();

        //Setting the radius of the Sphere
        sphere1.setRadius(50.0);

        //Setting the position of the sphere
        sphere1.setTranslateX(170);
        sphere1.setTranslateY(180);
        sphere1.setTranslateZ(350);

        sphere1.setDrawMode(DrawMode.LINE);

        //-----------------------------------------------------------------------------
        Cylinder cylinder = new Cylinder();

        cylinder.setHeight(120.0);
        cylinder.setRadius(10.0);

        cylinder.setTranslateX(190);
        cylinder.setTranslateY(180);
        cylinder.setTranslateZ(250);
        {
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(Color.DARKRED);
            cylinder.setMaterial(material);
        }
        cylinder.setDrawMode(DrawMode.FILL);

        cylinder.getTransforms().add(new Rotate(30, 90, 0));

        //-----------------------------------------------------------------------------
        PerspectiveCamera camera = new PerspectiveCamera(false);

        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);

        //-----------------------------------------------------------------------------
        final Group root = new Group();
        //final Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        root.getChildren().add(sphere1);
        root.getChildren().add(box);
        root.getChildren().add(cylinder);

        //-----------------------------------------------------------------------------
        final Scene scene = new Scene(root, 300, 275);

        scene.setCamera(camera);

        primaryStage.setScene(scene);

        //-----------------------------------------------------------------------------
        primaryStage.show();

        rotateTransition.play();
        //-----------------------------------------------------------------------------
    }


    public static void main(String[] args) {
        launch(args);
    }
}
