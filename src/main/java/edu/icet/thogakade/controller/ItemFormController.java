package edu.icet.thogakade.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class ItemFormController {

    @FXML
    private JFXButton btnCustomer;
    @FXML
    private JFXTextField txtCode;
    @FXML
    private Label lblItemCount;
    @FXML
    private JFXTextArea txtDescription;
    @FXML
    private JFXTextField txtSize;
    @FXML
    private JFXTextField txtPrice;
    @FXML
    private JFXTextField txtQty;
    @FXML
    private TableView table;
    @FXML
    private TableColumn colCode;
    @FXML
    private TableColumn colDescription;
    @FXML
    private TableColumn colSize;
    @FXML
    private TableColumn ColPrice;
    @FXML
    private TableColumn colQty;


    private void loadTables(){

    }

    public void searchOnAction(ActionEvent actionEvent) {

    }

    public void AddOnAction(ActionEvent actionEvent) {

    }


    public void UpdateOnAction(ActionEvent actionEvent) {

    }

    public void DeleteOnAction(ActionEvent actionEvent) {

    }

    public void cutomerOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnCustomer.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"))));
        stage.getIcons().add(new Image("img/Logo.png"));
        stage.setTitle("Thoga Kade");
        stage.show();
    }
}
