package edu.icet.thogakade.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.thogakade.db.DBConnection;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {
    public JFXButton btnItemForm;
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

    private Customer customer;

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
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM Customer");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                Table1 table1 = new Table1(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4),
                        resultSet.getDouble(5)
                );
                Table2 table2 = new Table2(
                        resultSet.getString(1),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                );
                table1List.add(table1);
                table2List.add(table2);
                ++count;
            }
            table1.setItems(table1List);
            table2.setItems(table2List);
            lblCustomerCount.setText(count + " Customers Have");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchOnAction(ActionEvent actionEvent) {
        String cusId = txtCustomerId.getText();

        if (cusId.isEmpty()) {
            alertView("Customer Id is Empty. Please Enter Valid Customer Id");
        } else {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement stm = connection.prepareStatement("SELECT * FROM Customer WHERE CustID = ?");
                stm.setString(1, cusId);
                ResultSet res = stm.executeQuery();
                if (res.next()) {
                    alertView("Customer Found");
                    customer = new Customer(
                            cusId,
                            res.getString(2),
                            res.getString(3),
                            res.getDate(4),
                            res.getDouble(5),
                            res.getString(6),
                            res.getString(7),
                            res.getString(8),
                            res.getString(9)
                    );

                    HashMap<String, String> title = new HashMap<>();
                    title.put("category", customer.getCustTitle());

                    cmbTitle.setValue(null);
                    cmbTitle.setPromptText(customer.getCustTitle());
                    txtCustomerName.setText(customer.getCustName());
                    dpDate.setValue(LocalDate.parse(customer.getDOB().toString()));
                    txtSalary.setText("" + customer.getSalary());
                    txtAddress.setText(customer.getCustAddress());
                    txtCity.setText(customer.getCity());
                    txtProvince.setText(customer.getProvince());
                    txtPostalCode.setText(customer.getPostalCode());
                } else {
                    alertView("Customer Id is not found. Please Enter Valid Customer Id");
                }

            } catch (SQLException | ClassNotFoundException e) {
                alertView(e.getMessage());
            }
        }
    }

    public void AddOnAction(ActionEvent actionEvent) {

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

                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement stm = connection.prepareStatement("INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?,?)");
                stm.setString(1, customer.getCustID());
                stm.setString(2, customer.getCustTitle());
                stm.setString(3, customer.getCustName());
                stm.setObject(4, customer.getDOB());
                stm.setDouble(5, customer.getSalary());
                stm.setString(6, customer.getCustAddress());
                stm.setString(7, customer.getCity());
                stm.setString(8, customer.getProvince());
                stm.setString(9, customer.getPostalCode());

                int i = stm.executeUpdate();

                if (i > 0) {
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


    public void UpdateOnAction(ActionEvent actionEvent) {

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

            if (title1==null){
                title = title2;
            }
            else{
                title = title1.toString();
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date date = format.parse(dpDate.getValue().toString());

            if (id.isEmpty() || title.equals("null") || name.isEmpty() || date == null || salary < 0 || address.isEmpty() || city.isEmpty() || province.isEmpty() || postalCode.isEmpty()) {
                alertView("Please fill all Fields");
            } else {
                Customer customer = new Customer(id, title, name, date, salary, address, city, province, postalCode);
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement stm = connection.prepareStatement("update Customer set CustTitle=?, CustName=?, DOB=?, salary=?, CustAddress=?, City=?, Province=?, PostalCode=? where CustID=?");
                stm.setString(1, customer.getCustTitle());
                stm.setString(2, customer.getCustName());
                stm.setObject(3, customer.getDOB());
                stm.setDouble(4, customer.getSalary());
                stm.setString(5, customer.getCustAddress());
                stm.setString(6, customer.getCity());
                stm.setString(7, customer.getProvince());
                stm.setString(8, customer.getPostalCode());
                stm.setString(9, id);

                int i = stm.executeUpdate();
                if (i > 0) {
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

    public void DeleteOnAction(ActionEvent actionEvent) {

        if (txtCustomerId.getText().isEmpty()) {
            alertView("Please Enter valid cistomer id");
        } else {
            try {
                String responce = confirmAlert("Are you sure to delete this customer");

                if (responce.equals("OK")) {
                    Connection connection = DBConnection.getInstance().getConnection();
                    PreparedStatement stm = connection.prepareStatement("delete from Customer where CustID = ?");
                    stm.setObject(1, txtCustomerId.getText());
                    int i = stm.executeUpdate();

                    if (i > 0) {
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
                System.out.print(e.getMessage());
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

    private String confirmAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation alert");
        alert.showAndWait();
        return alert.getResult().getText();
    }

    public void itemOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnItemForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/ItemForm.fxml"))));
        stage.getIcons().add(new Image("img/Logo.png"));
        stage.setTitle("Thoga Kade");
        stage.show();
    }
}
