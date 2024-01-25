package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.AutomatedEvaluation;
import com.codekatabattle.codebattle.Model.Project;

public interface AutomatedEvaluationRepository extends JpaRepository<AutomatedEvaluation,Project >{
    
}
