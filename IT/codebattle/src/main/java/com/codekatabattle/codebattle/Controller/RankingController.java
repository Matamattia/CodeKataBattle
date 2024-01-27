package com.codekatabattle.codebattle.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.codekatabattle.codebattle.Model.BattleRanking;
import com.codekatabattle.codebattle.Model.TournamentRanking;
import com.codekatabattle.codebattle.Service.RankingService;
import java.util.List;

@RestController
@RequestMapping("/rankings")
public class RankingController {
    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }
    
    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentRanking>> getTournamentRanking(@RequestParam int tournamentId) {
        List<TournamentRanking> tournamentRanking = rankingService.getTournamentRanking(tournamentId);
        return ResponseEntity.ok(tournamentRanking);
    }


    @GetMapping("/battles")
    public ResponseEntity<List<BattleRanking>> getBattleRanking(@RequestParam int battleId, @RequestParam int tournamentId) {
        List<BattleRanking> battleRanking = rankingService.getBattleRanking(battleId, tournamentId);
        return ResponseEntity.ok(battleRanking);
    }



    @PostMapping("/calculateTournamentRanking")
    public ResponseEntity<String> calculateTournamentRanking(@RequestParam int tournamentId) {
        rankingService.createTournamentRanking(tournamentId);
        return ResponseEntity.ok("Calculated Tournament Ranking");
    }



    @PostMapping("/calculateBattleRanking")
public ResponseEntity<String> calculateBattleRanking(@RequestParam int battleId, @RequestParam int tournamentId) {
    rankingService.calculateBattleRanking(battleId, tournamentId);
    return ResponseEntity.ok("Calculated Battle Ranking");
}






}
