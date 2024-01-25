package com.codekatabattle.codebattle.Model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "AuthorizedEducators")
@IdClass(AuthorizedEducator.AuthorizedEducatorsId.class)
public class AuthorizedEducator {

    @Id
    @ManyToOne
    @JoinColumn(name = "tournamentid", referencedColumnName = "id")
    private Tournament tournament;

    @Id
    @ManyToOne
    @JoinColumn(name = "authorizededucator", referencedColumnName = "email")
    private Educator educator;






    public static class AuthorizedEducatorsId implements Serializable {
        private Integer tournament;
        private String educator;

        // Costruttori, getters, setters, equals, hashCode

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            AuthorizedEducatorsId that = (AuthorizedEducatorsId) o;

            if (tournament != null ? !tournament.equals(that.tournament) : that.tournament != null)
                return false;
            return educator != null ? educator.equals(that.educator) : that.educator == null;
        }

        @Override
        public int hashCode() {
            int result = tournament != null ? tournament.hashCode() : 0;
            result = 31 * result + (educator != null ? educator.hashCode() : 0);
            return result;
        }
    }





    public Tournament getTournament() {
        return tournament;
    }





    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }





    public Educator getEducator() {
        return educator;
    }





    public void setEducator(Educator educator) {
        this.educator = educator;
    }





    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tournament == null) ? 0 : tournament.hashCode());
        result = prime * result + ((educator == null) ? 0 : educator.hashCode());
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
        AuthorizedEducator other = (AuthorizedEducator) obj;
        if (tournament == null) {
            if (other.tournament != null)
                return false;
        } else if (!tournament.equals(other.tournament))
            return false;
        if (educator == null) {
            if (other.educator != null)
                return false;
        } else if (!educator.equals(other.educator))
            return false;
        return true;
    }





    @Override
    public String toString() {
        return "AuthorizedEducator [tournament=" + tournament + ", educator=" + educator + "]";
    }
    
    
}
