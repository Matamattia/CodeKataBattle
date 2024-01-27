package com.codekatabattle.codebattle.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codekatabattle.codebattle.DTO.TournamentRankingDTO;
import com.codekatabattle.codebattle.Model.TournamentRanking;
import com.codekatabattle.codebattle.Model.TournamentRanking.TournamentRankingId;

public interface TournamentRankingRepository extends JpaRepository<TournamentRanking, TournamentRankingId>{

    @Query("SELECT new com.codekatabattle.codebattle.DTO.TournamentRankingDTO(tp.student.email, SUM(br.score)) " +
           "FROM BattleRanking br " +
           "JOIN br.team t " +
           "JOIN TeamParticipant tp ON t.teamId = tp.team.teamId " +
           "WHERE br.battle.tournament.id = :tournamentId " +
           "GROUP BY tp.student.email")
    List<TournamentRankingDTO> calculateTournamentRanking(@Param("tournamentId") int tournamentId);



    List<TournamentRanking> findByTournamentId(int tournamentId);



}
