package edu.icet.thogakade.controller.placeOrder;

import edu.icet.thogakade.controller.item.ItemController;
import edu.icet.thogakade.crudUtil.CrudUtil;
import edu.icet.thogakade.model.Item;
import edu.icet.thogakade.model.OrderCart;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceOrderController implements PlaceOrderService {

    public static PlaceOrderController instance;

    private PlaceOrderController() {
    }

    public int generateNewOrderId() throws SQLException, ClassNotFoundException {
        String sql = "select * from orders";
        int count = 0;
        ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()) {
            count++;
        }
        return count;
    }

    public boolean saveOrder(String orderId, String customerId, ObservableList<OrderCart> list, int discount) throws SQLException, ClassNotFoundException {

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        String sql = "insert into orders values(?,?,?)";
        boolean step1 = CrudUtil.execute(sql, orderId, date, customerId);

        if (step1) {
            for (OrderCart or : list) {
                sql = "INSERT INTO OrderDetail VALUES(?,?,?,?)";
                boolean step2 = CrudUtil.execute(sql, orderId, or.getCode(), or.getQty(), discount);
                if (step2) {

                    Item item = ItemController.getInstance().searchItem(or.getCode());
                    int newQrt = item.getQty() - or.getQty();

                    sql = "update item set QtyOnHand=? where ItemCode=?";
                    boolean step3 = CrudUtil.execute(sql, newQrt, or.getCode());
                    if (!step3) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public static PlaceOrderController getInstance() {
        if (instance == null) {
            instance = new PlaceOrderController();
        }
        return instance;
    }
}
