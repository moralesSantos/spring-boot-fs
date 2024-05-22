package com.luis.customer;

import com.luis.AbstractTestContainers;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;

//    @Autowired
//    private ApplicationContext applicationContext;


    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        //System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = FAKER.internet().emailAddress() +"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                25
        );
        underTest.save(customer);
        //When
        var actual = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerById() {
        //Given
        String email = FAKER.internet().emailAddress() +"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                25
        );
        underTest.save(customer);
        Integer id = underTest.findAll()
                .stream()
                .filter(c-> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        var actual = underTest.existsCustomerById(id);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailDoesNotExist() {
        //Given
        String email = FAKER.internet().emailAddress() +"-"+ UUID.randomUUID();
        //When
        var actual = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isFalse();
    }
    @Test
    void existsCustomerByIdWhenIdDoesNotExist() {
        //Given
        Integer id = -1;
        //When
        var actual = underTest.existsCustomerById(id);
        //Then
        assertThat(actual).isFalse();
    }
}