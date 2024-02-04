package com.codekatabattle.codebattle.Model;

import java.io.Serializable;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

@Entity
@Table(name = "TournamentRanking")
@IdClass(TournamentRanking.TournamentRankingId.class)
public class TournamentRanking {
        @Id
    @ManyToOne
    @JoinColumn(name = "tournamentid", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tournament tournament;

    @Id
    @ManyToOne
    @JoinColumn(name = "studentemail", referencedColumnName = "email")
    private Student student;

    @Column(name = "score")
    private Float score;

    // Costruttori, getter e setter

    public static class TournamentRankingId implements Serializable {
        private Integer tournament;
        private String student;
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((tournament == null) ? 0 : tournament.hashCode());
            result = prime * result + ((student == null) ? 0 : student.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TournamentRankingId other = (TournamentRankingId) obj;
            if (tournament == null) {
                if (other.tournament != null)
                    return false;
            } else if (!tournament.equals(other.tournament))
                return false;
            if (student == null) {
                if (other.student != null)
                    return false;
            } else if (!student.equals(other.student))
                return false;
            return true;
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

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tournament == null) ? 0 : tournament.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TournamentRanking other = (TournamentRanking) obj;
        if (tournament == null) {
            if (other.tournament != null)
                return false;
        } else if (!tournament.equals(other.tournament))
            return false;
        return true;
    }
    
}
