package com.codekatabattle.codebattle.Model;

import jakarta.persistence.*;
import java.util.Date;
import java.io.Serializable;

@Entity
@Table(name = "Battle")
@IdClass(Battle.BattleId.class)
public class Battle {
    @Id
    @Column(name = "battleId")
    private Integer battleId;

    @Id
    @ManyToOne
    @JoinColumn(name = "tournamentId", referencedColumnName = "id")
    private Tournament tournament;

    @Column(name = "linkRepository", columnDefinition = "TEXT")
    private String linkRepository;

    @Column(name = "minStudent")
    private Integer minStudent;

    @Column(name = "maxStudent")
    private Integer maxStudent;

    @Column(name = "descriptionCodeKata", columnDefinition = "TEXT")
    private String descriptionCodeKata;

    @Column(name = "codeKataTests")
    private byte[] codeKataTests;

    @Column(name = "registrationDeadline")
    private Date registrationDeadline;

    @Column(name = "submissionDeadline")
    private Date submissionDeadline;

    @Column(name = "isEvaluatedManual")
    private Boolean isEvaluatedManual;

    // Costruttori, getter e setter

    public static class BattleId implements Serializable {
        private Integer battleId;
        private Integer tournament;

        // Costruttori, getter e setter, equals, hashCode

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            BattleId battleId1 = (BattleId) o;

            if (battleId != null ? !battleId.equals(battleId1.battleId) : battleId1.battleId != null)
                return false;
            return tournament != null ? tournament.equals(battleId1.tournament) : battleId1.tournament == null;
        }

        @Override
        public int hashCode() {
            int result = battleId != null ? battleId.hashCode() : 0;
            result = 31 * result + (tournament != null ? tournament.hashCode() : 0);
            return result;
        }
    }
}
