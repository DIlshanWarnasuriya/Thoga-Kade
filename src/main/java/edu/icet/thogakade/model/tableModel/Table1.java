package edu.icet.thogakade.model.tableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Table1 {
    private String CustID;
    private String CustTitle;
    private String CustName;
    private Date DOB;
    private double salary;
}
