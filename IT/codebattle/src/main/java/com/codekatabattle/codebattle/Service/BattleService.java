package com.codekatabattle.codebattle.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Repository.ProjectRepository;
import com.codekatabattle.codebattle.Repository.TeamParticipantRepository;

import Scheduler.BattleDeadlineJob;




@Service
public class BattleService {
    @Autowired
    private BattleRepository battleRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private TeamParticipantRepository teamParticipantRepository;



    
    public Optional<Battle> battle(Battle.BattleId id) {
        return battleRepository.findById(id);
    }
    //add battle, pointless to check whether the educator has permissions because where he doesn't have permissions, 
    //the button doesn't really show up at all
    public Battle saveBattle(Battle battle) {
        Integer idBattle = battleRepository.findMaxBattleId();
        idBattle += 1;//id Battle
        battle.setBattleId(idBattle);
        Battle savedBattle = battleRepository.save(battle);
        // Pianifica un job Quartz per la registrationDeadline
        scheduleDeadlineJob(savedBattle, savedBattle.getRegistrationDeadline(), "Registration");

        // Pianifica un altro job Quartz per la submissionDeadline
        scheduleDeadlineJob(savedBattle, savedBattle.getSubmissionDeadline(), "Submission");
        //return battleRepository.save(battle);
        return savedBattle;
    }
    

    private void scheduleDeadlineJob(Battle battle, LocalDateTime deadline, String deadlineType) {
        JobDetail jobDetail = JobBuilder.newJob(BattleDeadlineJob.class)
            .withIdentity("battleDeadlineJob-" + deadlineType + "-" + battle.getBattleId(), "battles")
            .usingJobData("battleId", battle.getBattleId())
            .usingJobData("deadlineType", deadlineType)
            .build();

            Date startDate = Date.from(deadline.atZone(ZoneId.systemDefault()).toInstant());
        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("trigger-" + deadlineType + "-" + battle.getBattleId(), "battles")
            .startAt(startDate)
            .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("ENTRATO IN SCHEDULEDEADLINEJOB");
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



     public List<Battle> getBattlesForStudent(String studentEmail) {
        List<TeamParticipant> teamParticipants = teamParticipantRepository.findByStudentEmail(studentEmail);
        return teamParticipants.stream()
                               .map(teamParticipant -> teamParticipant.getTeam().getBattle())
                               .distinct()
                               .collect(Collectors.toList());
    }


    public List<Battle> getBattlesForEducator(List<Integer> tournamentIDs){
        return battleRepository.findBattlesByTournamentIds(tournamentIDs);
    }


    public List<Battle> getBattlesByTournament(Integer tournamentId) {
        return battleRepository.findByTournamentId(tournamentId);

    }
    public boolean checkUserRegistration(Integer battleId, String studentEmail) {
        Battle.BattleId battleIdObj = new Battle.BattleId();
        battleIdObj.setBattleId(battleId);
    
        Battle battle = battleRepository.findById(battleIdObj).orElse(null);
    
        if (battle == null) {
            return false; // La battaglia non esiste
        }
    
        // Verifica se lo studente è registrato per questa battaglia
        Student student = new Student();
        student.setEmail(studentEmail);
    
        if (teamParticipantRepository.existsByTeam_BattleAndStudent(battle, student)) {
            return true; // Lo studente è registrato per questa battaglia
        }
    
        return false; // Lo studente non è registrato per questa battaglia
    }
    

}