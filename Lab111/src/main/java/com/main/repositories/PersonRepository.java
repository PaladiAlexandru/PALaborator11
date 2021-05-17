package com.main.repositories;

import com.main.models.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {
    List<Long> mostConnected(Long k);
    List<Long> leastConnected(Long k);
}
