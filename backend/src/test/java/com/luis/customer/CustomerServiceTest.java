package com.luis.customer;

import com.luis.exception.DuplicateResourceExecution;
import com.luis.exception.RequestValidationException;
import com.luis.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void getCustomer() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,"alex", "alex@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        Customer actual = underTest.getCustomer(id);
        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void WillThrowWhenGetCustomerReturnEmptyOptional(){
        //Given
        int id = 10;
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(()-> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer not found with id " + id
                );
    }

    @Test
    void addCustomer() {
        //Given
        String email = "alex@gmail.com";
        when(customerDAO.exitsPersonWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",email, 19
                );
        //When
        underTest.addCustomer(request);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }
    @Test
    void willThrowWhenEmailExistWhileAddingACustomer() {
        //Given
        String email = "alex@gmail.com";
        when(customerDAO.exitsPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",email, 19
        );
        //When
        assertThatThrownBy(()-> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceExecution.class)
                        .hasMessage("customer email already exists");
        //Then
        verify(customerDAO,never()).insertCustomer(any());

    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 10;
        when(customerDAO.exitsPersonWithId(id)).thenReturn(true);
        //when
        underTest.deleteCustomerById(id);
        //Then
        verify(customerDAO).deleteCustomerById(id);
    }

    @Test
    void WillThrowDeleteCustomerByIdNotExists() {
        //Given
        int id = 10;
        when(customerDAO.exitsPersonWithId(id)).thenReturn(false);
        //when
        assertThatThrownBy(()-> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
        //Then
        verify(customerDAO,never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                "Alex","alex@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest
                        ("Alexandro", "alexander@gmail.com", 23);

        when(customerDAO.exitsPersonWithEmail(updateRequest.email())).thenReturn(false);
        //When
        underTest.updateCustomer(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void canUpdateCustomerName() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                "Alex","alex@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest
                        ("Alexander", null, null);
        //When
        underTest.updateCustomer(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateCustomerEmail() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                "Alex","alex@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest
                        (null, "alexander@gmail.com", null);
        when(customerDAO.exitsPersonWithEmail(updateRequest.email())).thenReturn(false);

        //When
        underTest.updateCustomer(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 22);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void willThrowExceptionWhenEmailExistWhileUpdatingCustomer() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                "Alex","alex@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest
                        (null, "alexander@gmail.com", null);
        when(customerDAO.exitsPersonWithEmail(updateRequest.email())).thenReturn(true);

        assertThatThrownBy(()-> underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(DuplicateResourceExecution.class)
                        .hasMessage("customer email already exists");

        //When
        verify(customerDAO, never()).updateCustomer(any());


    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                "Alex","alex@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(
                        customer.getName(), customer.getEmail(),customer.getId()
                );

        //When
        assertThatThrownBy(()-> underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //Then
        verify(customerDAO, never()).updateCustomer(any());
    }



}