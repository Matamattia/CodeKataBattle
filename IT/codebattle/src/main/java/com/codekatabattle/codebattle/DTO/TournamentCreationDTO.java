package com.codekatabattle.codebattle.DTO;

import java.util.List;

import com.codekatabattle.codebattle.Model.AuthorizedEducator;
import com.codekatabattle.codebattle.Model.Tournament;

public class TournamentCreationDTO {
    private Tournament newTournament;
    private List<AuthorizedEducator> educators;
    public Tournament getNewTournament() {
        return newTournament;
    }
    public void setNewTournament(Tournament newTournament) {
        this.newTournament = newTournament;
    }
    public List<AuthorizedEducator> getEducators() {
        return educators;
    }
    public void setEducators(List<AuthorizedEducator> educators) {
        this.educators = educators;
    }

    
}
