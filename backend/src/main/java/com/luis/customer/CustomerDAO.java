package com.luis.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface CustomerDAO {

    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean exitsPersonWithEmail(String email);
    void deleteCustomerById(Integer id);
    boolean exitsPersonWithId(Integer id);
    void updateCustomer(Customer update);
    Optional<Customer> selectUserByEmail(String email);
}
