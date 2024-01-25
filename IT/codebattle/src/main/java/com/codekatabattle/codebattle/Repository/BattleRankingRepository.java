package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.BattleRanking;
import com.codekatabattle.codebattle.Model.BattleRanking.BattleRankingId;

public interface BattleRankingRepository extends JpaRepository<BattleRanking,BattleRankingId>{
    
}
