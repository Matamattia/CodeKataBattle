package com.codekatabattle.codebattle.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Repository.ProjectRepository;

import Scheduler.BattleDeadlineJob;




@Service
public class BattleService {
    @Autowired
    private BattleRepository battleRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private Scheduler scheduler;


    
    public Optional<Battle> battle(Battle.BattleId id) {
        return battleRepository.findById(id);
    }
    //add battle, pointless to check whether the educator has permissions because where he doesn't have permissions, 
    //the button doesn't really show up at all
    public Battle saveBattle(Battle battle) {
        Battle savedBattle = battleRepository.save(battle);
        // Pianifica un job Quartz per la registrationDeadline
        scheduleDeadlineJob(savedBattle, savedBattle.getRegistrationDeadline(), "Registration");

        // Pianifica un altro job Quartz per la submissionDeadline
        scheduleDeadlineJob(savedBattle, savedBattle.getSubmissionDeadline(), "Submission");
        //return battleRepository.save(battle);
        return savedBattle;
    }
    

    private void scheduleDeadlineJob(Battle battle, Date deadline, String deadlineType) {
        JobDetail jobDetail = JobBuilder.newJob(BattleDeadlineJob.class)
            .withIdentity("battleDeadlineJob-" + deadlineType + "-" + battle.getBattleId(), "battles")
            .usingJobData("battleId", battle.getBattleId())
            .usingJobData("deadlineType", deadlineType)
            .build();

        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("trigger-" + deadlineType + "-" + battle.getBattleId(), "battles")
            .startAt(deadline)
            .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            // Gestisci l'eccezione
        }
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
