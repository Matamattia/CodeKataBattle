package com.codekatabattle.codebattle.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Repository.TeamRepository;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;


    //createTeam
    //per come l'abbiamo pensato questo è sia add team che create team, perchè di volta in volta viene aggiunto uno studente
    public Team joinTeam(Team team) {
        return teamRepository.save(team);
    }
    

    
}
