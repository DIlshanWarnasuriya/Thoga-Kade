package edu.icet.thogakade.controller.order;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.thogakade.bo.BoFactory;
import edu.icet.thogakade.bo.custom.CustomerBo;
import edu.icet.thogakade.bo.custom.ItemBo;
import edu.icet.thogakade.dto.*;
import edu.icet.thogakade.util.BoType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {


    @FXML
    private JFXTextField txtDiscount;
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

    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        setDateAndTime();    // set date and time
        setAllCustomerId();  // set all customer id to customer id combo box
        setAllItemId();      // set all item id to item id combo box
        generateId();

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            try {
                Customer customer = customerBo.searchCustomer(t1.toString());
                lblName.setText(customer.getCustTitle() + " " + customer.getCustName());
                lblAddress.setText(customer.getCustAddress());
                lblSalary.setText(Double.toString(customer.getSalary()));
                lblProvince.setText(customer.getProvince());

            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (NullPointerException ex) {
            }
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            try {
                Item item = itemBo.searchItem(t1.toString());
                lblDescription.setText(item.getDescription());
                lblPackSie.setText(item.getSize());
                lblUnitPrice.setText(Double.toString(item.getPrice()));

                if (orderCart.isEmpty()) {
                    lblAvailableQty.setText(Integer.toString(item.getQty()));
                } else {
                    int i = findIndex(cmbItemCode.getValue().toString());
                    if (i == -1) {
                        lblAvailableQty.setText(Integer.toString(item.getQty()));
                    } else {
                        OrderCart orderCart1 = orderCart.get(i);
                        int cartQty = orderCart1.getQty();
                        int qty = item.getQty() - cartQty;
                        lblAvailableQty.setText(Integer.toString(qty));
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (NullPointerException ex) {
            }
        });
    }

    private void setAllCustomerId() {
        try {
            ObservableList<String> allIds = FXCollections.observableArrayList();
            ObservableList<Customer> allCustomer = customerBo.getAllCustomer();

            allCustomer.forEach(customer -> allIds.add(customer.getCustID()));
            cmbCustomerId.setItems(allIds);

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setAllItemId() {
        try {
            ObservableList<String> itemIds = FXCollections.observableArrayList();
            ObservableList<Item> items = itemBo.getAllItem();

            items.forEach(customer -> itemIds.add(customer.getCode()));
            cmbItemCode.setItems(itemIds);

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    // Add to cart function
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
                    txtQty.setText("");
                    loadTableAndNetTotal();
                    setItemCodeInCart();
                }
            }
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.WARNING, "please select customer, Item and enter valid Qty").show();
        }
    }

    private void loadTableAndNetTotal() {
        if (!orderCart.isEmpty()) {
            table.setItems(orderCart);

            double netTotal = 0;
            for (OrderCart or : orderCart) {
                netTotal += or.getTotal();
            }
            lblTotal.setText(netTotal + "/=");
        }
    }

    private void setItemCodeInCart() {
        ObservableList<String> cartOrderList = FXCollections.observableArrayList();

        cmbItemCodeCart.setValue(null);

        for (OrderCart or : orderCart) {
            cartOrderList.add(or.getCode());
        }
        cmbItemCodeCart.setItems(cartOrderList);
    }

    // Delete From cart function
    public void deleteFromCartOnAction(ActionEvent actionEvent) {
        if (cmbItemCodeCart.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "please select Order Id").show();
        } else {
            int index = findIndex(cmbItemCodeCart.getValue().toString());
            int reduceQty = orderCart.get(index).getQty();
            orderCart.remove(index);

            new Alert(Alert.AlertType.INFORMATION, "Delete from cart Success").show();
            if (cmbItemCode.getValue() != null) {
                lblAvailableQty.setText(Integer.toString(Integer.parseInt(lblAvailableQty.getText()) + reduceQty));
            }
            setItemCodeInCart();
        }
    }

    // Place Order Function
    public void placeOrderOnAction(ActionEvent actionEvent) {

        if (orderCart.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No items in cart. please add items to cart").show();
        } else if (cmbCustomerId.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select customer id").show();
        } else {
            try {
                String orderId = lblOrderId.getText();
                String date = lblDate.getText();
                String customerId = cmbCustomerId.getValue().toString();
                ObservableList<OrderDetails> list = FXCollections.observableArrayList();
                int discount = Integer.parseInt(txtDiscount.getText());
                for (OrderCart or : orderCart){
                    list.add(new OrderDetails(orderId, or.getCode(), or.getQty(), discount));
                }

                Order order = new Order(orderId, date, customerId, list);

                boolean res = OrderController.getInstance().saveOrder(order);

                if (res) {
                    new Alert(Alert.AlertType.INFORMATION, "Order place successful").show();
                    generateId();
                    orderCart.remove(0, orderCart.toArray().length);
                    setItemCodeInCart();
                    txtDiscount.setText("0");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Order place fail").show();
                }
            } catch (RuntimeException e) {
                new Alert(Alert.AlertType.ERROR, "Enter valid discount").show();
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private void generateId() {
        try {
            int count = OrderController.getInstance().generateNewOrderId();
            lblOrderId.setText(String.format("D%03d", (count + 1)));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearOnAction(ActionEvent actionEvent) {
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

    private int findIndex(String code) {
        int i = 0;
        for (OrderCart or : orderCart) {
            if (or.getCode().equals(code)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void setDateAndTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(format.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            LocalTime time = LocalTime.now();
            lblTime.setText(time.getHour() + " : " + time.getMinute() + " : " + time.getSecond());
        }), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
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

    public void ExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCustomer.getScene().getWindow();
        stage.close();
    }
}
