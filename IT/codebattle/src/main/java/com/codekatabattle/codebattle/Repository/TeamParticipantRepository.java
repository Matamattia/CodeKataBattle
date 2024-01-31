package com.codekatabattle.codebattle.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Model.TeamParticipant.TeamParticipantId;

public interface TeamParticipantRepository extends JpaRepository<TeamParticipant,TeamParticipantId>{
    @Query("SELECT DISTINCT b.tournament.id FROM TeamParticipant tp JOIN tp.team t JOIN t.battle b WHERE tp.student.email = :studentEmail")
    List<Integer> findTournamentIdsByStudentEmail(@Param("studentEmail") String studentEmail);

    @Query("SELECT DISTINCT tp.student.email FROM TeamParticipant tp WHERE tp.team.teamId = :teamId")
    List<String> findStudentEmailsByTeamId(@Param("teamId") Integer teamId);

    List<TeamParticipant> findByStudentEmail(String email);


    boolean existsByTeam_BattleAndStudent(Battle battle, Student student);



}
