package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codekatabattle.codebattle.Model.AutomatedEvaluation;

public interface AutomatedEvaluationRepository extends JpaRepository<AutomatedEvaluation, Integer >{
      @Query("SELECT ae FROM AutomatedEvaluation ae WHERE ae.project.projectId = :projectId")
      AutomatedEvaluation findByProjectId(@Param("projectId") int projectId);
}
