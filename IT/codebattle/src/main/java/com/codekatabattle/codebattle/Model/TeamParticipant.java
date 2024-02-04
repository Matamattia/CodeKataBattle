package com.codekatabattle.codebattle.Model;
import java.io.Serializable;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

@Entity
@Table(name = "teamparticipant")
@IdClass(TeamParticipant.TeamParticipantId.class)
public class TeamParticipant {
   @Id
    @ManyToOne
    @JoinColumn(name = "teamid", referencedColumnName = "teamid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team team;

    @Id
    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    private Student student;

    // Costruttori, getter e setter
    
    public static class TeamParticipantId implements Serializable {
        private Integer team;
        private String student;
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((team == null) ? 0 : team.hashCode());
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
            TeamParticipantId other = (TeamParticipantId) obj;
            if (team == null) {
                if (other.team != null)
                    return false;
            } else if (!team.equals(other.team))
                return false;
            if (student == null) {
                if (other.student != null)
                    return false;
            } else if (!student.equals(other.student))
                return false;
            return true;
        }

        
        
        
        // Costruttori, getter e setter, equals, hashCode
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    
}
