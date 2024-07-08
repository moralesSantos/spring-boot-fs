package com.luis.customer;

import com.luis.exception.DuplicateResourceExecution;
import com.luis.exception.RequestValidationException;
import com.luis.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;



    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO,
                           CustomerDTOMapper customerDTOMapper,
                           PasswordEncoder passwordEncoder) {
        this.customerDAO = customerDAO;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Integer id){
        return customerDAO.selectCustomerById(id)
                .map(customerDTOMapper)
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
                        passwordEncoder.encode(customerRegistrationRequest.password()),
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

        Customer customer = customerDAO.selectCustomerById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("customer not found with id " + customerId)
                );


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
