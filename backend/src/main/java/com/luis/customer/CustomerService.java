package com.luis.customer;

import com.luis.exception.DuplicateResourceExecution;
import com.luis.exception.RequestValidationException;
import com.luis.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDAO.selectCustomerById(id)
                .orElseThrow(
                ()-> new ResourceNotFoundException("customer not found with id " + id)
        );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exists
        String email = customerRegistrationRequest.email();
        if (customerDAO.exitsPersonWithEmail(email)) {
            throw new DuplicateResourceExecution(
                    "customer email already exists"
            );
        }
        Customer customer = (
                new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age(),
                        customerRegistrationRequest.gender()
                )
        );

        customerDAO.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer id){
        //check if Id is valid
        if(!customerDAO.exitsPersonWithId(id)){
            throw new ResourceNotFoundException (
                    "customer with id [%s] not found".formatted(id)
            );
        }
        customerDAO.deleteCustomerById(id);
    }


    public void updateCustomer(Integer customerId,
                               CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(customerId);

        boolean changes = false;

        if(updateRequest.name() != null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
            changes = true;
        }
        if(updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())){
            customer.setAge(updateRequest.age());
            changes = true;
        }
        if(updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())){
            if (customerDAO.exitsPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResourceExecution(
                        "customer email already exists"
                );
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no data changes found");
        }
        customerDAO.updateCustomer(customer);

    }
}
