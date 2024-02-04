package com.codekatabattle.codebattle.Service;

import com.codekatabattle.codebattle.DTO.TournamentRankingDTO;
import com.codekatabattle.codebattle.Model.AutomatedEvaluation;
import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.BattleRanking;
import com.codekatabattle.codebattle.Model.ManualEvaluation;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Model.TournamentRanking;
import com.codekatabattle.codebattle.Repository.AutomatedEvaluationRepository;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Repository.ManualEvaluationRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;
import com.codekatabattle.codebattle.Repository.TournamentRankingRepository;
import com.codekatabattle.codebattle.Repository.TournamentRepository;
import com.codekatabattle.codebattle.Repository.BattleRankingRepository;
import jakarta.persistence.EntityNotFoundException;
import com.codekatabattle.codebattle.Repository.ProjectRepository;
import com.codekatabattle.codebattle.Repository.StudentRepository;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class RankingService {
    private final BattleRepository battleRepository;
    private final BattleRankingRepository battleRankingRepository;
    private final TournamentRankingRepository tournamentRankingRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final AutomatedEvaluationRepository automatedEvaluationRepository;
    private final ManualEvaluationRepository manualEvaluationRepository;
    private final StudentRepository studentRepository;
    

   


public RankingService(BattleRepository battleRepository, BattleRankingRepository battleRankingRepository,
            TournamentRankingRepository tournamentRankingRepository, TournamentRepository tournamentRepository,
            TeamRepository teamRepository, ProjectRepository projectRepository,
            AutomatedEvaluationRepository automatedEvaluationRepository,
            ManualEvaluationRepository manualEvaluationRepository, StudentRepository studentRepository) {
        this.battleRepository = battleRepository;
        this.battleRankingRepository = battleRankingRepository;
        this.tournamentRankingRepository = tournamentRankingRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
        this.automatedEvaluationRepository = automatedEvaluationRepository;
        this.manualEvaluationRepository = manualEvaluationRepository;
        this.studentRepository = studentRepository;
    }

    public List<BattleRanking> getBattleRanking(int battleId, int tournamentId) {
        return battleRankingRepository.findByBattleIdAndTournamentId(battleId, tournamentId);
    }
    
    

    public List<TournamentRanking> getTournamentRanking(int tournamentId) {
        return tournamentRankingRepository.findByTournamentId(tournamentId);
    }
    
    



public void createTournamentRanking(int tournamentId) {
        List<TournamentRankingDTO> rankings = tournamentRankingRepository.calculateTournamentRanking(tournamentId);
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new EntityNotFoundException("Tournament not found"));

        for (TournamentRankingDTO dto : rankings) {
            Student student = studentRepository.findById(dto.getStudentEmail()).orElseThrow(() -> new EntityNotFoundException("Student not found"));

            TournamentRanking tournamentRanking = new TournamentRanking();
            tournamentRanking.setStudent(student);
            tournamentRanking.setTournament(tournament);
            tournamentRanking.setScore(dto.getTotalScore().floatValue());

            tournamentRankingRepository.save(tournamentRanking);
        }
    }



// Aggiorna tutta la classifica della battaglia ,basandosi sulla valutazione manuale e su quella automatica
public void calculateBattleRanking(int battleId, int tournamentId) {
    // Recupera tutti i team partecipanti alla battaglia
    
    Battle.BattleId id = new Battle.BattleId();
    id.setBattleId(battleId); // Assumendo che battleId sia un Integer
    id.setTournament(tournamentId);
    
    Battle battle = battleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Battle not found"));
    List<Team> teams = teamRepository.findByBattle_BattleIdAndBattle_Tournament_Id(battleId,tournamentId);


    for (Team team : teams) {
        
        Project project = projectRepository.findByTeam_TeamId(team.getTeamId());

        // Recupera le valutazioni per il progetto
        AutomatedEvaluation automatedEvaluation = automatedEvaluationRepository.findByProjectId(project.getProjectId());
        // Inizializza il punteggio con il punteggio automatico
        float totalScore = automatedEvaluation.getTotalScore();

        // Controlla se esiste una valutazione manuale
        Optional<ManualEvaluation> manualEvaluationOpt = manualEvaluationRepository.findByAutomatedEvaluationId(automatedEvaluation.getId());
if (manualEvaluationOpt.isPresent()) {
    ManualEvaluation manualEvaluation = manualEvaluationOpt.get();
    totalScore += manualEvaluation.getPersonalScore();
}


        
        BattleRanking ranking = new BattleRanking();
        ranking.setBattle(battle);
        ranking.setTeam(team);
        ranking.setScore(totalScore);

        battleRankingRepository.save(ranking);
    }


}

    
}
