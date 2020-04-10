package io.nguyenhongphat0.crm.repositories;

import io.nguyenhongphat0.crm.entities.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findById(long id);
    List<Customer> findByNameContaining(String name);
}
