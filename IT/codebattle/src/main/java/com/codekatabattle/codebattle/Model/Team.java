package com.codekatabattle.codebattle.Model;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;


@Entity
@Table(name = "Team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamid")
    private Integer teamId;


    
    @Column(name = "name",unique = true)
    private String name;

    @Column(name = "codiceinvito")
    private String codiceInvito;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
        @JoinColumn(name = "battleid", referencedColumnName = "battleid"),
        @JoinColumn(name = "tournamentid", referencedColumnName = "tournamentid")
    })
    private Battle battle;


    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
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
        Team other = (Team) obj;
        if (teamId == null) {
            if (other.teamId != null)
                return false;
        } else if (!teamId.equals(other.teamId))
            return false;
        return true;
    }

    public String getCodiceInvito() {
        return codiceInvito;
    }

    public void setCodiceInvito(String codiceInvito) {
        this.codiceInvito = codiceInvito;
    }
    
}

