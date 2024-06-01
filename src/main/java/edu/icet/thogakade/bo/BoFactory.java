package edu.icet.thogakade.bo;

import edu.icet.thogakade.bo.custom.impl.CustomerBoImpl;
import edu.icet.thogakade.bo.custom.impl.ItemBoImpl;
import edu.icet.thogakade.util.BoType;

public class BoFactory {
    private static BoFactory instance;

    private BoFactory(){}

    public static BoFactory getInstance(){
        return instance==null ? instance=new BoFactory() : instance;
    }

    public <T extends SuperBo> T getBo(BoType type){
        switch (type){
            case CUSTOMER: return (T) new CustomerBoImpl();
            case ITEM: return (T) new ItemBoImpl();
        }
        return null;
    }

}
