package edu.icet.thogakade.entity;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomerEntity {
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
