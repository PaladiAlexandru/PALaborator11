package com.main.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "public")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Person.mostConnected",
                query = "SELECT person_id1, count(*) as nrFriends from friends GROUP BY person_id1 ORDER BY nrFriends DESC LIMIT ?1"
        ),
        @NamedNativeQuery(
                name = "Person.leastConnected",
                query = "SELECT person_id1, count(*) as nrFriends from friends GROUP BY person_id1 ORDER BY nrFriends ASC LIMIT ?1"
        )
})
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;


    public Person() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
