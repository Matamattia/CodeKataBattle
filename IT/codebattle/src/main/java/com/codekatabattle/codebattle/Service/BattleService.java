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
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Model.Battle.BattleId;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Repository.ProjectRepository;
import com.codekatabattle.codebattle.Repository.TeamParticipantRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;

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
    @Autowired
    private TeamRepository teamRepository;



    
    public Optional<Battle> battle(Battle.BattleId id) {
        return battleRepository.findById(id);
    }
    //add battle, pointless to check whether the educator has permissions because where he doesn't have permissions, 
    //the button doesn't really show up at all
    public Battle saveBattle(Battle battle) {
        
        Integer idBattle = battleRepository.findMaxBattleId();
    
    // Controlla se idBattle è null e assegna un valore appropriato
    if (idBattle == null) {
        idBattle = 0; // O un altro valore di default appropriato
    } else {
        idBattle += 1; // Incrementa solo se non è null
    }
        battle.setBattleId(idBattle);
        Battle savedBattle = battleRepository.save(battle);
        // Pianifica un job Quartz per la registrationDeadline
        scheduleDeadlineJob(savedBattle, savedBattle.getRegistrationDeadline(), "Registration");

        // Pianifica un altro job Quartz per la submissionDeadline
        scheduleDeadlineJob(savedBattle, savedBattle.getSubmissionDeadline(), "Submission");
        //return battleRepository.save(battle);
        return savedBattle;
    }




    public List<Project> getProjectsForBattle(Integer battleId, Integer tournamentId) {
        // Ottieni la lista di team per la battaglia specificata
        List<Team> teams = teamRepository.findByBattle_BattleIdAndBattle_Tournament_Id(battleId, tournamentId);

        // Per ogni team, ottieni il progetto associato e raccogli in una lista
        return teams.stream()
                    .map(team -> projectRepository.findByTeam_TeamId(team.getTeamId()))
                    .collect(Collectors.toList());
    }

    

    private void scheduleDeadlineJob(Battle battle, LocalDateTime deadline, String deadlineType) {
        BattleId battleId = new BattleId();
        battleId.setBattleId(battle.getBattleId());
        battleId.setTournament(battle.getTournament().getId());
        JobDetail jobDetail = JobBuilder.newJob(BattleDeadlineJob.class)
            .withIdentity("battleDeadlineJob-" + deadlineType + "-" + battleId, "battles")
            .usingJobData("battleId", battleId.getBattleId())  // Passando l'ID della battaglia come Integer
            .usingJobData("tournamentId", battleId.getTournament())
            .usingJobData("deadlineType", deadlineType)
            .build();

            //Date startDate = Date.from(deadline.atZone(ZoneId.systemDefault()).toInstant());
            Date startDate = Date.from(deadline.atZone(ZoneId.of("Europe/Rome")).toInstant());
            System.out.println(startDate);
            
        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("trigger-" + deadlineType + "-" + battleId, "battles")
            .startAt(startDate)
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
            LocalDateTime registrationDeadline = battle.getRegistrationDeadline();
            if (submissionDeadline != null && registrationDeadline != null && submissionDeadline.isAfter(currentDateTime) && registrationDeadline.isBefore(currentDateTime)) {
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



    public byte[] getCodeKataTeamByProjectId(Integer projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.map(Project::getCodeKataTeam).orElse(null);
    }

}