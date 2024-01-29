package com.codekatabattle.codebattle.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Repository.ProjectRepository;

@Service
public class BattleService {
    @Autowired
    private BattleRepository battleRepository;
    @Autowired
    private ProjectRepository projectRepository;


    //maybe useless
    public Optional<Battle> battle(Battle.BattleId id) {
        return battleRepository.findById(id);
    }
    //add battle, pointless to check whether the educator has permissions because where he doesn't have permissions, 
    //the button doesn't really show up at all
    public Battle saveBattle(Battle battle) {
        //notify the students
        return battleRepository.save(battle);
    }
    //save battle, same for save
    public void updateBattle(Battle battle) {
        battleRepository.save(battle);
    }
    //delete battle, same for save 
    public void deleteBattle(Battle.BattleId id) {
        battleRepository.deleteById(id);
    }
    //from id obtain project
    public Optional<Project> getProject(Integer projectId){
        return projectRepository.findById(projectId);
    }
     public boolean verifyBattleActive(Integer tournamentId) {
        List<Battle> battles = battleRepository.findByTournamentId(tournamentId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        for (Battle battle : battles) {
            LocalDateTime submissionDeadline = battle.getSubmissionDeadline();

            if (submissionDeadline != null && submissionDeadline.isAfter(currentDateTime)) {
            } else {
                return false;
            }
        }
        
        return true;
    }

}
