package com.codekatabattle.codebattle.Model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Battle")
@IdClass(Battle.BattleId.class)
public class Battle {
    @Id
    @Column(name = "battleid")
    private Integer battleId;

    @Id
    @ManyToOne
    @JoinColumn(name = "tournamentid", referencedColumnName = "id")
    private Tournament tournament;

    @Column(name = "linkrepository", columnDefinition = "TEXT")
    private String linkRepository;

    @Column(name = "minstudent")
    private Integer minStudent;

    @Column(name = "maxstudent")
    private Integer maxStudent;

    @Column(name = "descriptioncodekata", columnDefinition = "TEXT")
    private String descriptionCodeKata;

    @Column(name = "codekatatests")
    private byte[] codeKataTests;

    @Column(name = "filetype")
    private String fileType;

    @Column(name = "registrationdeadline")
    private LocalDateTime registrationDeadline;

    @Column(name = "submissiondeadline")
    private LocalDateTime submissionDeadline;

    @Column(name = "isEvaluatedmanual")
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


        public Integer getBattleId() {
            return battleId;
        }


        public void setBattleId(Integer battleId) {
            this.battleId = battleId;
        }


        public Integer getTournament() {
            return tournament;
        }


        public void setTournament(Integer tournament) {
            this.tournament = tournament;
        }
    }


    public Integer getBattleId() {
        return battleId;
    }


    public void setBattleId(Integer battleId) {
        this.battleId = battleId;
    }


    public Tournament getTournament() {
        return tournament;
    }


    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }


    public String getLinkRepository() {
        return linkRepository;
    }


    public void setLinkRepository(String linkRepository) {
        this.linkRepository = linkRepository;
    }


    public Integer getMinStudent() {
        return minStudent;
    }


    public void setMinStudent(Integer minStudent) {
        this.minStudent = minStudent;
    }


    public Integer getMaxStudent() {
        return maxStudent;
    }


    public void setMaxStudent(Integer maxStudent) {
        this.maxStudent = maxStudent;
    }


    public String getDescriptionCodeKata() {
        return descriptionCodeKata;
    }


    public void setDescriptionCodeKata(String descriptionCodeKata) {
        this.descriptionCodeKata = descriptionCodeKata;
    }


    public byte[] getCodeKataTests() {
        return codeKataTests;
    }


    public void setCodeKataTests(byte[] codeKataTests) {
        this.codeKataTests = codeKataTests;
    }


    public LocalDateTime getRegistrationDeadline() {
        return registrationDeadline;
    }


    public void setRegistrationDeadline(LocalDateTime registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }


    public LocalDateTime getSubmissionDeadline() {
        return submissionDeadline;
    }


    public void setSubmissionDeadline(LocalDateTime submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }


    public Boolean getIsEvaluatedManual() {
        return isEvaluatedManual;
    }


    public void setIsEvaluatedManual(Boolean isEvaluatedManual) {
        this.isEvaluatedManual = isEvaluatedManual;
    }


    public String getFileType() {
        return fileType;
    }


    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
