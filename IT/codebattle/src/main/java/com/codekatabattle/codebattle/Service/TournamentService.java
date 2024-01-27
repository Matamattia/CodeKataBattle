package com.codekatabattle.codebattle.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.AuthorizedEducator;
import com.codekatabattle.codebattle.Model.StudentTournament;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Repository.AuthorizedEducatorRepository;
import com.codekatabattle.codebattle.Repository.StudentTournamentRepository;

import com.codekatabattle.codebattle.Repository.TournamentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired 
    private AuthorizedEducatorRepository authorizedEducatorRepository;
    @Autowired
    private StudentTournamentRepository studentTournamentRepository;


    //tournamentsInfo non so se è utile in quanto tournaments ritorna tutti i tornei.
    public Optional<Tournament> tournamentInfo(Integer tournamentId){
        return tournamentRepository.findById(tournamentId);
    }

    //return all the tournament in the platform
    public List<Tournament> tournaments() {
        return tournamentRepository.findAll();
    }
      //quando si crea un tournament però bisogna inserire chi degli educator ha i permessi
      public Tournament createTournament(Tournament newTournament, List<AuthorizedEducator> educators){
        authorizedEducatorRepository.saveAll(educators);//per i permessi
        return tournamentRepository.save(newTournament);
    }
   //return all the tournament in which the educators is autorized
    public List<Tournament> myTournamentsEducator(String educatorEmail) {
        List<AuthorizedEducator> authorizedEntries = authorizedEducatorRepository.findByEducatorEmail(educatorEmail);
        List<Tournament> tournament = new ArrayList<>();
        for (AuthorizedEducator authorizedEducator : authorizedEntries) {
            tournament.add(authorizedEducator.getTournament());
        }

        return tournament;
    }
    
    //tournaments of the student
    public List<Tournament> myTournaments(String studentEmail){
        /*
        List <Integer> tournamentsId = teamParticipantRepository.findTournamentIdsByStudentEmail(studentEmail);
        return tournamentRepository.findAllById(tournamentsId);
        */
        List<Integer> tournamentIDs = studentTournamentRepository.findTournamentIdsByStudentEmail(studentEmail);
        return tournamentRepository.findAllById(tournamentIDs);
        
    }
    
    //delete tournament
    public void deleteTournament(Tournament tournament,String emailEducator) {
        List<Tournament> tournamentAutorized = myTournamentsEducator(emailEducator);
        if(tournamentAutorized.contains(tournament)){
            tournamentRepository.deleteById(tournament.getId());
        }
        
    }
    public boolean closeTournament(Integer tournamentId, String educatorEmail) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);
    
        if (tournamentOpt.isPresent()) {
            List<Tournament> tournamentAutorized = myTournamentsEducator(educatorEmail);
    
            if (tournamentAutorized.contains(tournamentOpt.get())) {
                Tournament tournament = tournamentOpt.get();
                tournament.setIsOpen(false);
                tournamentRepository.save(tournament);
                return true;
            } else {
                return false; // Il torneo non è autorizzato per l'educatore
            }
        } else {
            return false; // Torneo non trovato
        }
    }
    
    
    public StudentTournament joinTournament(StudentTournament studentTournament) {

        return studentTournamentRepository.save(studentTournament);
    }

    
    //verifyBattleActive()
     

    
}
