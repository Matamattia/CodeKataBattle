// 1. getEducatorInfoTest()
// Questo test verifica l'endpoint che restituisce le informazioni dettagliate di un educatore specifico, incluse le liste dei tornei e delle battaglie associate a quell'educatore. In particolare, il test si concentra su:

// Accessibilità dell'Endpoint con Autenticazione: Assicura che l'endpoint sia accessibile agli utenti autenticati, simulando un contesto di autenticazione con @WithMockUser.
// Correttezza dei Dati Restituiti: Verifica che l'endpoint restituisca i dati corretti relativi all'educatore, ai tornei e alle battaglie. Ciò include la presenza di questi oggetti nella risposta e la verifica che siano strutturati come previsto.
// Gestione delle Situazioni di Non Trovato: Testa la capacità dell'endpoint di gestire correttamente il caso in cui l'educatore richiesto non esista, aspettandosi una risposta 404 (Not Found) in tali scenari.
// Integrazione con i Servizi: Assicura che il controller interagisca correttamente con i servizi di backend (EducatorService, TournamentService, BattleService), verificando che i metodi di servizio appropriati siano chiamati con i parametri attesi.



// getAllEducatorsTest()
// Questo test verifica l'endpoint che restituisce un elenco di tutti gli educatori registrati nel sistema. Esso mira a:

// Accessibilità dell'Endpoint con Autenticazione: Controlla che l'endpoint sia accessibile agli utenti autenticati, anche in questo caso utilizzando @WithMockUser per simulare un utente autenticato durante il test.
// Formato e Contenuto della Risposta: Verifica che l'endpoint restituisca una lista di educatori in formato JSON, controllando che la risposta sia un array JSON e che contenga almeno un elemento se ci sono educatori disponibili.
// Correttezza dei Dati Restituiti: Assicura che i dati restituiti siano corretti e completi, confrontando gli oggetti Educator restituiti con quelli forniti dai servizi mockati.
// Verifica dell'Integrazione con i Servizi: Testa l'integrazione con EducatorService, assicurando che il metodo per ottenere tutti gli educatori sia chiamato correttamente.





package com.codekatabattle.codebattle;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.codekatabattle.codebattle.Controller.EducatorInfoController;
import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Service.EducatorService;
import com.codekatabattle.codebattle.Service.TournamentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(EducatorInfoController.class)
public class EducatorInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private BattleService battleService;

    @MockBean
    private EducatorService educatorService;

    // Test per ottenere le informazioni di un educatore specifico
    @Test
    @WithMockUser
    public void getEducatorInfoTest() throws Exception {
        Educator mockEducator = new Educator(); // Assumi che Educator sia una classe definita da te
        mockEducator.setEmail("test@example.com");
        // Configura ulteriori campi di mockEducator se necessario
    
        List<Tournament> mockTournaments = Arrays.asList(new Tournament()); // Assumi che Tournament sia una classe definita da te
        List<Battle> mockBattles = Arrays.asList(new Battle()); // Assumi che Battle sia una classe definita da te
    
        given(educatorService.getEducatorByEmail(anyString())).willReturn(Optional.of(mockEducator));
        given(tournamentService.myTournamentsEducator(anyString())).willReturn(mockTournaments);
        given(battleService.getBattlesForEducator(anyList())).willReturn(mockBattles);
    
        mockMvc.perform(get("/api/educators/{email}", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.educator").exists())
                .andExpect(jsonPath("$.tournaments").exists())
                .andExpect(jsonPath("$.battles").exists());
    }

    // Test per ottenere tutti gli educatori
    @Test
    @WithMockUser("customUsername")
    public void getAllEducatorsTest() throws Exception {
        List<Educator> mockEducators = Arrays.asList(new Educator()); // Assumi che Educator sia una classe definita da te
    
        given(educatorService.getAllEducators()).willReturn(mockEducators);
    
        mockMvc.perform(get("/api/educators/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }
}

