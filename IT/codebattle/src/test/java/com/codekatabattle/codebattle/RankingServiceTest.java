package com.codekatabattle.codebattle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.codekatabattle.codebattle.Repository.BattleRankingRepository;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Repository.ManualEvaluationRepository;
import com.codekatabattle.codebattle.Repository.ProjectRepository;
import com.codekatabattle.codebattle.Repository.StudentRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;
import com.codekatabattle.codebattle.Repository.TournamentRankingRepository;
import com.codekatabattle.codebattle.Repository.TournamentRepository;
import com.codekatabattle.codebattle.Service.RankingService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RankingServiceTest {

    @Mock
    private BattleRepository battleRepository;
    @Mock
    private BattleRankingRepository battleRankingRepository;
    @Mock
    private TournamentRankingRepository tournamentRankingRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AutomatedEvaluationRepository automatedEvaluationRepository;
    @Mock
    private ManualEvaluationRepository manualEvaluationRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private RankingService rankingService;

    /*Verifica che il metodo createTournamentRanking calcoli e salvi correttamente le classifiche di un torneo, 
    basandosi sui dati forniti da un altro metodo del repository che calcola i ranking. */
    @Test
public void testGetBattleRanking() {
    int battleId = 1, tournamentId = 1;
    List<BattleRanking> expectedRankings = Arrays.asList(new BattleRanking());
    
    when(battleRankingRepository.findByBattleIdAndTournamentId(battleId, tournamentId)).thenReturn(expectedRankings);
    
    List<BattleRanking> actualRankings = rankingService.getBattleRanking(battleId, tournamentId);
    
    assertNotNull(actualRankings);
    assertFalse(actualRankings.isEmpty());
    assertEquals(expectedRankings, actualRankings);
}
/*Verifica che il metodo getTournamentRanking recuperi correttamente la classifica di un torneo specifico in base al suo ID. */
@Test
public void testGetTournamentRanking() {
    int tournamentId = 1;
    List<TournamentRanking> expectedRankings = Arrays.asList(new TournamentRanking());
    
    when(tournamentRankingRepository.findByTournamentId(tournamentId)).thenReturn(expectedRankings);
    
    List<TournamentRanking> actualRankings = rankingService.getTournamentRanking(tournamentId);
    
    assertNotNull(actualRankings);
    assertFalse(actualRankings.isEmpty());
    assertEquals(expectedRankings, actualRankings);
}
/* verifica che il metodo recuperi i ranking calcolati e salvi correttamente i TournamentRanking nel repository. Per semplicità, assumeremo che il metodo tournamentRankingRepository.calculateTournamentRanking restituisca una lista di DTO, 
e che gli studenti esistano per ciascun email nei DTO. */
@Test
public void testCreateTournamentRanking() {
    int tournamentId = 1;
    Tournament tournament = new Tournament();
    Student student = new Student();
    student.setEmail("student@example.com");
    List<TournamentRankingDTO> rankingDTOs = Arrays.asList(new TournamentRankingDTO("student@example.com", 100f));
    
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
    when(studentRepository.findById("student@example.com")).thenReturn(Optional.of(student));
    when(tournamentRankingRepository.calculateTournamentRanking(tournamentId)).thenReturn(rankingDTOs);
    
    rankingService.createTournamentRanking(tournamentId);
    
    verify(tournamentRankingRepository, times(1)).save(any(TournamentRanking.class));
}
/*Questo test verifica che il metodo calculateBattleRanking calcoli correttamente il punteggio totale per ogni team in una battaglia, 
combinando i punteggi di valutazione automatica e manuale, e salvi i risultati nel BattleRankingRepository. */
@Test
public void testCalculateBattleRanking() {
    int battleId = 1, tournamentId = 1;
    Battle battle = new Battle();
    Team team = new Team();
    team.setTeamId(1);
    Project project = new Project();
    project.setProjectId(1);
    AutomatedEvaluation automatedEvaluation = new AutomatedEvaluation();
    automatedEvaluation.setTotalScore(50f);
    ManualEvaluation manualEvaluation = new ManualEvaluation();
    manualEvaluation.setPersonalScore(50f);

    when(battleRepository.findById(any(Battle.BattleId.class))).thenReturn(Optional.of(battle));
    when(teamRepository.findByBattle_BattleIdAndBattle_Tournament_Id(battleId, tournamentId)).thenReturn(Arrays.asList(team));
    when(projectRepository.findByTeam_TeamId(team.getTeamId())).thenReturn(project);
    when(automatedEvaluationRepository.findByProjectId(project.getProjectId())).thenReturn(automatedEvaluation);
    when(manualEvaluationRepository.findByAutomatedEvaluationId(automatedEvaluation.getId())).thenReturn(Optional.of(manualEvaluation));

    rankingService.calculateBattleRanking(battleId, tournamentId);

    verify(battleRankingRepository, times(1)).save(any(BattleRanking.class));
}

}

