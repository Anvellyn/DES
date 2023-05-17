package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

        public Parent loadFXML() throws IOException {
            return FXMLLoader.load(getClass().getResource("main.fxml"));
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            Parent root = loadFXML();
            primaryStage.setTitle("Kryptografia_Zestaw_1");
            primaryStage.setScene(new Scene(root, 860, 620));
            primaryStage.setResizable(false);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
            byte[] result = DES.encodeMessage("chania".getBytes(), "133457799BBCDFF1", false);
            System.out.println(result.length);
            DES.printBits(result, 8);
            System.out.println(DES.byteArrayToHex(result));
            byte[] encrypted = DES.encodeMessage(DES.hexStringToByteArray(DES.byteArrayToHex(result)),
                    "133457799BBCDFF1", true);
            System.out.println(new String(encrypted));
        }
    }
