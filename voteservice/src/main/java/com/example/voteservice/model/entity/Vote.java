package com.example.voteservice.model.entity;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue
    private Long id;

    private int grade;

    private Long quote;

    private Long author;
    @Column(name = "date_of_creation")
    private LocalDate createdAt;

    public Vote() {
    }

    @Override
    public String toString() {
        return "Vote{" +
                ", grade=" + grade +
                ", user id =" + author +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(id, vote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
