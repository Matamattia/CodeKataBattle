package com.codekatabattle.codebattle.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.AutomatedEvaluation;
import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.ManualEvaluation;
import com.codekatabattle.codebattle.Repository.AutomatedEvaluationRepository;
import com.codekatabattle.codebattle.Repository.EducatorRepository;
import com.codekatabattle.codebattle.Repository.ManualEvaluationRepository;

@Service
public class EvaluationService {
    @Autowired
    private AutomatedEvaluationRepository automatedEvaluationRepository;

    @Autowired
    private ManualEvaluationRepository manualEvaluationRepository;

    @Autowired
    private EducatorRepository educatorRepository;
    
    public ManualEvaluation evaluateProject(Integer projectId, Float personalScore, String educatorEmail) {
        AutomatedEvaluation automatedEvaluation = automatedEvaluationRepository.findByProjectId(projectId);
        Optional<Educator> educator = educatorRepository.findByEmail(educatorEmail);

        if (automatedEvaluation == null || educator.isEmpty()) {
            // Gestisci il caso in cui l'automatedEvaluation o l'educatore non esistano
            return null;
        }

        Optional<ManualEvaluation> existingManualEvaluation = manualEvaluationRepository.findByAutomatedEvaluationId(automatedEvaluation.getId());

        ManualEvaluation manualEvaluation;
        if (existingManualEvaluation.isPresent()) {
            // Se esiste già un ManualEvaluation, aggiorna il personalScore
            manualEvaluation = existingManualEvaluation.get();
            manualEvaluation.setPersonalScore(personalScore);
        } else {
            // Altrimenti, crea un nuovo ManualEvaluation
            manualEvaluation = new ManualEvaluation();
            //vedere se calcolare l'id qui
            manualEvaluation.setAutomatedEvaluation(automatedEvaluation);
            manualEvaluation.setEducator(educator.get());
            manualEvaluation.setPersonalScore(personalScore);
        }

        return manualEvaluationRepository.save(manualEvaluation);
    }
    
}
