import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Stack;


public class Gui extends Application {

    private File selectedFile;
    @Override
    public void start(Stage primaryStage) {




        Button btOpenFileCompress = new Button("Choose a file to compress");
//        btOpenFileCompress.setPadding(new Insets(20, 20, 20, 20));
        Button btOpenFileDecompress = new Button("Choose a file to decompress");

        btOpenFileDecompress.setOnMouseClicked(event -> {
              primaryStage.close();
              Stage stage = new Stage();

             StackPane stackPane = new StackPane();

            Button btFileChooser = new Button("Choose a compress folder");
            btFileChooser.setPadding(new Insets(10,10,10,10));

            VBox vBox = new VBox();
            vBox.getChildren().add(btFileChooser);
            vBox.setPadding(new Insets(200,0,0,200));

            stackPane.getChildren().add(vBox);
            Scene scene = new Scene(stackPane);
            stage.setScene(scene);
            stage.show();

            stage.setWidth(600);
            stage.setHeight(600);

            btFileChooser.setOnMouseClicked(event1 -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Files","*.yip")
                );

                fileChooser.setTitle("Choose a file");

                selectedFile = fileChooser.showOpenDialog(null);
                if(selectedFile!=null){
//                    System.out.println(selectedFile.getAbsolutePath());

                    Decompress de = new Decompress();
                    de.decompress(selectedFile.getAbsolutePath());
                }
            });

        });

        btOpenFileCompress.setOnMouseClicked(event -> {
            primaryStage.close();
            Stage stage = new Stage();

            Pane pane = new Pane();

            Button btFileChooser = new Button("Choose a file");
            Button btFolderChooser = new Button("Choose a folder");



            btFileChooser.setPadding(new Insets(10,10,10,10));
            btFolderChooser.setPadding(new Insets(10,10,10,10));


            HBox hBox = new HBox();
            HBox hBox1 = new HBox();

            hBox.setPadding(new Insets(20,0,20,10));

            hBox.getChildren().add(btFileChooser);
            hBox1.getChildren().add(btFolderChooser);

            VBox vBox = new VBox();
            vBox.getChildren().addAll(hBox,hBox1);
            vBox.setPadding(new Insets(150,0,0,150));



            pane.getChildren().add(vBox);

            btFileChooser.setOnMouseClicked(event1 -> {
                FileChooser fileChooser = new FileChooser();

                fileChooser.setTitle("Choose a file");

                selectedFile = fileChooser.showOpenDialog(null);
                if(selectedFile!=null){
                    long start = System.currentTimeMillis();

                    Compress co = new Compress();
                    co.connect(selectedFile.getName());
                    co.prepairCompress(selectedFile.getAbsolutePath());

                    long end = System.currentTimeMillis();
                    System.out.println("压缩时间为： " + (end - start) + "ms");
                }
            });

            btFolderChooser.setOnMouseClicked(event1 -> {
                DirectoryChooser folderChooser = new DirectoryChooser();

                selectedFile = folderChooser.showDialog(null);
                if(selectedFile!=null){

                    long start = System.currentTimeMillis();

                    Compress co = new Compress();
                    co.connect(selectedFile.getName());
                    co.prepairCompress(selectedFile.getAbsolutePath());
                    long end = System.currentTimeMillis();

                    System.out.println("压缩时间为： " + (end - start) + "ms");
                }
            });


            Scene scene = new Scene(pane);
            stage.setWidth(500);
            stage.setHeight(500);
            stage.setScene(scene);
            stage.show();

        });


        StackPane stackPane = new StackPane();


        HBox hBox = new HBox();
        HBox hBox1 = new HBox();

        hBox.setPadding(new Insets(50,0,50,10));

        hBox.getChildren().add(btOpenFileCompress);
        hBox1.getChildren().add(btOpenFileDecompress);

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(200, 0,0,225));
        //.setPadding(new Insets(20,20,20,20));1
        vBox1.getChildren().addAll(hBox,hBox1);


        stackPane.getChildren().add(vBox1);
//        stackPane.getChildren().add(vBox2);

//        pane.setPadding(new Insets(11,12,11,12));

//        pane.getChildren().add(btOpenFileCompress);
        Scene scene = new Scene(stackPane, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
//        button.addEventHandler();
    }
}
