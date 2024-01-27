package com.codekatabattle.codebattle.Model;


import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "StudentTournament")
@IdClass(StudentTournament.StudentTournamentId.class)
public class StudentTournament {

    @Id
    @ManyToOne
    @JoinColumn(name = "tournament", referencedColumnName = "id")
    private Tournament tournament;

    @Id
    @ManyToOne
    @JoinColumn(name = "student", referencedColumnName = "email")
    private Student student;

    // Costruttori, getter e setter

    public static class StudentTournamentId implements Serializable {
        private Integer tournament;
        private String student;

        // Costruttori, getter e setter, equals, hashCode

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StudentTournamentId that = (StudentTournamentId) o;
            return Objects.equals(tournament, that.tournament) &&
                    Objects.equals(student, that.student);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tournament, student);
        }
    }
}

