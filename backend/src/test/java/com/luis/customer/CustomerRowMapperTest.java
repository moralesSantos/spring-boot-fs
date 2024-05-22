package com.luis.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Alex");
        when(resultSet.getString("email")).thenReturn("alex@gmail.com");
        when(resultSet.getInt("age")).thenReturn(25);


        //When
        Customer actual = customerRowMapper.mapRow(resultSet,1);

        //Then
        Customer expected = new Customer(
                1,"Alex","alex@gmail.com", 25
        );

        assertThat(actual).isEqualTo(expected);

    }
}