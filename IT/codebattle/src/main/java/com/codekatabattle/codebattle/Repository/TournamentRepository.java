package com.codekatabattle.codebattle.Repository;

import com.codekatabattle.codebattle.Model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer>{
    
     
}
