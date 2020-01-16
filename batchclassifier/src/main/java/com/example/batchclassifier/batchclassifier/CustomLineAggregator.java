package com.example.batchclassifier.batchclassifier;

public class CustomLineAggregator implements org.springframework.batch.item.file.transform.LineAggregator<Customer> {
    @Override
    public String aggregate(Customer customer) {
        return customer.toString();
    }
}
