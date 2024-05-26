package edu.icet.thogakade.crudUtil;

import edu.icet.thogakade.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {
    public static <T> T execute(String sql, Object... args) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement(sql);
        for (int i=0; i<args.length; i++){
            stm.setObject(i+1, args[i]);
        }

        if (sql.startsWith("SELECT") || sql.startsWith("select")){
            return (T) stm.executeQuery();
        }
        return (T) (Boolean) (stm.executeUpdate()>0);
    }
}
