package me.joshmckinney.scheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private int custId;
    private boolean custIsActive;
    private String custName;
    private String custAddress;
    private String custPhone;

    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static ObservableList<Customer> customerInactiveList = FXCollections.observableArrayList();
    private static ObservableList<Customer> lookupCustomer = FXCollections.observableArrayList();

    public Customer(int custId, boolean isActive, String custName, String custAddress, String custPhone) {
        this.custId = custId;
        this.custIsActive = isActive;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custPhone = custPhone;
    }

    public static void addCustomer(Customer newCustomer) {
        customerList.add(newCustomer);
    }
    public static void addInactiveCustomer(Customer newCustomer) { customerInactiveList.add(newCustomer); }

    public int getCustId() { return custId; }
    public boolean getCustIsActive() { return custIsActive; }
    public String getCustName() { return custName; }
    public String getCustAddress() { return custAddress; }
    public String getCustPhone() { return custPhone; }
    public static ObservableList<Customer> getAllCustomers() { return customerList; }
    public static ObservableList<Customer> getInactiveCustomers() { return customerInactiveList; }
    public static ObservableList<Customer> getLookupCustomer() { return lookupCustomer; }
    public static void deleteAllCustomers() {
        getAllCustomers().clear();
    }
    public static void deleteAllInactiveCustomers() {
        getInactiveCustomers().clear();
    }
    public static ObservableList<Customer> lookupCustomer(String custName) {
        if (!(getLookupCustomer().isEmpty())) {
            getLookupCustomer().clear();
        }
        for (Customer customer : getAllCustomers()) {
            if (customer.getCustName().toLowerCase().contains(custName.toLowerCase())) {
                getLookupCustomer().add(customer);
            }
        }
        return getLookupCustomer();
    }
}
