package com.codekatabattle.codebattle.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Repository.StudentRepository;
import com.codekatabattle.codebattle.Repository.TeamParticipantRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamParticipantRepository teamParticipantRepository;
    @Autowired
    StudentRepository studentRepository;

    //createTeam
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }
    /* 
    public TeamParticipant joinTeam(TeamParticipant teamParticipant){
        return teamParticipantRepository.save(teamParticipant);
    }*/

     public TeamParticipant joinTeam(String studentEmail,Integer teamId){
        TeamParticipant teamParticipant = new TeamParticipant();
        Student student = studentRepository.findById(studentEmail).get();
        Team team = teamRepository.findById(teamId).get();
        teamParticipant.setStudent(student);
        teamParticipant.setTeam(team);
        
        
        return teamParticipantRepository.save(teamParticipant);
    }
    
    public Team findByCodiceInvito(String codiceInvito) {
        return teamRepository.findByCodiceInvito(codiceInvito);
    }

    public boolean isMemberOfTeam(String codiceInvito, String studentEmail) {
        // Qui dovrai scrivere la logica per determinare se lo studente è membro del team.
        // Questo esempio è semplificato e potrebbe richiedere modifiche in base alla struttura del tuo database.

        Team teamOpt = teamRepository.findByCodiceInvito(codiceInvito);
        
        Team team = teamOpt;

        return teamParticipantRepository.existsByTeam_TeamIdAndStudent_Email(team.getTeamId(), studentEmail);

    }


    public boolean isStudentInTeamForBattle(String studentEmail, Integer battleId, Integer tournamentId) {
        // Trova tutti i team per la battaglia specificata
        List<Team> teamsForBattle = teamRepository.findByBattle_BattleIdAndBattle_Tournament_Id(battleId, tournamentId);
        
        if (teamsForBattle.isEmpty()) {
            // Se non ci sono team per la battaglia, lo studente non può far parte di un team
            return false;
        }

        // Verifica se esiste un TeamParticipant che collega lo studente a uno dei team trovati
        for (Team team : teamsForBattle) {
            boolean exists = teamParticipantRepository.existsByTeam_TeamIdAndStudent_Email(team.getTeamId(), studentEmail);
            if (exists) {
                // Se troviamo uno studente che fa parte di un team per la battaglia, ritorniamo true
                return true;
            }
        }

        // Se non troviamo nessun collegamento, lo studente non fa parte di un team per questa battaglia
        return false;
    }
    
}
