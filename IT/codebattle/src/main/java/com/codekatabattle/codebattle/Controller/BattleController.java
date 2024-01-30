package com.codekatabattle.codebattle.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codekatabattle.codebattle.DTO.BattleDTO;
import com.codekatabattle.codebattle.DTO.JoinTeamDTO;
import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Service.TeamService;
import com.codekatabattle.codebattle.Service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/battle")
public class BattleController {
    @Autowired
    private BattleService battleService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TournamentService tournamentService;


    //il problema è che per url dovrebbe inviare più id. Una soluzione può essere inviare solo l'id della battaglia e non quello del torneo
    // Get a specific battle
    @GetMapping("/infoBattle")
    public ResponseEntity<Optional<Battle>> getBattle(@RequestParam Integer battleId, @RequestParam Integer tournamentId) {
        Battle.BattleId id = new Battle.BattleId();
        id.setBattleId(battleId);
        id.setTournament(tournamentId);
        Optional<Battle> battle = battleService.battle(id);
        return ResponseEntity.ok(battle);
    }


    //checked V
    // Create a new battle
    @PostMapping("/create")
    public ResponseEntity<Battle> createBattle(@RequestParam("battleData") String battleDTOJson,
    @RequestParam("codeKataTests") MultipartFile codeKataTests) throws IOException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        BattleDTO battleDTO = objectMapper.readValue(battleDTOJson, BattleDTO.class);
        System.out.println("submission :" + battleDTO.getSubmissionDeadline());
        Battle battle = new Battle();
        Optional<Tournament> tournament = tournamentService.tournamentInfo(battleDTO.getTournamentId());
        
        battle.setBattleId(battleDTO.getBattleId());
        battle.setTournament(tournament.get());
        battle.setLinkRepository(battleDTO.getLinkRepository());
        battle.setMinStudent(battleDTO.getMinStudent());
        battle.setMaxStudent(battleDTO.getMaxStudent());
        battle.setDescriptionCodeKata(battleDTO.getDescriptionCodeKata());
        battle.setFileType(battleDTO.getFileType());
        battle.setIsEvaluatedManual(battleDTO.getIsEvaluatedManual());
        Date registrationDeadline = battleDTO.getRegistrationDeadline();
        Date submissionDeadline = battleDTO.getSubmissionDeadline();
        LocalDateTime registrationLocalDateTime = dateToLocalDateTime(registrationDeadline);
        LocalDateTime submissionLocalDateTime = dateToLocalDateTime(submissionDeadline);
        battle.setRegistrationDeadline(registrationLocalDateTime);
        battle.setSubmissionDeadline(submissionLocalDateTime);
        //To manage the multipart
        if (!codeKataTests.isEmpty()) {
        byte[] fileData = codeKataTests.getBytes();
        battle.setCodeKataTests(fileData);
        // Imposta il file nel tuo oggetto Battle
    }
        Battle savedBattle = battleService.saveBattle(battle);
        return ResponseEntity.ok(savedBattle);
    }


    // Update an existing battle
    @PutMapping
    public ResponseEntity<Void> updateBattle(@RequestBody Battle battle) {
        battleService.updateBattle(battle);
        return ResponseEntity.ok().build();
    }

    //checked V
    @DeleteMapping
    public ResponseEntity<Void> deleteBattle(@RequestParam Integer battleId, @RequestParam Integer tournamentId) {
        Battle.BattleId id = new Battle.BattleId();
        id.setBattleId(battleId);
        id.setTournament(tournamentId);
        battleService.deleteBattle(id);
        return ResponseEntity.ok().build();
    }

    // Get project details for a given project ID
    @GetMapping("/project")
    public ResponseEntity<Optional<Project>> getProject(@RequestParam Integer projectId) {
        Optional<Project> project = battleService.getProject(projectId);
        return ResponseEntity.ok(project);
    }

    // Verify if a battle is active
    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyBattleActive(@RequestParam Integer tournamentId) {
        boolean isActive = battleService.verifyBattleActive(tournamentId);
        return ResponseEntity.ok(isActive);
    }
    //checked V
    @PostMapping("/createTeam")
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        Team joinedTeam = teamService.createTeam(team);
        return ResponseEntity.ok(joinedTeam);
    }
    /* 
    @PostMapping("/joinTeam")
    public ResponseEntity<TeamParticipant> joinTeam(@RequestBody TeamParticipant teamParticipant) {
        TeamParticipant joinedTeam = teamService.joinTeam(teamParticipant);
        return ResponseEntity.ok(joinedTeam);
    }*/

    @PostMapping("/join")
    public ResponseEntity<TeamParticipant> joinTeam(@RequestBody JoinTeamDTO requestDTO) {
   
        String studentEmail = requestDTO.getStudentEmail();
        Integer teamId = requestDTO.getTournamentId();

        TeamParticipant joinedTeam = teamService.joinTeam(studentEmail,teamId);

        return ResponseEntity.ok(joinedTeam);
    }




private LocalDateTime dateToLocalDateTime(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
}

}
