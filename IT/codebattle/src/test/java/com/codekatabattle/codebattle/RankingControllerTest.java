package com.codekatabattle.codebattle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.codekatabattle.codebattle.Service.RankingService;
import com.codekatabattle.codebattle.Controller.RankingController;
import com.codekatabattle.codebattle.Model.BattleRanking;
import com.codekatabattle.codebattle.Model.TournamentRanking;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;



import java.util.List;

public class RankingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RankingService rankingService;

    @InjectMocks
    private RankingController rankingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rankingController).build();
    }

    @Test
public void testGetTournamentRanking() throws Exception {
    int tournamentId = 1;
    List<TournamentRanking> mockRankings = List.of(new TournamentRanking()); // Crea un mock della lista delle classifiche

    given(rankingService.getTournamentRanking(tournamentId)).willReturn(mockRankings);

    mockMvc.perform(get("/rankings/tournaments")
            .param("tournamentId", String.valueOf(tournamentId)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1))); // Assicurati che la lista restituita abbia un elemento
}

 @Test
public void testCalculateTournamentRanking() throws Exception {
    int tournamentId = 1;

    mockMvc.perform(post("/rankings/calculateTournamentRanking")
            .param("tournamentId", String.valueOf(tournamentId)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Calculated Tournament Ranking")));

    verify(rankingService).createTournamentRanking(tournamentId); // Verifica che il servizio sia stato chiamato
}
@Test
public void testGetBattleRanking() throws Exception {
    int battleId = 1;
    int tournamentId = 2;
    List<BattleRanking> mockRankings = List.of(new BattleRanking()); // Crea un mock della lista delle classifiche

    given(rankingService.getBattleRanking(battleId, tournamentId)).willReturn(mockRankings);

    mockMvc.perform(get("/rankings/battles")
            .param("battleId", String.valueOf(battleId))
            .param("tournamentId", String.valueOf(tournamentId)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1))); // Assicurati che la lista restituita abbia un elemento
}

@Test
public void testCalculateBattleRanking() throws Exception {
    int battleId = 1;
    int tournamentId = 2;

    mockMvc.perform(post("/rankings/calculateBattleRanking")
            .param("battleId", String.valueOf(battleId))
            .param("tournamentId", String.valueOf(tournamentId)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Calculated Battle Ranking")));

    verify(rankingService).calculateBattleRanking(battleId, tournamentId); // Verifica che il servizio sia stato chiamato
}


}