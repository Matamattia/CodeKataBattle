package com.codekatabattle.codebattle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codekatabattle.codebattle.Controller.TournamentController;
import com.codekatabattle.codebattle.DTO.TournamenteCreateDTO;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.TournamentService;
import com.codekatabattle.codebattle.Service.EducatorService;
import com.codekatabattle.codebattle.Service.StudentService;
import com.codekatabattle.codebattle.Model.Educator;


import java.util.Arrays;
import java.util.Date;

import java.util.Optional;

public class TournamentControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TournamentService tournamentService;
    
    @Mock
    private EducatorService educatorService; // Mock aggiuntivo

    @Mock
    private StudentService studentService; // Mock aggiuntivo
    @InjectMocks
    private TournamentController tournamentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tournamentController).build();
        given(educatorService.getEducatorByEmail(anyString())).willReturn(Optional.of(new Educator()));
    }

    @Test
    public void testGetTournament() throws Exception {
        Integer id = 1;
        Tournament tournament = new Tournament();

        given(tournamentService.tournamentInfo(id)).willReturn(Optional.of(tournament));

        mockMvc.perform(get("/tournament/myTournament")
                .param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(tournamentService).tournamentInfo(id);
    }

    @Test
    public void testCreateTournament() throws Exception {
        Tournament tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setDescription("A description for the test tournament");
        tournament.setIsOpen(true);
        tournament.setRegistrationDeadline(new Date());
    
        TournamenteCreateDTO tournamentCreateDTO = new TournamenteCreateDTO();
        tournamentCreateDTO.setTournament(tournament);
        tournamentCreateDTO.setEducatorEmails(Arrays.asList("educator1@example.com", "educator2@example.com"));
        tournamentCreateDTO.setEducatorCreator("creator@example.com");
    
        given(tournamentService.createTournament(any(Tournament.class))).willReturn(tournament);
    
        mockMvc.perform(post("/tournament/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tournamentCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(tournament.getName()));
    
        verify(tournamentService).createTournament(any(Tournament.class));
    }



}
