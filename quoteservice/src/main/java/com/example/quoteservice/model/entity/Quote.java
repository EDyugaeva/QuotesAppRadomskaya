package com.example.quoteservice.model.entity;

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
@Table(name = "quote")
@NoArgsConstructor
public class Quote {

    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private Timestamp dateOfCreation;
    private Timestamp dateOfUpdate;
    private Long userAccountId;
    @ElementCollection
    private Set<Long> votes;

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", dateOfCreation=" + dateOfCreation + '\'' +
                ", dateOfUpdate=" + dateOfUpdate + '\'' +
                ", author=" + userAccountId + '\'' +
                ", votes=" + votes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return Objects.equals(id, quote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
