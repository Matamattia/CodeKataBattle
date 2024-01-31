package com.codekatabattle.codebattle.DTO;

import java.util.List;

public class TeamDTO {
    private String name;
    private List<String> studentEmails;
    private Integer battleId;
    private String creatorEmail;
    private Integer tournamentId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getStudentEmails() {
        return studentEmails;
    }
    public void setStudentEmails(List<String> studentEmails) {
        this.studentEmails = studentEmails;
    }
    public Integer getBattleId() {
        return battleId;
    }
    public void setBattleId(Integer battleId) {
        this.battleId = battleId;
    }
    public String getCreatorEmail() {
        return creatorEmail;
    }
    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }
    public Integer getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }
    
}
