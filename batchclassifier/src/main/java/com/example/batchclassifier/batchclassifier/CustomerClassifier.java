package com.example.batchclassifier.batchclassifier;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

public class CustomerClassifier implements Classifier<Customer, ItemWriter<? super Customer>> {
    private static final long serialVersionUID = 1L;
    private ItemWriter<Customer> eventItemWriter;
    private ItemWriter<Customer> oddItemWriter;

    public CustomerClassifier(ItemWriter<Customer> eventItemWriter, ItemWriter<Customer> oddItemWriter) {
        this.eventItemWriter = eventItemWriter;
        this.oddItemWriter = oddItemWriter;
    }

    @Override
    public ItemWriter<? super Customer> classify(Customer customer) {
        return customer.getId() % 2 == 0 ? eventItemWriter : oddItemWriter;
    }
}
