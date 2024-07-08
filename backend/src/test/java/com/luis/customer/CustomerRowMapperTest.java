package com.luis.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
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
        when(resultSet.getString("gender")).thenReturn("FEMALE");


        //When
        Customer actual = customerRowMapper.mapRow(resultSet,1);

        //Then
        Customer expected = new Customer(
                1,"Alex","alex@gmail.com", "password", 25,
                Gender.FEMALE);

        assertThat(actual).isEqualTo(expected);

    }
}