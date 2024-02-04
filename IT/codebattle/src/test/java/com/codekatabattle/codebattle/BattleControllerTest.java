// testGetBattle()
// Questo test verifica il comportamento dell'endpoint /battle/infoBattle, che è 
// progettato per recuperare i dettagli di una specifica "battaglia" 
// data una combinazione di battleId e tournamentId. Ecco cosa fa nel dettaglio:



// testUpdateBattle()
// Questo test controlla l'endpoint PUT generico /battle destinato all'aggiornamento di 
// una battaglia esistente. Ecco i suoi passaggi chiave:

// -Preparazione Dati: Crea un'istanza di Battle da inviare come corpo della richiesta.
// -Richiesta HTTP: Effettua una richiesta PUT all'endpoint con il corpo della richiesta 
// contenente i dati di Battle serializzati in JSON.
// -Verifica Risposta: Assicura che la risposta sia 200 OK, indicando che l'aggiornamento 
// è stato accettato e processato correttamente.
// -Verifica Comportamento Servizio: Verifica che il metodo battleService.updateBattle()
//  sia stato chiamato, confermando che il controller ha delegato correttamente l'operazione al servizio.




// testDeleteBattle()
// Questo test mira a verificare l'endpoint POST /battle/delete per eliminare una battaglia specifica. 
// Il test procede come segue:

// Preparazione Dati: Crea un'istanza di DeleteBattleRequestDTO contenente gli ID necessari per 
// identificare la battaglia da eliminare.
// Richiesta HTTP: Invia una richiesta POST all'endpoint con il DeleteBattleRequestDTO serializzato 
// in JSON nel corpo della richiesta.
// Verifica Risposta: Controlla che la risposta sia 200 OK, indicando che la richiesta di
//  eliminazione è stata eseguita con successo.
// Verifica Comportamento Servizio: Conferma che battleService.deleteBattle()
//  sia stato invocato con i parametri corretti, assicurando che il controller abbia 
//  richiesto al servizio di eseguire l'operazione di eliminazione.














package com.codekatabattle.codebattle;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

import com.codekatabattle.codebattle.Controller.BattleController;

import com.codekatabattle.codebattle.DTO.DeleteBattleRequestDTO;
import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Service.MailService;
import com.codekatabattle.codebattle.Service.TeamService;
import com.codekatabattle.codebattle.Service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class BattleControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BattleService battleService;

    @Mock
    private TeamService teamService;

    @Mock
    private TournamentService tournamentService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private BattleController battleController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(battleController).build();
    }

    @Test
    public void testGetBattle() throws Exception {
        Integer battleId = 1;
        Integer tournamentId = 1;
        Battle battle = new Battle(); // Supponendo che Battle sia una classe ben definita

        given(battleService.battle(any(Battle.BattleId.class))).willReturn(Optional.of(battle));

        mockMvc.perform(get("/battle/infoBattle")
                .param("battleId", battleId.toString())
                .param("tournamentId", tournamentId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(battleService).battle(any(Battle.BattleId.class));
    }

 

    @Test
    public void testUpdateBattle() throws Exception {
        Battle battle = new Battle(); // Supponendo che Battle sia una classe ben definita

        mockMvc.perform(put("/battle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(battle)))
                .andExpect(status().isOk());

        verify(battleService).updateBattle(any(Battle.class));
    }

    @Test
    public void testDeleteBattle() throws Exception {
        DeleteBattleRequestDTO requestDTO = new DeleteBattleRequestDTO(); // Assumi che DeleteBattleRequestDTO sia una classe con i campi appropriati

        mockMvc.perform(post("/battle/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(battleService).deleteBattle(any(Battle.BattleId.class));
    }

}
