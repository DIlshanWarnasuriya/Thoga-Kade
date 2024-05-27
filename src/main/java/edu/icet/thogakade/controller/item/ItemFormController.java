package edu.icet.thogakade.controller.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.icet.thogakade.model.Item;
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
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    @FXML
    private JFXButton btnOrderForm;
    @FXML
    private JFXButton btnItemForm;
    @FXML
    private JFXButton btnPlaceOrder;
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

    private void loadTables() {
        try {
            ObservableList<Item> allItems = ItemController.getInstance().getAllItems();
            table.setItems(allItems);
            lblItemCount.setText(allItems.toArray().length + " Items Have");

        } catch (SQLException | ClassNotFoundException e) {
            alertView(e.getMessage());
        }
    }

    public void searchOnAction(ActionEvent actionEvent) {
        String code = txtCode.getText();
        try {
            Item item = ItemController.getInstance().searchItem(code);

            if (item != null) {
                alertView("Item Found");
                txtDescription.setText(item.getDescription());
                txtSize.setText(item.getSize());
                txtPrice.setText("" + item.getPrice());
                txtQty.setText("" + item.getQty());
            } else {
                alertView("Item Not Found");
                clearAll();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void addOnAction(ActionEvent actionEvent) {
        try {
            int qty = Integer.parseInt(txtQty.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (txtCode.getText().isEmpty() || txtDescription.getText().isEmpty() || price <= 0 || qty <= 0 || txtSize.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Fill all textBoxes").show();
            } else {
                Item item = new Item(txtCode.getText(), txtDescription.getText(), txtSize.getText(), price, qty);
                boolean res = ItemController.getInstance().saveItem(item);

                if (res) {
                    alertView("Item added Successful");
                    loadTables();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Item added Fail").show();
                }
                clearAll();
            }
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.WARNING, "please Enter valid input").show();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void updateOnAction(ActionEvent actionEvent) {
        try {
            int qty = Integer.parseInt(txtQty.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (txtCode.getText().isEmpty() || txtDescription.getText().isEmpty() || price <= 0 || qty <= 0 || txtSize.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Fill all textBoxes").show();
            } else {
                Item item = new Item(txtCode.getText(), txtDescription.getText(), txtSize.getText(), price, qty);
                boolean res = ItemController.getInstance().updateItem(item);

                if (res) {
                    alertView("Item Update Successful");
                    loadTables();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Item Update Fail").show();
                }
                clearAll();
            }
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.WARNING, "please Enter valid input").show();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        try {
            String confirmation = confirmAlert();
            if (confirmation.equals("OK")) {
                boolean res = ItemController.getInstance().deleteItem(txtCode.getText());
                if (res) {
                    alertView("Item Delete Successful");
                    loadTables();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Item delete Fail").show();
                }
                clearAll();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearAll() {
        txtCode.setText("");
        txtDescription.setText("");
        txtSize.setText("");
        txtQty.setText("");
        txtPrice.setText("");
    }

    private void alertView(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle("Warning alert");
        alert.showAndWait();
    }

    private String confirmAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure to delete this item", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Warning alert");
        alert.showAndWait();
        return alert.getResult().getText();
    }


    public void customerOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnCustomer.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")))));
        stage.getIcons().add(new Image("img/Logo.png"));
        stage.setTitle("Thoga Kade");
        stage.show();
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnPlaceOrder.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/PlaceOrder.fxml")))));
        stage.getIcons().add(new Image("img/Logo.png"));
        stage.setTitle("Thoga Kade");
        stage.show();
    }

    public void OrdersOnAction(ActionEvent actionEvent) {
    }

    public void ExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnPlaceOrder.getScene().getWindow();
        stage.close();
    }
}
