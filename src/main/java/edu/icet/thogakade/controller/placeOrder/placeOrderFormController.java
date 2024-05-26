package edu.icet.thogakade.controller.placeOrder;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class placeOrderFormController {

    public JFXButton btnPlaceOrder;
    @FXML
    private JFXButton btnCustomer;
    @FXML
    private JFXButton btnItemForm;
    @FXML
    private JFXButton btnOrderForm;

    public void customerOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnCustomer.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")))));
        stage.getIcons().add(new Image("img/Logo.png"));
        stage.setTitle("Thoga Kade");
        stage.show();
    }

    public void itemOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnItemForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ItemForm.fxml")))));
        stage.getIcons().add(new Image("img/Logo.png"));
        stage.setTitle("Thoga Kade");
        stage.show();
    }

    public void orderOnAction(ActionEvent actionEvent) {

    }
}
