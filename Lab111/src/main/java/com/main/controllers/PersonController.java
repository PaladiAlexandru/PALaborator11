package com.main.controllers;

import com.main.exceptions.MyException;
import com.main.models.Person;
import com.main.models.Friends;
import com.main.repositories.FriendsRepository;
import com.main.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;



    @GetMapping
    public List<Person> findAllPersons() {

        return (List<Person>) personRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findPersonById(@PathVariable(value = "id") long id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Person not found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(person);

        }
    }

    @PostMapping
    public ResponseEntity<Person> savePerson(@Validated @RequestBody Person person) {
        personRepository.save(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<Person> modifyPerson(@Validated @RequestBody Person person) {
        Optional<Person> personInDB = personRepository.findById(person.getId());
        try {
            personInDB.get().setName(person.getName());
        } catch (NoSuchElementException e) {
            throw new MyException("Person doesn't exist!");
        }

        personRepository.save(personInDB.get());
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable(value = "id") Long id) {

        Optional<Person> personInDB = personRepository.findById(id);

        if (personInDB.isPresent()) {
            personRepository.delete(personInDB.get());
            return new ResponseEntity<>(personInDB.get(), HttpStatus.OK);
        }
        throw new MyException("Person doesn't exist!");

    }

    @GetMapping(value = "/most/{k}")
    public ResponseEntity<Object> mostPopular(@PathVariable(value = "k") long id) {
        List<Long> mostPopularIds = personRepository.mostConnected(id);

        if (mostPopularIds.isEmpty()) {
            throw new MyException("There is no popular kid!");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(mostPopularIds);

        }
    }
    @GetMapping(value = "/least/{k}")
    public ResponseEntity<Object> mostLeastPopular(@PathVariable(value = "k") long id) {
        List<Long> leastPopularIds = personRepository.leastConnected(id);

        if (leastPopularIds.isEmpty()) {
            throw new MyException("There is no least popular kid!");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(leastPopularIds);

        }
    }
}
