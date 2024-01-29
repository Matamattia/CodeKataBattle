package com.codekatabattle.codebattle.Controller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codekatabattle.codebattle.DTO.GitHubPushPayload;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Repository.TeamRepository;
import com.codekatabattle.codebattle.Service.AutomatedEvaluationService;
import com.codekatabattle.codebattle.Service.ProjectService;
import com.codekatabattle.codebattle.Service.RankingService;


@Controller
public class GitHubController {
    TeamRepository teamRepository;
    ProjectService projectService;
    RankingService rankingService;
    AutomatedEvaluationService automatedEvaluationService;


    //CONSIDERA CHE HO CAMBIATO DATE IN LOCALDATETIME IN BATTLE
    //Permette ad ogni push di calcolare il nuovo ranking della battaglia, basandosi sui parametri forniti
    @PostMapping("/webhook")
    public ResponseEntity<String> handleGitHubPush(@RequestBody GitHubPushPayload payload) {
        // Cerco il team associato alla repository che ha effettuato il push
        String teamName = payload.getRepositoryName(); // ASSUMO CHE I NOMI DELLE REOSITORY SIANO UGUALI AI NOMI DEI TEAM;
        Team team = teamRepository.findByName(teamName);
        //Ora, avendo il team, creo il progetto se non esiste gia, altrimenti lo aggiorno con il nuovo codekata pushato
        Project project =  projectService.createOrUpdateProject(team, payload.getRepositoryName(), payload.getCodeKataBattle());

        // calcolo gli score basandomi sui parametri di tempo e test passati forniti da github
        Float functionalScore = payload.getFunctionalScore()*100;//  // aggiusta facendoti fornire come functionalscore nello yaml, Il rapporto trai test totali e test passati
        Float timelinessScore = calculateTimelinessScore(payload.getPushTime(), team.getBattle().getRegistrationDeadline(), team.getBattle().getSubmissionDeadline());

        // ora creo l'automted evaluation associata a quel team per quel progetto, oppure la aggiorno se esiste gia
        automatedEvaluationService.createOrUpdateEvaluation(project, functionalScore, timelinessScore);

        // ora recupero id di battle e torunamen tramite il team
        int battleId = team.getBattle().getBattleId();
        int tournamentId = team.getBattle().getTournament().getId();
        // Delega il calcolo dello ranking o l'aggiornamento del ranking al RankingService
        rankingService.calculateBattleRanking(battleId, tournamentId);

        return ResponseEntity.ok("Push event processed and ranking updated");
    }



    private float calculateTimelinessScore(LocalDateTime pushTime, LocalDateTime registrationDeadline, LocalDateTime submissionDeadline) {
       
    
        long totalDuration = Duration.between(registrationDeadline, submissionDeadline).toMinutes();
        long pushDuration = Duration.between(registrationDeadline, pushTime).toMinutes();
    
        if (pushDuration < 0 || totalDuration == 0) return 0; // Push prima dell'inizio o durata totale zero
    
        float score = (1 - ((float)pushDuration / totalDuration)) * 100;
        return Math.max(score, 0); // Assicurati che il punteggio non sia negativo
    }
    
    
}
