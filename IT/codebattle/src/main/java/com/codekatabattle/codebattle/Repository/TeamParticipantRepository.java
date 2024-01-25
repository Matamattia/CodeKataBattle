package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.TeamParticipant;
import com.codekatabattle.codebattle.Model.TeamParticipant.TeamParticipantId;

public interface TeamParticipantRepository extends JpaRepository<TeamParticipant,TeamParticipantId>{
    
}
