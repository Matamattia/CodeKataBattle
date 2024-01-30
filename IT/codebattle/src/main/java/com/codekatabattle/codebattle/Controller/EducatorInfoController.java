package com.codekatabattle.codebattle.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Service.EducatorService;
import com.codekatabattle.codebattle.Service.TournamentService;

@RestController
@RequestMapping("/api/educators")
public class EducatorInfoController {
    
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private BattleService battleService;
    @Autowired
    private EducatorService educatorService;


    @GetMapping("/{email}")
    public ResponseEntity<?> getEducatorInfo(@PathVariable String email) {
        Optional<Educator> educatorOpt = educatorService.getEducatorByEmail(email);
        if (!educatorOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Educator educator = educatorOpt.get();
        List<Tournament> tournaments = tournamentService.myTournamentsEducator(email);
        //estraggo gli id dei tornei
        List<Integer> tournamentIds = tournaments.stream()
            .map(Tournament::getId)
            .collect(Collectors.toList());

        List<Battle> battles = battleService.getBattlesForEducator(tournamentIds);


        Map<String, Object> response = new HashMap<>();
        response.put("educator", educator);
        response.put("tournaments", tournaments);
        response.put("battles", battles);

        return ResponseEntity.ok(response);
    }
}
