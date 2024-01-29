package com.codekatabattle.codebattle.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Repository.TeamParticipantRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamParticipantRepository teamParticipantRepository;

    //createTeam
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }
    
    public TeamParticipant joinTeam(TeamParticipant teamParticipant){
        return teamParticipantRepository.save(teamParticipant);
    }
    
}
