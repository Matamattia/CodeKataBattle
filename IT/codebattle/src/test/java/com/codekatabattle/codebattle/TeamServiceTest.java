package com.codekatabattle.codebattle;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Service.TeamService;
import com.codekatabattle.codebattle.Repository.StudentRepository;
import com.codekatabattle.codebattle.Repository.TeamParticipantRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamParticipantRepository teamParticipantRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private TeamService teamService;

//Questo metodo si aspetta di salvare un team nel database. 
//Verificheremo che il metodo save del teamRepository sia stato chiamato con il team corretto.
    @Test
public void testCreateTeam() {
    Team team = new Team();
    team.setName("Team A");
    
    when(teamRepository.save(any(Team.class))).thenReturn(team);
    
    Team savedTeam = teamService.createTeam(team);
    
    assertEquals("Team A", savedTeam.getName());
    verify(teamRepository, times(1)).save(team);
}

/*Questo metodo permette a uno studente di unirsi a un team.
 Verificheremo che il metodo save del teamParticipantRepository sia stato chiamato con il TeamParticipant corretto. */
 @Test
 public void testJoinTeam() {
     Student student = new Student();
     student.setEmail("student@example.com");
     
     Team team = new Team();
     team.setTeamId(1);
     
     when(studentRepository.findById(student.getEmail())).thenReturn(Optional.of(student));
     when(teamRepository.findById(team.getTeamId())).thenReturn(Optional.of(team));
     when(teamParticipantRepository.save(any(TeamParticipant.class))).thenAnswer(i -> i.getArguments()[0]);
     
     TeamParticipant result = teamService.joinTeam(student.getEmail(), team.getTeamId());
     
     assertEquals(student.getEmail(), result.getStudent().getEmail());
     assertEquals(team.getTeamId(), result.getTeam().getTeamId());
     verify(teamParticipantRepository, times(1)).save(any(TeamParticipant.class));
 }
/*Questo metodo recupera un team tramite il suo codice di invito. 
Verificheremo che il metodo findByCodiceInvito del teamRepository restituisca il team corretto. */ 

@Test
public void testFindByCodiceInvito() {
    String codiceInvito = "INVITO123";
    Team expectedTeam = new Team();
    expectedTeam.setCodiceInvito(codiceInvito);
    
    when(teamRepository.findByCodiceInvito(codiceInvito)).thenReturn(expectedTeam);
    
    Team result = teamService.findByCodiceInvito(codiceInvito);
    
    assertEquals(codiceInvito, result.getCodiceInvito());
    verify(teamRepository, times(1)).findByCodiceInvito(codiceInvito);
}
/*Questo metodo verifica se uno studente è membro di un team tramite codice di invito. 
Verificheremo che il metodo existsByTeam_TeamIdAndStudent_Email del teamParticipantRepository sia usato per determinare l'appartenenza al team. */
@Test
public void testIsMemberOfTeam() {
    String codiceInvito = "INVITO123";
    String studentEmail = "student@example.com";
    Team team = new Team();
    team.setTeamId(1);
    team.setCodiceInvito(codiceInvito);
    
    when(teamRepository.findByCodiceInvito(codiceInvito)).thenReturn(team);
    when(teamParticipantRepository.existsByTeam_TeamIdAndStudent_Email(team.getTeamId(), studentEmail)).thenReturn(true);
    
    boolean isMember = teamService.isMemberOfTeam(codiceInvito, studentEmail);
    
    assertTrue(isMember);
    verify(teamParticipantRepository, times(1)).existsByTeam_TeamIdAndStudent_Email(team.getTeamId(), studentEmail);
}

}

