package com.codekatabattle.codebattle.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codekatabattle.codebattle.DTO.BattleDTO;
import com.codekatabattle.codebattle.DTO.DeleteBattleRequestDTO;
import com.codekatabattle.codebattle.DTO.JoinTeamDTO;
import com.codekatabattle.codebattle.DTO.TeamDTO;
import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.MailStructure;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Service.MailService;
import com.codekatabattle.codebattle.Service.TeamService;
import com.codekatabattle.codebattle.Service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private MailService mailService;


  
    // Get a specific battle
    @GetMapping("/infoBattle")
    public ResponseEntity<Optional<Battle>> getBattle(@RequestParam Integer battleId, @RequestParam Integer tournamentId) {
        Battle.BattleId id = new Battle.BattleId();
        id.setBattleId(battleId);
        id.setTournament(tournamentId);
        Optional<Battle> battle = battleService.battle(id);
        return ResponseEntity.ok(battle);
    }

    @GetMapping("/infoManualEvaluation")
        public ResponseEntity<Boolean> isBattleManuallyEvaluated(@RequestParam Integer battleId, @RequestParam Integer tournamentId) {
        Battle.BattleId id = new Battle.BattleId();
        id.setBattleId(battleId);
        id.setTournament(tournamentId);
        Optional<Battle> battle = battleService.battle(id); // Assumo che il metodo sia rinominato per adattarsi meglio alla sua funzione

        // Verifica se l'oggetto Battle è presente e quindi restituisce il valore di isEvaluatedManual
        return battle.map(b -> ResponseEntity.ok(b.getIsEvaluatedManual()))
            .orElseGet(() -> ResponseEntity.ok(false)); // Restituisce false se non viene trovata nessuna battle
}


    @GetMapping("/isTerminated")
    public ResponseEntity<Boolean> isTerminated(@RequestParam Integer battleId, @RequestParam Integer tournamentId) {
        Battle.BattleId id = new Battle.BattleId();
        id.setBattleId(battleId);
        id.setTournament(tournamentId);
        Optional<Battle> battle = battleService.battle(id);

        if (battle.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            boolean isTerminated = now.isEqual(battle.get().getSubmissionDeadline()) || now.isAfter(battle.get().getSubmissionDeadline());
            return ResponseEntity.ok(isTerminated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/isRegistrationOpen")
    public ResponseEntity<Boolean> isRegistrationOpen(@RequestParam Integer battleId, @RequestParam Integer tournamentId) {
        Battle.BattleId id = new Battle.BattleId();
        id.setBattleId(battleId);
        id.setTournament(tournamentId);
        Optional<Battle> battle = battleService.battle(id);

        if (battle.isPresent()) {
        LocalDateTime now = LocalDateTime.now();
        boolean isRegistrationOpen = now.isBefore(battle.get().getRegistrationDeadline());
        return ResponseEntity.ok(isRegistrationOpen);
        } else {
        return ResponseEntity.notFound().build();
        }
    }


    
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjectsForBattle(@RequestParam Integer battleId, @RequestParam Integer tournamentId) {
        List<Project> projects = battleService.getProjectsForBattle(battleId, tournamentId);
        if (projects.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projects);
    }





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
        
        battle.setRegistrationDeadline(registrationLocalDateTime.minusHours(1));
        battle.setSubmissionDeadline(submissionLocalDateTime.minusHours(1));
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

    @PostMapping("/delete") 
    public ResponseEntity<Void> deleteBattle(@RequestBody DeleteBattleRequestDTO request) {
    Battle.BattleId id = new Battle.BattleId();
    id.setBattleId(request.getBattleId());
    id.setTournament(request.getTournamentId());
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
public ResponseEntity<Team> createTeam(@RequestBody TeamDTO teamDTO) {
    Team team = new Team();
    // Imposta i dettagli del team
    Battle.BattleId id = new Battle.BattleId();
     id.setBattleId(teamDTO.getBattleId());
    id.setTournament(teamDTO.getTournamentId());
    Optional<Battle> battle = battleService.battle(id);
    team.setBattle(battle.get());
    team.setName(teamDTO.getName());

    // Genera un codice di invito univoco per il team
    String codiceInvito = generareCodiceInvito(); // Implementa questa funzione
    team.setCodiceInvito(codiceInvito);

    Team savedTeam = teamService.createTeam(team);

    // Aggiungi il creatore al team
    teamService.joinTeam(teamDTO.getCreatorEmail(), savedTeam.getTeamId());

    // Costruisci il link di accettazione
    String linkAccettazione = "http://localhost:3000/login?redirect=/accetta-invito&codice=" + codiceInvito;

    // Invia le email di invito agli studenti con il link di accettazione
    teamDTO.getStudentEmails().forEach(email -> {
        String messaggio = "Sei stato invitato nel team: " + team.getName() + ". Per accettare l'invito, clicca su questo link: " + linkAccettazione;
        mailService.sendMail(email, new MailStructure("Invito al team", messaggio));
    });

    return ResponseEntity.ok(savedTeam);
    }
    

    @PostMapping("/join")
    public ResponseEntity<TeamParticipant> joinTeam(@RequestBody JoinTeamDTO requestDTO) {

        String studentEmail = requestDTO.getStudentEmail();
        Integer teamId = requestDTO.getTournamentId();
        TeamParticipant joinedTeam = teamService.joinTeam(studentEmail, teamId);

        return ResponseEntity.ok(joinedTeam);
    }




private LocalDateTime dateToLocalDateTime(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
}

@GetMapping("/battlesByTournament")
public ResponseEntity<List<Battle>> getBattlesByTournament(@RequestParam Integer tournamentId) {
    List<Battle> battles = battleService.getBattlesByTournament(tournamentId);
    return ResponseEntity.ok(battles);
}

@GetMapping("/checkRegistration")
public ResponseEntity<Boolean> checkUserRegistration(@RequestParam Integer battleId,
        @RequestParam String studentEmail) {
    boolean isRegistered = battleService.checkUserRegistration(battleId, studentEmail);
    return new ResponseEntity<>(isRegistered, HttpStatus.OK);
}

@PostMapping("/conferma-invito")
public ResponseEntity<Integer> confermaInvito(@RequestParam String codice, @RequestParam String studentEmail) {
// Verifica che il codice di invito sia valido
Team team = teamService.findByCodiceInvito(codice);
System.out.println(team.getTeamId());
if (team != null) {
    // Restituisci l'ID del team come Integer
    return ResponseEntity.ok(team.getTeamId());
} else {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1); // -1 per indicare che il codice di invito non è valido
}
}




public String generareCodiceInvito() {
SecureRandom random = new SecureRandom();
byte[] bytes = new byte[32]; // Lunghezza desiderata del codice
random.nextBytes(bytes);
String codice = Base64.getUrlEncoder().encodeToString(bytes);
return codice;
}



@GetMapping("/downloadCodeKata")
    public ResponseEntity<byte[]> downloadCodeKata(@RequestParam Integer projectId) {
        try {
            byte[] codeKataTeam = battleService.getCodeKataTeamByProjectId(projectId);
            if (codeKataTeam == null) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            
            String filename = "codeKataTeam.txt";
            headers.setContentDispositionFormData("attachment", filename);
            return new ResponseEntity<>(codeKataTeam, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/isMemberOfTeam")
    public ResponseEntity<Boolean> isMemberOfTeam(@RequestParam String codiceInvito,
            @RequestParam String studentEmail) {
        boolean isMember = teamService.isMemberOfTeam(codiceInvito, studentEmail);
        return ResponseEntity.ok(isMember);
    }


    @GetMapping("/isStudentInTeamForBattle")
    public ResponseEntity<Boolean> isStudentInTeamForBattle(@RequestParam String studentEmail, @RequestParam Integer battleId, @RequestParam Integer tournamentId) {
        // Creazione dell'ID della battaglia
        Battle.BattleId id = new Battle.BattleId();
        id.setBattleId(battleId);
        id.setTournament(tournamentId);
        
        // Verifica se la battaglia esiste
        Optional<Battle> battle = battleService.battle(id);
        if (!battle.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        
        boolean isInTeam = teamService.isStudentInTeamForBattle(studentEmail, battleId, tournamentId);
        
        return ResponseEntity.ok(isInTeam);
    }
    


}
