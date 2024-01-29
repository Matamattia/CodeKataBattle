package com.codekatabattle.codebattle.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.codekatabattle.codebattle.Model.Team;
import java.util.List;


public interface TeamRepository extends JpaRepository<Team, Integer>{
    
    List<Team> findByBattle_BattleIdAndBattle_Tournament_Id(Integer battleId, Integer tournamentId);

    Team findByName(String name);



    
}
