
package com.codekatabattle.codebattle.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.ManualEvaluation;

public interface ManualEvaluationRepository extends JpaRepository<ManualEvaluation,Integer>{
      ManualEvaluation findByAutomatedEvaluationId(int id);
      

      Optional<ManualEvaluation> findByAutomatedEvaluationId(Integer automatedEvaluationId);
}

