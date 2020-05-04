package io.nguyenhongphat0.crm.repositories;

import io.nguyenhongphat0.crm.entities.Estate;
import org.springframework.data.repository.CrudRepository;

public interface EstateRepository extends CrudRepository<Estate, Long> {
    Estate findById(long id);
}
