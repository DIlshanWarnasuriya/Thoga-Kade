package edu.icet.thogakade.controller.customer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.thogakade.model.Customer;
import edu.icet.thogakade.model.tableModel.Table1;
import edu.icet.thogakade.model.tableModel.Table2;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {
    @FXML
    private JFXButton btnItemForm;
    @FXML
    private JFXButton btnOrders;
    @FXML
    private JFXButton btnPlaceOrder;
    @FXML
    private Label lblCustomerCount;
    @FXML
    private JFXTextField txtCustomerId;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private DatePicker dpDate;
    @FXML
    private JFXTextField txtSalary;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtCity;
    @FXML
    private JFXTextField txtProvince;
    @FXML
    private JFXTextField txtPostalCode;
    @FXML
    private JFXComboBox cmbTitle;
    @FXML
    private TableView table1;
    @FXML
    private TableColumn colCustomerId1;
    @FXML
    private TableColumn colTitle;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colDOB;
    @FXML
    private TableColumn colSalary;
    @FXML
    private TableView table2;
    @FXML
    private TableColumn colCustomerId2;
    @FXML
    private TableColumn colAddress;
    @FXML
    private TableColumn colCity;
    @FXML
    private TableColumn colPostalCode;
    @FXML
    private TableColumn colProvince;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCustomerId1.setCellValueFactory(new PropertyValueFactory<>("CustID"));
        colCustomerId2.setCellValueFactory(new PropertyValueFactory<>("CustID"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("CustTitle"));
        colName.setCellValueFactory(new PropertyValueFactory<>("CustName"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("DOB"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("CustAddress"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("City"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("Province"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));

        setValueToComboBox();
        loadTables();
    }

    private void setValueToComboBox() {
        ObservableList<String> title = FXCollections.observableArrayList();
        title.add("Mrs.");
        title.add("Mr.");
        title.add("Ms.");
        title.add("Miss.");
        cmbTitle.setItems(title);
    }

    private void loadTables() {
        ObservableList<Table1> table1List = FXCollections.observableArrayList();
        ObservableList<Table2> table2List = FXCollections.observableArrayList();
        int count = 0;

        try {
            ObservableList<Customer> allCustomer = CustomerController.getInstance().getAllCustomer();

            for (Customer customer : allCustomer){
                table1List.add(new Table1(
                        customer.getCustID(),
                        customer.getCustTitle(),
                        customer.getCustName(),
                        customer.getDOB(),
                        customer.getSalary()
                ));

                table2List.add(new Table2(
                        customer.getCustID(),
                        customer.getCustAddress(),
                        customer.getCity(),
                        customer.getProvince(),
                        customer.getPostalCode()
                ));
                count++;
            }
            lblCustomerCount.setText(count + " Customers Have");
            table1.setItems(table1List);
            table2.setItems(table2List);


        } catch (SQLException | ClassNotFoundException e) {
            alertView(e.getMessage());
        }
    }

    public void searchOnAction(ActionEvent actionEvent) {
        String cusId = txtCustomerId.getText();

        if (cusId.isEmpty()) {
            alertView("Customer Id is Empty. Please Enter Valid Customer Id");
        } else {
            try {
                Customer customer = CustomerController.getInstance().searchCustomer(cusId);

                if (customer == null) {
                    new Alert(Alert.AlertType.WARNING, "Customer Id is not found. Please Enter Valid Customer Id", ButtonType.OK).show();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Found", ButtonType.OK).show();

                    cmbTitle.setValue(null);
                    cmbTitle.setPromptText(customer.getCustTitle());
                    txtCustomerName.setText(customer.getCustName());
                    dpDate.setValue(LocalDate.parse(customer.getDOB().toString()));
                    txtSalary.setText("" + customer.getSalary());
                    txtAddress.setText(customer.getCustAddress());
                    txtCity.setText(customer.getCity());
                    txtProvince.setText(customer.getProvince());
                    txtPostalCode.setText(customer.getPostalCode());
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
            }
        }
    }

    public void addOnAction(ActionEvent actionEvent) {

        try {
            String id = txtCustomerId.getText();
            String title = cmbTitle.getValue().toString();
            String name = txtCustomerName.getText();
            double salary = Double.parseDouble(txtSalary.getText());
            String address = txtAddress.getText();
            String city = txtCity.getText();
            String province = txtProvince.getText();
            String postalCode = txtPostalCode.getText();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dpDate.getValue().toString());

            if (id.isEmpty() || title.equals("null") || name.isEmpty() || date == null || salary < 0 || address.isEmpty() || city.isEmpty() || province.isEmpty() || postalCode.isEmpty()) {
                alertView("Please fill all Fields");
            } else {
                Customer customer = new Customer(id, title, name, date, salary, address, city, province, postalCode);

                boolean res = CustomerController.getInstance().addCustomer(customer);

                if (res) {
                    alertView("Customer Add Successful");
                    loadTables();
                    clearFields();
                } else {
                    alertView("Customer Add Fail");
                }
            }
        } catch (RuntimeException e) {
            alertView("Enter only Numbers for salary");
        } catch (SQLException | ClassNotFoundException | ParseException e) {
            alertView(e.getMessage());
        }
    }

    public void updateOnAction(ActionEvent actionEvent) {

        try {
            String id = txtCustomerId.getText();
            Object title1 = cmbTitle.getValue();
            String title2 = cmbTitle.getPromptText();
            String name = txtCustomerName.getText();
            double salary = Double.parseDouble(txtSalary.getText());
            String address = txtAddress.getText();
            String city = txtCity.getText();
            String province = txtProvince.getText();
            String postalCode = txtPostalCode.getText();
            String title = "";

            if (title1 == null) title = title2;
            else title = title1.toString();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date date = format.parse(dpDate.getValue().toString());

            if (id.isEmpty() || title.equals("null") || name.isEmpty() || date == null || salary < 0 || address.isEmpty() || city.isEmpty() || province.isEmpty() || postalCode.isEmpty()) {
                alertView("Please fill all Fields");
            } else {
                Customer customer = new Customer(id, title, name, date, salary, address, city, province, postalCode);

                boolean res= CustomerController.getInstance().updateCustomer(customer, id);

                if (res) {
                    alertView("Customer Update Successful");
                    loadTables();
                    clearFields();
                } else {
                    alertView("Customer Update Fail");
                }
            }
        } catch (ParseException | SQLException | ClassNotFoundException e) {
            alertView(e.getMessage());
        }
    }

    public void deleteOnAction(ActionEvent actionEvent) {

        if (txtCustomerId.getText().isEmpty()) {
            alertView("Please Enter valid customer id");
        } else {
            try {
                String confirmation = confirmAlert();

                if (confirmation.equals("OK")) {

                    boolean res = CustomerController.getInstance().deleteCustomer(txtCustomerId.getText());

                    if (res) {
                        loadTables();
                        clearFields();
                        alertView("Customer Delete Successful");
                    } else {
                        alertView("Customer Delete fail. Check Customer Id");
                    }
                } else {
                    clearFields();
                }
            } catch (SQLException | ClassNotFoundException e) {
                alertView(e.getMessage());
            }
        }
    }

    private void clearFields() {
        txtCustomerId.setText("");
        cmbTitle.setValue(null);
        cmbTitle.setPromptText("Title");
        txtCustomerName.setText("");
        txtSalary.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtProvince.setText("");
        txtPostalCode.setText("");
        dpDate.setValue(null);
    }

    private void alertView(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle("Alert");
        alert.showAndWait();
    }

    private String confirmAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure to delete this customer", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation alert");
        alert.showAndWait();
        return alert.getResult().getText();
    }

    public void itemOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnItemForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ItemForm.fxml")))));
        stage.getIcons().add(new Image("img/Logo.png"));
        stage.setTitle("Thoga Kade");
        stage.show();
    }
    public void placeOrderOnActon(ActionEvent actionEvent) throws IOException  {
        Stage stage = (Stage) btnPlaceOrder.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/PlaceOrder.fxml")))));
        stage.getIcons().add(new Image("img/Logo.png"));
        stage.setTitle("Thoga Kade");
        stage.show();
    }

    public void orderOnAction(ActionEvent actionEvent) {
    }


}
