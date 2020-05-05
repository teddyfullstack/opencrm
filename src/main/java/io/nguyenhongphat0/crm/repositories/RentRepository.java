package io.nguyenhongphat0.crm.repositories;

import io.nguyenhongphat0.crm.entities.Rent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RentRepository extends CrudRepository<Rent, Long> {
    Rent findById(long id);
    List<Rent> findRentsByEstateIdAndEndDateIsNull(long estateId);
}
