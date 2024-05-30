package edu.icet.thogakade.controller.item;

import edu.icet.thogakade.crudUtil.CrudUtil;
import edu.icet.thogakade.model.Item;
import edu.icet.thogakade.model.OrderDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemController implements ItemService {

    static ItemController instance;

    private ItemController() {
    }

    public static ItemController getInstance() {
        if (instance == null) {
            instance = new ItemController();
        }
        return instance;
    }

    @Override
    public ObservableList<Item> getAllItems() throws SQLException, ClassNotFoundException {

        ObservableList<Item> itemList = FXCollections.observableArrayList();
        String sql = "select * from Item";
        ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()) {

            Item item = new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5)
            );
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public Item searchItem(String code) throws SQLException, ClassNotFoundException {

        String sql = "select * from Item where ItemCode = ?";
        ResultSet resultSet = CrudUtil.execute(sql, code);

        if (resultSet.next()) {

            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5)
            );
        }
        return null;
    }

    @Override
    public Boolean saveItem(Item item) throws SQLException, ClassNotFoundException {
        String sql = "insert into item values(?,?,?,?,?)";

        return CrudUtil.execute(
                sql,
                item.getCode(),
                item.getDescription(),
                item.getSize(),
                item.getPrice(),
                item.getQty()
        );
    }

    @Override
    public Boolean updateItem(Item item) throws SQLException, ClassNotFoundException {
        String sql = "update item set Description=?, PackSize=?, UnitPrice=?, QtyOnHand=? where ItemCode=?";

        return CrudUtil.execute(
                sql,
                item.getDescription(),
                item.getSize(),
                item.getPrice(),
                item.getQty(),
                item.getCode()
        );
    }

    @Override
    public Boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        String sql = "delete from item where ItemCode=?";
        return CrudUtil.execute(sql, code);
    }

    public boolean updateStock(ObservableList<OrderDetails> list) throws SQLException, ClassNotFoundException {

        String sql = "update item set QtyOnHand=QtyOnHand-? where ItemCode=?";
        for (OrderDetails or : list) {

            boolean isUpdateItem = CrudUtil.execute(sql, or.getQty(), or.getItemCode());
            if (!isUpdateItem) {
                return false;
            }
        }
        return true;
    }
}
