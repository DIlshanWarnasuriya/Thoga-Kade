package edu.icet.thogakade.controller.order;

import edu.icet.thogakade.crudUtil.CrudUtil;
import edu.icet.thogakade.model.OrderDetails;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class OrderDetailController {

    public static OrderDetailController instance;

    private OrderDetailController(){}

    public static OrderDetailController getInstance(){
        if (instance==null){
            instance = new OrderDetailController();
        }
        return instance;
    }

    public boolean saveOrderDetails(ObservableList<OrderDetails> list) throws SQLException, ClassNotFoundException {

        for (OrderDetails or : list){
            String sql = "insert into OrderDetail values(?,?,?,?)";
            boolean isAddOrderDetails = CrudUtil.execute(sql, or.getOrderId(), or.getItemCode(), or.getQty(), or.getDiscount());
            if (!isAddOrderDetails){
                return false;
            }
        }
        return true;
    }

}
