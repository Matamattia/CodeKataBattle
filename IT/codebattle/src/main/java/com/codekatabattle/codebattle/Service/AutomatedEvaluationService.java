package com.codekatabattle.codebattle.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.AutomatedEvaluation;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Repository.AutomatedEvaluationRepository;

@Service
public class AutomatedEvaluationService {

    @Autowired
    private AutomatedEvaluationRepository automatedEvaluationRepository;

    public void createOrUpdateEvaluation(Project project, Float functionalScore, Float timelinessScore) {
        AutomatedEvaluation evaluation = automatedEvaluationRepository.findByProjectId(project.getProjectId());

        if (evaluation != null) {
            // Aggiorna i punteggi esistenti
            evaluation.setFunctionalScore(functionalScore);
            evaluation.setTimelinessScore(timelinessScore);
            evaluation.setTotalScore((functionalScore + timelinessScore)/2); // Calcolo semplice del totalScore
        } else {
            // Crea una nuova valutazione automatizzata
            evaluation = new AutomatedEvaluation();
            evaluation.setProject(project);
            evaluation.setFunctionalScore(functionalScore);
            evaluation.setTimelinessScore(timelinessScore);
            evaluation.setTotalScore((functionalScore + timelinessScore)/2); // Calcolo semplice del totalScore
        }

        automatedEvaluationRepository.save(evaluation);
    }
}
