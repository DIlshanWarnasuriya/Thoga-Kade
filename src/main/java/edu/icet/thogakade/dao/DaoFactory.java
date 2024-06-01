package edu.icet.thogakade.dao;

import edu.icet.thogakade.dao.custom.impl.CustomerDaoImpl;
import edu.icet.thogakade.util.DaoType;

public class DaoFactory {
    private static DaoFactory instance;

    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return instance==null ? instance= new DaoFactory() : instance;
    }

    public <T> T getDao(DaoType type){
        switch (type){
            case CUSTOMER: return (T) new CustomerDaoImpl();
        }
        return null;
    }
}
