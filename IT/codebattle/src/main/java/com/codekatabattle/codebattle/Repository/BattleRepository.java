package com.codekatabattle.codebattle.Repository;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Battle.BattleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRepository extends JpaRepository<Battle, BattleId>{
    
}
