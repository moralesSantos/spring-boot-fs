package com.luis.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //When
        underTest.selectAllCustomers();
        //then
        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.selectCustomerById(id);
        //Then
        verify(customerRepository)
                .findById(id);
    }

    @Test
    void insertCustomer() {
        //given
        Customer customer = new Customer(
                1,"ali", "a@gmail.com", "password", 2,
                Gender.MALE);
        //when
        underTest.insertCustomer(customer);
        //then
        verify(customerRepository)
                .save(customer);
    }

    @Test
    void exitsPersonWithEmail() {
        //given
        String email = "test@test.com";
        //when
        underTest.exitsPersonWithEmail(email);
        //then
        verify(customerRepository)
                .existsCustomerByEmail(email);

    }

    @Test
    void deleteCustomerById() {
        //given
        int id =1;
        //when
        underTest.deleteCustomerById(id);
        //then
        verify(customerRepository)
                .deleteById(id);
    }

    @Test
    void exitsPersonWithId() {
        int id = 1;
        underTest.exitsPersonWithId(id);
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer();
        customer.setId(1);
        underTest.updateCustomer(customer);
        verify(customerRepository).save(customer);
    }
}