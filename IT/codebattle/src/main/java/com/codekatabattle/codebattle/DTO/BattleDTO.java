package com.codekatabattle.codebattle.DTO;

import java.util.Date;


public class BattleDTO {
 
    private Integer battleId;
    private Integer tournamentId; 
    private String linkRepository;
    private Integer minStudent;
    private Integer maxStudent;
    private String descriptionCodeKata;
    private String fileType;
    private Date registrationDeadline;
    private Date submissionDeadline;
    private Boolean isEvaluatedManual;
    public Integer getBattleId() {
        return battleId;
    }
    public void setBattleId(Integer battleId) {
        this.battleId = battleId;
    }
    public Integer getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
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
    
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }
    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }
    public Date getSubmissionDeadline() {
        return submissionDeadline;
    }
    public void setSubmissionDeadline(Date submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }
    public Boolean getIsEvaluatedManual() {
        return isEvaluatedManual;
    }
    public void setIsEvaluatedManual(Boolean isEvaluatedManual) {
        this.isEvaluatedManual = isEvaluatedManual;
    }

   
}

