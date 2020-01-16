package com.example.batchclassifier.batchclassifier;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements org.springframework.jdbc.core.RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
        return Customer
                .builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("firstName"))
                .lastName(resultSet.getString("lastName"))
                .birthdate(resultSet.getString("birthdate"))
                .build();
    }
}
