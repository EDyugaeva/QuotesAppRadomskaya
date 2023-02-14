package com.example.userservice.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "user_account")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private String name;

    private String email;
    @JsonIgnore
    private String password;
    private Timestamp dateOfCreation;
    @ElementCollection
    private Set<Long> quotes;

    @ElementCollection
    private Set<Long> votes;


    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", quotes=" + quotes + '\'' +
                ", votes = " + votes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
