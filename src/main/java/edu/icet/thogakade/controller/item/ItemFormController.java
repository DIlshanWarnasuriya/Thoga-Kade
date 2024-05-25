package edu.icet.thogakade.controller.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.icet.thogakade.db.DBConnection;
import edu.icet.thogakade.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

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
    private TableColumn colPrice;
    @FXML
    private TableColumn colQty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadTables();
    }

    private void loadTables(){
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        int count = 0;
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("select * from Item");
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()){

                Item item = new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                );
                count++;
                itemList.add(item);
            }
            table.setItems(itemList);
            lblItemCount.setText(count + " Items Have");

        } catch (SQLException | ClassNotFoundException e) {
            alertView(e.getMessage());
        }
    }

    public void searchOnAction(ActionEvent actionEvent) {
        String code = txtCode.getText();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("select * from Item where ItemCode = ?");
            stm.setObject(1, code);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()){
                alertView("Customer Found");
                Item item = new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                );

                txtDescription.setText(item.getDescription());
                txtSize.setText(item.getSize());
                txtPrice.setText(""+item.getPrice());
                txtQty.setText("Qty - "+item.getQty());
            }
            else{
                alertView("Customer Not Found");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

    private void alertView(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle("Warning alert");
        alert.showAndWait();
    }

    private String confirmAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Warning alert");
        alert.showAndWait();
        return alert.getResult().getText();
    }
}
