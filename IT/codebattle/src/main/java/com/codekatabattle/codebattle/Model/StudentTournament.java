package com.codekatabattle.codebattle.Model;


import java.io.Serializable;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

@Entity
@Table(name = "StudentTournament")
@IdClass(StudentTournament.StudentTournamentId.class)
public class StudentTournament {

    

    @Id
    @ManyToOne
    @JoinColumn(name = "tournament", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
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

        public Integer getTournament() {
            return tournament;
        }

        public void setTournament(Integer tournament) {
            this.tournament = tournament;
        }

        public String getStudent() {
            return student;
        }

        public void setStudent(String student) {
            this.student = student;
        }
        
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    @Override
    public String toString() {
        return "StudentTournament [tournament=" + tournament + ", student=" + student + "]";
    }
}

