package edu.icet.thogakade.controller.placeOrder;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.thogakade.controller.customer.CustomerController;
import edu.icet.thogakade.controller.item.ItemController;
import edu.icet.thogakade.model.Customer;
import edu.icet.thogakade.model.Item;
import edu.icet.thogakade.model.OrderCart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {


    @FXML
    private TableView table;
    @FXML
    private TableColumn colCode;
    @FXML
    private TableColumn colDescription;
    @FXML
    private TableColumn colQty;
    @FXML
    private TableColumn colPrice;
    @FXML
    private TableColumn colTotal;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblOrderId;
    @FXML
    private JFXComboBox cmbCustomerId;
    @FXML
    private Label lblName;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblSalary;
    @FXML
    private Label lblProvince;
    @FXML
    private JFXComboBox cmbItemCode;
    @FXML
    private JFXButton btnPlaceOrder;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblPackSie;
    @FXML
    private Label lblUnitPrice;
    @FXML
    private Label lblAvailableQty;
    @FXML
    private JFXTextField txtQty;
    @FXML
    private JFXComboBox cmbItemCodeCart;
    @FXML
    private Label lblTotal;
    @FXML
    private JFXButton btnCustomer;
    @FXML
    private JFXButton btnItemForm;
    @FXML
    private JFXButton btnOrderForm;

    private ObservableList<OrderCart> orderCart = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        setAllCustomerId();
        setAllItemId();

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            try {
                Customer customer = CustomerController.getInstance().searchCustomer(t1.toString());
                lblName.setText(customer.getCustTitle() + " " + customer.getCustName());
                lblAddress.setText(customer.getCustAddress());
                lblSalary.setText(Double.toString(customer.getSalary()));
                lblProvince.setText(customer.getProvince());

            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
            catch (NullPointerException ex){}
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            try {
                Item item = ItemController.getInstance().searchItem(t1.toString());
                lblDescription.setText(item.getDescription());
                lblPackSie.setText(item.getSize());
                lblUnitPrice.setText(Double.toString(item.getPrice()));

                if (orderCart==null){
                    lblAvailableQty.setText(Integer.toString(item.getQty()));
                }
                else{
                    int i = findIndex(cmbItemCode.getValue().toString());
                    if (i==-1){
                        lblAvailableQty.setText(Integer.toString(item.getQty()));
                    }
                    else {
                        OrderCart orderCart1 = orderCart.get(i);
                        int cartQty = orderCart1.getQty();
                        int qty = item.getQty() - cartQty;
                        lblAvailableQty.setText(Integer.toString(qty));
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }catch (NullPointerException ex){}
        });


    }

    private int findIndex(String code){
        int i = 0;
        for (OrderCart or : orderCart){
            if (or.getCode().equals(code)){
                return i;
            }
            i++;
        }
        return -1;
    }

    private void setAllCustomerId(){
        try {
            ObservableList<String> allIds = FXCollections.observableArrayList();
            ObservableList<Customer> allCustomer = CustomerController.getInstance().getAllCustomer();

            allCustomer.forEach(customer -> allIds.add(customer.getCustID()));
            cmbCustomerId.setItems(allIds);

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setAllItemId(){
        try {
            ObservableList<String> itemIds = FXCollections.observableArrayList();
            ObservableList<Item> items = ItemController.getInstance().getAllItems();

            items.forEach(customer -> itemIds.add(customer.getCode()));
            cmbItemCode.setItems(itemIds);

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void addToCartOnAction(ActionEvent actionEvent) {

        try {
            int qty = Integer.parseInt(txtQty.getText());
            if (cmbCustomerId.getValue() == null || cmbItemCode.getValue() == null || qty <= 0) {
                new Alert(Alert.AlertType.WARNING, "please select customer, Item and enter valid Qty").show();
            } else {
                int availableQty = Integer.parseInt(lblAvailableQty.getText());
                if (qty > availableQty) {
                    new Alert(Alert.AlertType.WARNING, "You can only buy " + availableQty + " Quantity only").show();
                } else {
                    String code = cmbItemCode.getValue().toString();
                    String description = lblDescription.getText();
                    double unitPrice = Double.parseDouble(lblUnitPrice.getText());
                    double total = unitPrice * qty;

                    orderCart.add(new OrderCart(code, description, qty, unitPrice, total));
                    availableQty = availableQty - qty;
                    lblAvailableQty.setText(Integer.toString(availableQty));
                    loadTableAndNetTotal();
                    setItemCodeInCart();
                    clearAll();
                }
            }
        }catch (RuntimeException e){
            new Alert(Alert.AlertType.WARNING, "please select customer, Item and enter valid Qty").show();
        }
    }

    private void loadTableAndNetTotal(){
        if (orderCart != null){
            table.setItems(orderCart);

            double netTotal = 0;
            for (OrderCart or : orderCart){
                netTotal += or.getTotal();
            }
            lblTotal.setText(netTotal+"/=");
        }
    }

    private void setItemCodeInCart(){
        ObservableList<String> cartOrderList = FXCollections.observableArrayList();

        for (OrderCart or : orderCart){
            cartOrderList.add(or.getCode());
        }

        cmbItemCodeCart.setItems(cartOrderList);
    }


    public void deleteFromCartOnAction(ActionEvent actionEvent) {
        if (cmbItemCodeCart.getValue()==null){
            new Alert(Alert.AlertType.WARNING, "please select Order Id").show();
        }
        else {
            int code = 0;
            int reduceQty = 0;
            for (OrderCart or : orderCart){
                if (or.getCode().equals(cmbItemCodeCart.getValue().toString())){
                    reduceQty = or.getQty();
                    break;
                }
                code++;
            }

            orderCart.remove(code);
            new Alert(Alert.AlertType.INFORMATION, "Delete Success").show();
            loadTableAndNetTotal();
            lblAvailableQty.setText(Integer.toString(Integer.parseInt(lblAvailableQty.getText()) + reduceQty));

            cmbItemCodeCart.setValue(null);
            cmbItemCodeCart.setPromptText("Select Item code");
            setItemCodeInCart();
        }
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clearAll();
    }
    private void clearAll(){
        cmbItemCode.setValue(null);

        cmbItemCodeCart.setValue(null);

        cmbCustomerId.setValue(null);
        txtQty.setText("");
        lblName.setText("");
        lblAddress.setText("");
        lblSalary.setText("");
        lblProvince.setText("");
        lblDescription.setText("");
        lblPackSie.setText("");
        lblUnitPrice.setText("");
        lblAvailableQty.setText("");



    }

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
