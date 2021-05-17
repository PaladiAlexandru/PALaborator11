package com.main.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "friends",schema = "public")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Friends.findFriends",
                query = "SELECT f.person_id2 FROM Friends f WHERE f.person_id1= ?1"
        )

})
public class Friends implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long personId1;
    private long personId2;



    public Friends() {
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonId1() {
        return personId1;
    }

    public void setPersonId1(long personId1) {
        this.personId1 = personId1;
    }

    public long getPersonId2() {
        return personId2;
    }

    public void setPersonId2(long personId2) {
        this.personId2 = personId2;
    }
}
