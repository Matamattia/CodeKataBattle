package com.codekatabattle.codebattle.DTO;

import java.util.List;

import com.codekatabattle.codebattle.Model.Tournament;

public class TournamenteCreateDTO {
    private Tournament tournament;
    private List<String> educatorEmails;
    private String educatorCreator;
    public Tournament getTournament() {
        return tournament;
    }
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    public List<String> getEducatorEmails() {
        return educatorEmails;
    }
    public void setEducatorEmails(List<String> educatorEmails) {
        this.educatorEmails = educatorEmails;
    }
    public String getEducatorCreator() {
        return educatorCreator;
    }
    public void setEducatorCreator(String educatorCreator) {
        this.educatorCreator = educatorCreator;
    }
    
    
}
