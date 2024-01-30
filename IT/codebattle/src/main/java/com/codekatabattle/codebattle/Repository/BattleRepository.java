package com.codekatabattle.codebattle.Repository;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Battle.BattleId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRepository extends JpaRepository<Battle, BattleId>{
    List<Battle> findByTournamentId(Integer tournamentId);
    
    @Query("SELECT b FROM Battle b WHERE b.tournament.id IN :tournamentIds")
    List<Battle> findBattlesByTournamentIds(List<Integer> tournamentIds);

    @Query("SELECT MAX(b.battleId) FROM Battle b")
    Integer findMaxBattleId();
}
