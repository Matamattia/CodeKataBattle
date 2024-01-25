package com.codekatabattle.codebattle.Model;

import java.io.Serializable;
import jakarta.persistence.*;


@Entity
@Table(name = "BattleRanking")
@IdClass(BattleRanking.BattleRankingId.class)
public class BattleRanking {
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "battleid", referencedColumnName = "battleid"),
        @JoinColumn(name = "tournamentId", referencedColumnName = "tournamentid")
    })
    private Battle battle;

    @Id
    @ManyToOne
    @JoinColumn(name = "teamid", referencedColumnName = "teamid")
    private Team team;

    @Column(name = "score")
    private Float score;

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((battle == null) ? 0 : battle.hashCode());
        result = prime * result + ((team == null) ? 0 : team.hashCode());
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
        BattleRanking other = (BattleRanking) obj;
        if (battle == null) {
            if (other.battle != null)
                return false;
        } else if (!battle.equals(other.battle))
            return false;
        if (team == null) {
            if (other.team != null)
                return false;
        } else if (!team.equals(other.team))
            return false;
        return true;
    }



    public static class BattleRankingId implements Serializable {
        private Battle.BattleId battle; // Chiave composta di Battle
        private Integer team;
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((battle == null) ? 0 : battle.hashCode());
            result = prime * result + ((team == null) ? 0 : team.hashCode());
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
            BattleRankingId other = (BattleRankingId) obj;
            if (battle == null) {
                if (other.battle != null)
                    return false;
            } else if (!battle.equals(other.battle))
                return false;
            if (team == null) {
                if (other.team != null)
                    return false;
            } else if (!team.equals(other.team))
                return false;
            return true;
        }

        
    }



    public Battle getBattle() {
        return battle;
    }



    public void setBattle(Battle battle) {
        this.battle = battle;
    }



    public Team getTeam() {
        return team;
    }



    public void setTeam(Team team) {
        this.team = team;
    }



    public Float getScore() {
        return score;
    }



    public void setScore(Float score) {
        this.score = score;
    }

    // Getters and Setters
}
