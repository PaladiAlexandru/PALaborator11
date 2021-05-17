package com.main.controllers;

import com.main.exceptions.MyException;
import com.main.models.Friends;
import com.main.models.Person;
import com.main.repositories.FriendsRepository;
import com.main.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @PostMapping("/{userId1}/add/{userId2}")
    public ResponseEntity<Object> addFriend(@PathVariable(value = "userId1") long userId1, @PathVariable(value = "userId2") long userId2) {
        Optional<Person> person1 = personRepository.findById(userId1);
        Optional<Person> person2 = personRepository.findById(userId2);

        if (person1.isEmpty() || person2.isEmpty()) {
            System.out.println("ACI");
            throw new MyException("Person not found");
        } else {
            Friends friends = new Friends();
            friends.setPersonId1(person1.get().getId());
            friends.setPersonId2(person2.get().getId());
            friendsRepository.save(friends);
            return ResponseEntity.status(HttpStatus.OK).body("Friend added!");

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getFriends(@PathVariable(value = "id") long id) {
        List<Long> friends = friendsRepository.findFriends(id);

        if (friends.isEmpty()) {
            throw new MyException("Person has no friends");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(friends);

        }
    }

}
