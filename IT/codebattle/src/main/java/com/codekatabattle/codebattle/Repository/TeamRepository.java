package com.codekatabattle.codebattle.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.codekatabattle.codebattle.Model.Team;


public interface TeamRepository extends JpaRepository<Team, Integer>{
    
}
