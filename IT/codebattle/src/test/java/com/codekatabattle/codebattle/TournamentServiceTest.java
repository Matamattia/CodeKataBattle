package com.codekatabattle.codebattle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;

import java.util.List;
import java.util.Optional;

import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.StudentTournament;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Repository.AuthorizedEducatorRepository;
import com.codekatabattle.codebattle.Repository.StudentTournamentRepository;
import com.codekatabattle.codebattle.Repository.TournamentRepository;
import com.codekatabattle.codebattle.Service.StudentService;
import com.codekatabattle.codebattle.Service.TournamentService;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private StudentTournamentRepository studentTournamentRepository;
    
    @InjectMocks
    private TournamentService tournamentService;

    @Mock
    private StudentService studentService;
    @Mock
    private AuthorizedEducatorRepository authorizedEducatorRepository;

    //Questo test verifica che il metodo tournamentInfo restituisca correttamente un torneo quando fornito un ID valido.
    @Test
public void testTournamentInfo() {
    Integer tournamentId = 1;
    Tournament expectedTournament = new Tournament();
    expectedTournament.setId(tournamentId);
    
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(expectedTournament));
    
    Optional<Tournament> result = tournamentService.tournamentInfo(tournamentId);
    
    assertTrue(result.isPresent());
    assertEquals(expectedTournament, result.get());
}

//Questo test verifica che il metodo tournaments restituisca una lista di tutti i tornei.
@Test
public void testTournaments() {
    List<Tournament> expectedTournaments = Arrays.asList(new Tournament(), new Tournament());
    
    when(tournamentRepository.findAll()).thenReturn(expectedTournaments);
    
    List<Tournament> result = tournamentService.tournaments();
    
    assertEquals(expectedTournaments.size(), result.size());
}
//Questo test verifica che un nuovo torneo venga correttamente salvato.
@Test
public void testCreateTournament() {
    Tournament newTournament = new Tournament();
    newTournament.setName("New Tournament");
    
    when(tournamentRepository.save(any(Tournament.class))).thenReturn(newTournament);
    
    Tournament result = tournamentService.createTournament(newTournament);
    
    assertEquals("New Tournament", result.getName());
}
//Questo metodo permette a uno studente di registrarsi a un torneo. 
//Verificheremo che il metodo saveAndFlush di studentTournamentRepository venga chiamato con il StudentTournament corretto.
@Test
public void testJoinTournament() {
    Student student = new Student();
    student.setEmail("student@example.com");
    Tournament tournament = new Tournament();
    tournament.setId(1);
    StudentTournament studentTournament = new StudentTournament();
    studentTournament.setStudent(student);
    studentTournament.setTournament(tournament);
    
    when(studentTournamentRepository.saveAndFlush(any(StudentTournament.class))).thenReturn(studentTournament);
    
    StudentTournament result = tournamentService.joinTournament(studentTournament);
    
    assertNotNull(result);
    assertEquals(student.getEmail(), result.getStudent().getEmail());
    assertEquals(tournament.getId(), result.getTournament().getId());
}


//return false wwhen the tournament is not found
@Test
public void testCloseTournament_TournamentNotFound() {
    Integer tournamentId = 999; // ID che presumibilmente non esiste nel database
    String educatorEmail = "educator@example.com";
    
    // Configura il mock per restituire un Optional vuoto, simulando che il torneo non esista
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

    // Tentativo di chiusura del torneo
    boolean result = tournamentService.closeTournament(tournamentId, educatorEmail);

    // Verifica che il risultato sia false poiché il torneo non esiste
    assertFalse(result);
    // Verifica che il metodo save non sia stato mai chiamato poiché il torneo non esiste
    verify(tournamentRepository, never()).save(any(Tournament.class));
}
/*Questo metodo chiude un torneo dato il suo ID e l'email dell'educatore che esegue l'azione. 
Verificheremo che il torneo non venga chiuso quando l'educatore è  non autorizzato autorizzato e il torneo esiste. */


    @Test
    public void testCloseTournament_UnauthorizedEducator() {
        Integer tournamentId = 1;
        String educatorEmail = "educator@example.com";
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setIsOpen(true);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(authorizedEducatorRepository.findByEducatorEmail(educatorEmail)).thenReturn(Arrays.asList());

        boolean result = tournamentService.closeTournament(tournamentId, educatorEmail);

        assertFalse(result);
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }









}

