package com.codekatabattle.codebattle.Controller;

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
import com.codekatabattle.codebattle.Model.AuthorizedEducator;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.StudentTournament;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Repository.StudentRepository;
import com.codekatabattle.codebattle.Service.StudentService;
import com.codekatabattle.codebattle.Service.TournamentService;

@RestController
@RequestMapping("/tournament")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private StudentService studentService;

//checked V
    @GetMapping("/myTournament")
    public ResponseEntity<Optional<Tournament>> getTournament(@RequestParam Integer id) {
        Optional<Tournament> tournament = tournamentService.tournamentInfo(id);
            return ResponseEntity.ok(tournament);
        
    }
//checked V
    @GetMapping("/all")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.tournaments();
        return ResponseEntity.ok(tournaments);
    }
   

    //checked V
    @PostMapping("/create")
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
        Tournament insertedTournament = tournamentService.createTournament(tournament);
        return ResponseEntity.ok(insertedTournament);
    }
    

    //checked V
    @PostMapping("/authorized")
    public ResponseEntity<List<AuthorizedEducator>> createTournament(@RequestBody List<AuthorizedEducator> educators) {
        List<AuthorizedEducator> createdTournament = tournamentService.insertAuthorized(educators);
        return ResponseEntity.ok(createdTournament);
    }

    //checked V
    @GetMapping("/educator")
    public ResponseEntity<List<Tournament>> getEducatorTournaments(@RequestParam String educatorEmail) {
        List<Tournament> tournaments = tournamentService.myTournamentsEducator(educatorEmail);
        return ResponseEntity.ok(tournaments);
    }
    
//checked V
   @GetMapping("/student")
public ResponseEntity<List<Tournament>> getStudentTournaments(@RequestParam String studentEmail) {
    System.out.println(studentEmail);
    List<Tournament> tournaments = tournamentService.myTournaments(studentEmail);
    return ResponseEntity.ok(tournaments);
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



    //checked V, vi è il problema di cascade per i vincoli
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

    //checked V
    @PostMapping("/close")
    public ResponseEntity<Boolean> closeTournament(@RequestParam Integer tournamentId, @RequestBody EducatorEmailDTO educatorEmail) {
    boolean closed = tournamentService.closeTournament(tournamentId, educatorEmail.getEducatorEmail());
    if (closed) {
        return ResponseEntity.ok(true);
    } else {
        return ResponseEntity.badRequest().body(false);
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
