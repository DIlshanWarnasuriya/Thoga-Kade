package edu.icet.thogakade.controller.order;

import edu.icet.thogakade.controller.item.ItemController;
import edu.icet.thogakade.util.CrudUtil;
import edu.icet.thogakade.db.DBConnection;
import edu.icet.thogakade.dto.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderController implements OrderService {

    public static OrderController instance;

    private OrderController() {
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

    public boolean saveOrder(Order order) throws SQLException, ClassNotFoundException {
        Connection connection = null;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "insert into orders values(?,?,?)";
            boolean isAddOrder = CrudUtil.execute(sql, order.getOrderId(), order.getDate(), order.getCustomerId());

            if (isAddOrder) {
                boolean isAddOrderDetails = OrderDetailController.getInstance().saveOrderDetails(order.getList());

                if (isAddOrderDetails) {
                    boolean isUpdateItems = ItemController.getInstance().updateStock(order.getList());

                    if (isUpdateItems) {
                        connection.setAutoCommit(true);
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;

        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }
}
