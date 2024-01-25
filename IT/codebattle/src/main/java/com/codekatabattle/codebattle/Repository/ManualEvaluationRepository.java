package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.AutomatedEvaluation;
import com.codekatabattle.codebattle.Model.ManualEvaluation;

public interface ManualEvaluationRepository extends JpaRepository<ManualEvaluation,AutomatedEvaluation>{
    
}
