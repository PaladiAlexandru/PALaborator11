package com.main.repositories;

import com.main.models.Friends;
import com.main.models.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendsRepository extends CrudRepository<Friends, Long> {
    List<Long> findFriends(Long id);
}
