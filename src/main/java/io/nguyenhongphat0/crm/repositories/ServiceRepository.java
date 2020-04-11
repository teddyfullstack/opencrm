package io.nguyenhongphat0.crm.repositories;

import io.nguyenhongphat0.crm.entities.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    Service findById(long id);
}
