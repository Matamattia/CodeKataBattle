package com.codekatabattle.codebattle.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codekatabattle.codebattle.DTO.EducatorEmailDTO;
import com.codekatabattle.codebattle.DTO.TournamenteCreateDTO;
import com.codekatabattle.codebattle.Model.AuthorizedEducator;
import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.MailStructure;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.StudentTournament;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Service.EducatorService;
import com.codekatabattle.codebattle.Service.MailService;
import com.codekatabattle.codebattle.Service.StudentService;
import com.codekatabattle.codebattle.Service.TournamentService;


@RestController
@RequestMapping("/tournament")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private EducatorService educatorService;
    @Autowired
    private MailService mailService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private BattleService battleService;


    @GetMapping("/myTournament")
    public ResponseEntity<Optional<Tournament>> getTournament(@RequestParam Integer id) {
        Optional<Tournament> tournament = tournamentService.tournamentInfo(id);
            return ResponseEntity.ok(tournament);
        
    }
    @GetMapping("/statusTournament")
    public ResponseEntity<Boolean> getStatusTournament(@RequestParam Integer id) {
        Optional<Tournament> tournament = tournamentService.tournamentInfo(id);
        boolean isOpen = tournament.get().getIsOpen();
            return ResponseEntity.ok(isOpen);
        
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.tournaments();
        return ResponseEntity.ok(tournaments);
    }
   


    @PostMapping("/create")
    public ResponseEntity<Tournament> createTournament(@RequestBody TournamenteCreateDTO tournamentCreateDTO) {
        Tournament tournament = tournamentCreateDTO.getTournament();
        
        Tournament insertedTournament = tournamentService.createTournament(tournament);
        //for authorized educators
        List<String> educatorEmails = tournamentCreateDTO.getEducatorEmails();
        List<AuthorizedEducator> authorizedEducators = new ArrayList<>();

        for(String email: educatorEmails){
            AuthorizedEducator authorizedEducator = new AuthorizedEducator();
            authorizedEducator.setTournament(tournament); // Imposta il riferimento al torneo
            Educator educator = educatorService.getEducatorByEmail(email).get();
            authorizedEducator.setEducator(educator);
            authorizedEducators.add(authorizedEducator);
        }
        AuthorizedEducator authorizedEducator = new AuthorizedEducator();
        authorizedEducator.setTournament(tournament);
        Educator creator = educatorService.getEducatorByEmail(tournamentCreateDTO.getEducatorCreator()).get();
        authorizedEducator.setEducator(creator);
        authorizedEducators.add(authorizedEducator);

        tournamentService.insertAuthorized(authorizedEducators);
        List<Student> students = studentService.getAllStudents();
        for (Student student : students) {
            
            mailService.sendMail(student.getEmail(), new MailStructure(student.getName(),"The Tournament  " + tournament.getName()+ " with id: "+tournament.getId()+" is created" ));
           
        }
        
        return ResponseEntity.ok(insertedTournament);
    }

    


    @PostMapping("/authorized")
    public ResponseEntity<List<AuthorizedEducator>> createTournament(@RequestBody List<AuthorizedEducator> educators) {
        List<AuthorizedEducator> createdTournament = tournamentService.insertAuthorized(educators);
        return ResponseEntity.ok(createdTournament);
    }


    @GetMapping("/educator")
    public ResponseEntity<List<Tournament>> getEducatorTournaments(@RequestParam String educatorEmail) {
        List<Tournament> tournaments = tournamentService.myTournamentsEducator(educatorEmail);
        return ResponseEntity.ok(tournaments);
    }
    

   @GetMapping("/student")
public ResponseEntity<List<Tournament>> getStudentTournaments(@RequestParam String studentEmail) {
    System.out.println(studentEmail);
    List<Tournament> tournaments = tournamentService.myTournaments(studentEmail);
    return ResponseEntity.ok(tournaments);
}

@GetMapping("/registrationStatus")
public ResponseEntity<Boolean> getRegistrationStatus(@RequestParam Integer id) {
    boolean isRegistrationOpen = tournamentService.isRegistrationOpen(id);
    return ResponseEntity.ok(isRegistrationOpen);
}


    

@PostMapping("/join")
public ResponseEntity<StudentTournament> joinTournament(@RequestParam Integer tournamentId, @RequestParam String studentEmail) {
    // Crea un nuovo oggetto StudentTournament
    StudentTournament studentTournament = new StudentTournament();
    
    // Trova il torneo e lo studente in base agli ID forniti (gestire i casi in cui non vengono trovati)
    Optional<Tournament> tournament = tournamentService.tournamentInfo(tournamentId);
    Optional<Student> student = studentService.getStudentByEmail(studentEmail);

    if (tournament == null || student == null) {
        // Gestisci il caso in cui il torneo o lo studente non vengono trovati
        return ResponseEntity.badRequest().build();
    }

    studentTournament.setTournament(tournament.get());
    studentTournament.setStudent(student.get());

    // Registra lo studente al torneo
    StudentTournament joinedTournament = tournamentService.joinTournament(studentTournament);

    return ResponseEntity.ok(joinedTournament);
}


@GetMapping("/isAuthorized")
public ResponseEntity<Boolean> isAuthorized(@RequestParam String educatorEmail, @RequestParam Integer tournamentId) {
    boolean isAuthorized = tournamentService.isEducatorAuthorizedForTournament(educatorEmail, tournamentId);
    return ResponseEntity.ok(isAuthorized);
}



   
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteTournament(@RequestParam Integer id, @RequestBody EducatorEmailDTO educatorEmail) {
        Tournament tournament = tournamentService.tournamentInfo(id).orElse(null);
        if (tournament != null) {
          tournamentService.deleteTournament(tournament, educatorEmail.getEducatorEmail());
        return ResponseEntity.noContent().build();
        } else {
        return ResponseEntity.notFound().build();
    }
    }


    @PostMapping("/close")
    public ResponseEntity<Boolean> closeTournament(@RequestParam Integer tournamentId, @RequestBody EducatorEmailDTO educatorEmail) {
        boolean notActive = battleService.verifyBattleActive(tournamentId);
        System.out.println(notActive);
        if( notActive == false){
            return ResponseEntity.ok(false);
        }
        boolean closed = tournamentService.closeTournament(tournamentId, educatorEmail.getEducatorEmail());
        if (closed) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/checkRegistration")
public ResponseEntity<Boolean> checkRegistration(@RequestParam Integer tournamentId, @RequestParam String studentEmail) {
    // Trova il torneo in base all'ID fornito
    Optional<Tournament> tournament = tournamentService.tournamentInfo(tournamentId);
    
    if (!tournament.isPresent()) {
        // Il torneo non è stato trovato
        return ResponseEntity.notFound().build();
    }

    // Verifica se lo studente è registrato al torneo
    boolean isRegistered = tournamentService.isStudentRegistered(tournament.get(), studentEmail);

   
    return ResponseEntity.ok(isRegistered);
}

}
