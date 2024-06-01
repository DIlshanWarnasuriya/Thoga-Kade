package edu.icet.thogakade.dto.tableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Table2 {
    private String CustID;
    private String CustAddress;
    private String City;
    private String Province;
    private String PostalCode;
}
