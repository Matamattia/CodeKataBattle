package com.codekatabattle.codebattle.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codekatabattle.codebattle.Model.StudentTournament;
import com.codekatabattle.codebattle.Model.StudentTournament.StudentTournamentId;

public interface StudentTournamentRepository extends JpaRepository<StudentTournament,StudentTournamentId>{
    //List<StudentTournament> findByStudentEmail(String email);
    @Query("SELECT st.tournament.id FROM StudentTournament st WHERE st.student.email = :email")
    List<Integer> findTournamentIdsByStudentEmail(String email);

    List<StudentTournament> findByStudentEmail(String email);

}
