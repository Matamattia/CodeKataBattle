package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.codekatabattle.codebattle.Model.TournamentRanking;
import com.codekatabattle.codebattle.Model.TournamentRanking.TournamentRankingId;

public interface TournamentRankingRepository extends JpaRepository<TournamentRanking, TournamentRankingId>{
}
