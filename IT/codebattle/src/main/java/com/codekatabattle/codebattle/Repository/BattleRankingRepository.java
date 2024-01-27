package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.codekatabattle.codebattle.Model.BattleRanking;
import com.codekatabattle.codebattle.Model.BattleRanking.BattleRankingId;
import java.util.List;

public interface BattleRankingRepository extends JpaRepository<BattleRanking,BattleRankingId>{
    @Query("SELECT br FROM BattleRanking br WHERE br.battle.battleId = :battleId AND br.battle.tournament.id = :tournamentId")
    List<BattleRanking> findByBattleIdAndTournamentId(@Param("battleId") Integer battleId, @Param("tournamentId") Integer tournamentId);
}
