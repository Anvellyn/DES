package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Controller extends Window {
    final FileChooser fileChooser = new FileChooser();

    @FXML
    public GridPane root = new GridPane();
    @FXML
    public GridPane firstTable = new GridPane();
    @FXML
    public GridPane secondTable = new GridPane();
    @FXML
    public GridPane menuBar = new GridPane();

    @FXML
    public Label valueHEXKey = new Label();
    @FXML
    public Label algorithmDES = new Label();
    @FXML
    public Label loadFileKey = new Label();
    @FXML
    public Label saveKeyToFile = new Label();
    @FXML
    public Label encryptLabel = new Label();
    @FXML
    public Label loadTextWithoutEncryption = new Label();
    @FXML
    public Label loadFileEncrypted = new Label();
    @FXML
    public Label savePublicTextToFile = new Label();
    @FXML
    public Label saveEncryptedLabel = new Label();
    @FXML
    public Label komunikat1 = new Label();
    @FXML
    public Label komunikat2 = new Label();

    @FXML
    public TextField keyLoad = new TextField();
    @FXML
    public TextField valueKeyText = new TextField();
    @FXML
    public TextField nameOfKeyFile = new TextField();
    @FXML
    public TextField publicText = new TextField();
    @FXML
    public TextField encryptedText = new TextField();
    @FXML
    public TextField nameOfFileWithoutEncryption = new TextField();
    @FXML
    public TextField nameOfFileWithoutEncryption1 = new TextField();
    @FXML
    public TextField nameOfFileWithPublicText = new TextField();
    @FXML
    public TextField nameOfFileEncryptedFile = new TextField();
    @FXML
    public TextField nameOfFileEncryptedFile1 = new TextField();
    @FXML
    public TextField encryptedFileName = new TextField();

    @FXML
    public Button loadKeyButton = new Button();
    @FXML
    public Button generateKey = new Button();
    @FXML
    public Button saveKeyFile = new Button();
    @FXML
    public Button encryptButton1 = new Button();
    @FXML
    public Button decryptButton = new Button();
    @FXML
    public Button encryptButton = new Button();
    @FXML
    public Button decryptButton1 = new Button();
    @FXML
    public Button savePublicTextButton = new Button();
    @FXML
    public Button saveEncryptedFile = new Button();

    public void loadKeyAction(ActionEvent actionEvent) {
        try {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(this);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String input;
            List<String> readLines = new ArrayList<>();
            while ((input = bufferedReader.readLine()) != null) {
                readLines.add(input);
            }
            valueKeyText.setText(readLines.get(0));
            keyLoad.setText("Otworzono: " + file.getName());
        } catch (NullPointerException | IOException e) {
            keyLoad.setText("Nie można otworzyć pliku!");
        }
    }

    public void saveKeyAction(ActionEvent actionEvent) {
        try {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showSaveDialog(this);
            FileWriter newFile = new FileWriter(file);
            newFile.write(valueKeyText.getText());
            newFile.close();
            nameOfKeyFile.setText("Zapisano do pliku: " + file.getName());
        } catch (NullPointerException | IOException e) {
            nameOfKeyFile.setText("Nie można zapisać do pliku!");
        }
    }

    public void generateKey(ActionEvent actionEvent) {
        valueKeyText.setText(DES.wygenerujKlucz());
    }

    public void encrypt(ActionEvent actionEvent) {
        try {
            if (valueKeyText.getText().equals("")) {
                valueKeyText.setText(DES.wygenerujKlucz());
            }
            encryptedText.setText(DES.byteArrayToHex(DES.encodeMessage(publicText.getText().getBytes(),
                    valueKeyText.getText(),
                    false)));
        } catch (NumberFormatException e) {
            publicText.setText("Nie podano wiadomości!");
            publicText.clear();
        }
    }

    public void decrypt(ActionEvent actionEvent) {
        try {
            String message = encryptedText.getText();
            publicText.setText(new String(DES.encodeMessage(DES.hexStringToByteArray(message),
                    valueKeyText.getText(),
                    true)));
        } catch (NumberFormatException e) {
            encryptedText.setText("Nie podano szyfrogramu!");
        }
    }

    public void loadEncryptedFile(ActionEvent actionEvent) {
        try {
            File file = fileChooser.showOpenDialog(this);
            FileReader fileReader = new FileReader(file);
            encryptedText.setText(fileReader.toString());
            nameOfFileEncryptedFile.setText(file.getAbsolutePath());
            komunikat2.setText("Otworzono: " + file.getName());
        } catch (NullPointerException | FileNotFoundException e) {
            encryptedFileName.setText("Nie podano pliku z szyfrogramem!");
        }
    }

    public void loadFileWithPublicText(ActionEvent actionEvent) {
        try {
            File file = fileChooser.showOpenDialog(this);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String input;
            List<String> readLines = new ArrayList<>();
            while ((input = bufferedReader.readLine()) != null) {
                readLines.add(input);
            }
            StringBuilder result = new StringBuilder();
            for (String readLine : readLines) {
                result.append(readLine).append(" \n");
            }
            nameOfFileWithoutEncryption.setText(file.getAbsolutePath());
            komunikat1.setText("Otworzono: " + file.getName());
        } catch (NullPointerException | IOException e) {
            nameOfFileWithoutEncryption.setText("Nie znaleziono pliku!");
        }
    }

    public void saveEncryptedFile(ActionEvent actionEvent) {
        saveFile(encryptedText);
    }

    public void saveFileWithPublicText(ActionEvent actionEvent) {
        saveFile(publicText);
    }

    public void saveFile(TextField textField) {
        try {
            File file = fileChooser.showSaveDialog(this);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(textField.getText());
            fileWriter.close();
            nameOfFileEncryptedFile.setText("Zapisano do pliku: " + file.getName());
            System.out.println(file.lastModified());
        } catch (NullPointerException | IOException e) {
            nameOfFileEncryptedFile.setText("Nie można zapisać pliku!");
        }
    }

    public void encrypt1(ActionEvent actionEvent) {
        try {
            File toOpenFile = fileChooser.showOpenDialog(this);
            if (valueKeyText.getText().equals("")) {
                valueKeyText.setText(DES.wygenerujKlucz());
            }
            File newFile = fileChooser.showSaveDialog(this);
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(DES.encodeMessage(Files.readAllBytes(toOpenFile.toPath()), valueKeyText.getText(), false));
            fos.close();
            nameOfFileWithoutEncryption.setText(toOpenFile.getAbsolutePath());
            nameOfFileEncryptedFile.setText(newFile.getAbsolutePath());
            komunikat2.setText("Zaszyfrowany plik zapisano do: " + newFile.getName());
        } catch (NullPointerException | IOException e) {
            komunikat2.setText("Nie można odczytać pliku");
        }
    }

    public void decrypt1(ActionEvent actionEvent) {
        try {
            File openFile = fileChooser.showOpenDialog(this);
            if (valueKeyText.getText().equals("")) {
                valueKeyText.setText(DES.wygenerujKlucz());
            }
            File newFile = fileChooser.showSaveDialog(this);
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(DES.encodeMessage(Files.readAllBytes(openFile.toPath()), valueKeyText.getText(), true));
            fos.close();
            nameOfFileWithoutEncryption1.setText(openFile.getAbsolutePath());
            nameOfFileEncryptedFile1.setText(newFile.getAbsolutePath());
            komunikat1.setText("Odszyfrowany plik zapisano do: " + newFile.getName());
        } catch (NullPointerException | IOException e) {
            komunikat2.setText("Nie można odczytać pliku");
        }
    }
}