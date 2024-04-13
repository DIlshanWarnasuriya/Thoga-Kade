package edu.icet.thogakade.model;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Customer {
    private String CustID;
    private String CustTitle;
    private String CustName;
    private Date DOB;
    private double salary;
    private String CustAddress;
    private String City;
    private String Province;
    private String PostalCode;
}
