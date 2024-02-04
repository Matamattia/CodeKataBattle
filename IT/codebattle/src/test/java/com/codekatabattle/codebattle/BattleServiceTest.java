package com.codekatabattle.codebattle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.test.context.SpringBootTest;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Repository.ProjectRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;;


@SpringBootTest
public class BattleServiceTest {

    @Mock
    private BattleRepository battleRepository;

    @Mock
    private Scheduler scheduler; // Simulare le dipendenze rilevanti

    @InjectMocks
    private BattleService battleService;

     @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TeamRepository teamRepository;

    @BeforeEach
    public void setup() {
        // Setup iniziale per i test, se necessario
    }

    @Test
    public void testBattleFound() {
        // Configurazione del test
        Battle.BattleId id = new Battle.BattleId();
        id.setBattleId(1);
        id.setTournament(1);

        Battle expectedBattle = new Battle();
        expectedBattle.setBattleId(1);
        expectedBattle.setTournament(new Tournament());
        
        when(battleRepository.findById(id)).thenReturn(Optional.of(expectedBattle));

        // Esecuzione del metodo da testare
        Optional<Battle> result = battleService.battle(id);

        // Verifica dei risultati
        assertEquals(true, result.isPresent());
        assertEquals(expectedBattle, result.get());
    }

    @Test
    public void testSaveBattle() throws SchedulerException {
        // Configurazione del test
        Battle battle = new Battle();
        battle.setTournament(new Tournament());
        battle.setRegistrationDeadline(LocalDateTime.now().plusDays(10));
        battle.setSubmissionDeadline(LocalDateTime.now().plusDays(20));

        when(battleRepository.findMaxBattleId()).thenReturn(1); // Simula il ritorno del massimo ID esistente
        when(battleRepository.save(any(Battle.class))).thenAnswer(i -> i.getArguments()[0]);

        // Esecuzione del metodo da testare
        Battle savedBattle = battleService.saveBattle(battle);

        // Verifica che il metodo save del repository sia stato chiamato
        verify(battleRepository, times(1)).save(battle);
        // Verifica che le deadline siano state pianificate correttamente
        verify(scheduler, times(2)).scheduleJob(any(), any());

        // Verifica dei risultati
        assertEquals(Integer.valueOf(2), savedBattle.getBattleId()); // Verifica che l'ID sia stato incrementato
    }

    /*Questo esempio mostra come testare due metodi di BattleService. Per testBattleFound, si verifica che il metodo 
    battle restituisca correttamente un Optional<Battle> quando il BattleRepository trova una corrispondenza. 
    Per testSaveBattle, si simula la logica di incremento dell'ID battaglia, la persistenza della battaglia, 
    e la pianificazione dei job Quartz, verificando che ogni passo sia eseguito come previsto. */




/*Questo metodo verifica se una battaglia associata a un dato torneo è attiva basandosi
 sulle deadline di registrazione e invio.  */
@Test
public void testVerifyBattleActive() {
    // Configurazione del test
    Integer tournamentId = 1;
    List<Battle> battles = new ArrayList<>();
    Battle battle = new Battle();
    battle.setRegistrationDeadline(LocalDateTime.now().minusDays(1)); // Ieri
    battle.setSubmissionDeadline(LocalDateTime.now().plusDays(1)); // Domani
    battles.add(battle);

    when(battleRepository.findByTournamentId(tournamentId)).thenReturn(battles);

    // Esecuzione del metodo da testare
    boolean isActive = battleService.verifyBattleActive(tournamentId);

    // Verifica dei risultati
    assertEquals(false, isActive); // La battaglia dovrebbe essere considerata attiva
    verify(battleRepository, times(1)).findByTournamentId(tournamentId);
}


//Questo metodo si aspetta di aggiornare un'istanza di Battle esistente
    @Test
public void testUpdateBattle() {
    // Configurazione del test
    Battle battleToUpdate = new Battle();
    battleToUpdate.setBattleId(1);
    battleToUpdate.setDescriptionCodeKata("Updated Description");

    when(battleRepository.save(any(Battle.class))).thenReturn(battleToUpdate);

    // Esecuzione del metodo da testare
    battleService.updateBattle(battleToUpdate);

    // Verifica che il metodo save del repository sia stato chiamato con l'entità corretta
    ArgumentCaptor<Battle> battleCaptor = ArgumentCaptor.forClass(Battle.class);
    verify(battleRepository).save(battleCaptor.capture());
    Battle capturedBattle = battleCaptor.getValue();

    assertEquals("Updated Description", capturedBattle.getDescriptionCodeKata());
}
//Questo metodo elimina una battaglia dato il suo identificativo.
@Test
public void testDeleteBattle() {
    // Configurazione del test
    Battle.BattleId idToDelete = new Battle.BattleId();
    idToDelete.setBattleId(1);
    idToDelete.setTournament(1);

    doNothing().when(battleRepository).deleteById(any(Battle.BattleId.class));

    // Esecuzione del metodo da testare
    battleService.deleteBattle(idToDelete);

    // Verifica che il metodo deleteById del repository sia stato chiamato con l'ID corretto
    ArgumentCaptor<Battle.BattleId> idCaptor = ArgumentCaptor.forClass(Battle.BattleId.class);
    verify(battleRepository).deleteById(idCaptor.capture());
    Battle.BattleId capturedId = idCaptor.getValue();

    assertEquals(Integer.valueOf(1), capturedId.getBattleId());
    assertEquals(Integer.valueOf(1), capturedId.getTournament());
}
//ArgumentCaptor per catturare e verificare i parametri passati ai metodi del repository.
// È una pratica utile per confermare che i dati corretti siano manipolati dai servizi.



/*Questo metodo si aspetta di recuperare un progetto dato il suo ID. Nel test, ci concentreremo sulla verifica che il metodo 
findById del projectRepository sia stato chiamato e restituisca il progetto atteso. */
@Test
public void testGetProject() {
    // Configurazione del test
    Integer projectId = 1;
    Project expectedProject = new Project();
    expectedProject.setProjectId(projectId);
    expectedProject.setGithubRepository("https://github.com/example/repo");

    when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));

    // Esecuzione del metodo da testare
    Optional<Project> result = battleService.getProject(projectId);

    // Verifica dei risultati
    assertTrue(result.isPresent(), "Il progetto dovrebbe essere trovato");
    assertEquals(expectedProject, result.get(), "Il progetto restituito dovrebbe corrispondere al progetto atteso");
    assertEquals(projectId, result.get().getProjectId(), "L'ID del progetto restituito dovrebbe corrispondere all'ID richiesto");

    // Verifica che il metodo findById del repository sia stato chiamato con l'ID corretto
    verify(projectRepository, times(1)).findById(projectId);
}



}


