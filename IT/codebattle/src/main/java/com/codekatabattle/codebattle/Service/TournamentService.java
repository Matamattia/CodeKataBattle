package com.codekatabattle.codebattle.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.AuthorizedEducator;
import com.codekatabattle.codebattle.Model.Student;
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

    @Autowired
    private StudentService studentService;


    public Optional<Tournament> tournamentInfo(Integer tournamentId){
        return tournamentRepository.findById(tournamentId);
    }

    //return all the tournament in the platform
    public List<Tournament> tournaments() {
        return tournamentRepository.findAll();
    }
      
       public List<AuthorizedEducator> insertAuthorized(List<AuthorizedEducator> educators){
        return authorizedEducatorRepository.saveAll(educators);   
    }

    public Tournament createTournament(Tournament tournament){
        return tournamentRepository.save(tournament);
    }


   //return all the tournament in which the educators is authorized
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
            
            //fare la supposizione che nessuno studente è iscritto
            //non sono impostati i vincoli che se elimino tournament, devo eliminare anche gli altri
            
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
        
        StudentTournament stud = studentTournamentRepository.saveAndFlush(studentTournament);//flush probabilmente si può eliminare
        return stud;
       
    }


    public boolean isStudentRegistered(Tournament tournament, String studentEmail) {
    // Cerca lo studente in base all'email
    Optional<Student> student = studentService.getStudentByEmail(studentEmail);

    if (!student.isPresent()) {
        // Lo studente non è stato trovato
        return false;
    }

    // Crea un oggetto StudentTournamentId con l'ID del torneo e l'email dello studente
    StudentTournament.StudentTournamentId studentTournamentId = new StudentTournament.StudentTournamentId();
    studentTournamentId.setTournament(tournament.getId());
    studentTournamentId.setStudent(studentEmail);

    // Cerca lo studentTournament utilizzando l'ID del torneo e l'email dello studente
    Optional<StudentTournament> studentTournament = studentTournamentRepository.findById(studentTournamentId);
    
    
    return studentTournament.isPresent();
}


    

    
    

    
}
