package com.codekatabattle.codebattle.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codekatabattle.codebattle.Model.AuthorizedEducator;
import com.codekatabattle.codebattle.Model.StudentTournament;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.TournamentService;

@RestController
@RequestMapping("/tournament")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;


    @GetMapping("/myTournament")
    public ResponseEntity<Optional<Tournament>> getTournament(@RequestParam Integer id) {
        Optional<Tournament> tournament = tournamentService.tournamentInfo(id);
            return ResponseEntity.ok(tournament);
        
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.tournaments();
        return ResponseEntity.ok(tournaments);
    }
    @PostMapping("/create")
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament newTournament, @RequestBody List<AuthorizedEducator> educators) {
        Tournament createdTournament = tournamentService.createTournament(newTournament, educators);
        return ResponseEntity.ok(createdTournament);
    }

    @GetMapping("/educator")
    public ResponseEntity<List<Tournament>> getEducatorTournaments(@RequestBody String educatorEmail) {
        List<Tournament> tournaments = tournamentService.myTournamentsEducator(educatorEmail);
        return ResponseEntity.ok(tournaments);
    }

    @GetMapping("/student")
    public ResponseEntity<List<Tournament>> getStudentTournaments(@RequestBody String studentEmail) {
        List<Tournament> tournaments = tournamentService.myTournaments(studentEmail);
        return ResponseEntity.ok(tournaments);
    }
    

    @PostMapping("/join")
    public ResponseEntity<StudentTournament> joinTournament(@RequestBody StudentTournament studentTournament) {
        StudentTournament joinedTournament = tournamentService.joinTournament(studentTournament);
        return ResponseEntity.ok(joinedTournament);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTournament(@RequestParam Integer id, @RequestBody String educatorEmail) {
        Tournament tournament = tournamentService.tournamentInfo(id).orElse(null);
        if (tournament != null) {
          tournamentService.deleteTournament(tournament, educatorEmail);
        return ResponseEntity.noContent().build();
        } else {
        return ResponseEntity.notFound().build();
    }
    }

    @PostMapping("/close")
    public ResponseEntity<Boolean> closeTournament(@RequestParam Integer tournamentId, @RequestBody String educatorEmail) {
    boolean closed = tournamentService.closeTournament(tournamentId, educatorEmail);
    if (closed) {
        return ResponseEntity.ok(true);
    } else {
        return ResponseEntity.badRequest().body(false);
    }
    }

}
